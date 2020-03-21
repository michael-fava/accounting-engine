package com.mfava.accountingengine.service.impl

import com.mfava.accountingengine.exception.BaseException
import com.mfava.accountingengine.exception.ErrorCode
import com.mfava.accountingengine.filters.TransactionFilter
import com.mfava.accountingengine.model.*
import com.mfava.accountingengine.repo.TransactionRepository
import com.mfava.accountingengine.service.AccountsService
import com.mfava.accountingengine.service.TransactionService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate

@Service
class TransactionServiceImpl(val transactionRepository: TransactionRepository, val accountsService: AccountsService) : TransactionService {

    override fun getTransactionsByFilter(filter: TransactionFilter, pageable: Pageable): Page<TransactionEntry> {
        return transactionRepository.findAll(filter.toPredicate(), pageable)
    }

    @Transactional
    override fun createTransaction(request: TransactionRequest): TransactionEntry {
        validateTransactionRequest(request)
        val transactionEntry = convertToTransactionEntry(request)
        return transactionRepository.save(transactionEntry)
    }

    @Transactional
    override fun cancelTransaction(transactionId: UUID) {
        val transaction = transactionRepository.findById(transactionId)

        if (!transaction.isPresent) {
            throw BaseException(ErrorCode.TRANSACTION_NOT_FOUND, "Transaction with id provided not found")
        }
        transaction.filter(Predicate { t -> t.transactionStatus == TransactionStatus.INITIATED })
                .ifPresent(Consumer { tr -> updateState(tr, TransactionStatus.CANCELLED) })
    }

    @Transactional
    override fun executeTransaction(transactionEntry: TransactionEntry) {

        val sourceAccount = accountsService.getAccount(UUID.fromString(if (transactionEntry.sourceType == TransactionEndpointType.ACCOUNT) transactionEntry.sourceIdentifier else AccountType.SYSTEM_DEPOSIT.accountId))
        val destinationAccount = accountsService.getAccount(UUID.fromString(if (transactionEntry.destinationType == TransactionEndpointType.ACCOUNT) transactionEntry.destinationIdentifier else AccountType.SYSTEM_WITHDRAW.accountId))

        if (!canFulfillTransaction(sourceAccount, transactionEntry)) {
            throw BaseException(ErrorCode.INSUFFICIENT_FUNDS, "a/c:" + sourceAccount.id)
        }

        //perform money movement fom source to destination

        accountsService.updateAccountBalance(sourceAccount, transactionEntry.amount, AccountBalanceOperation.SUBTRACT)
        accountsService.updateAccountBalance(destinationAccount, transactionEntry.amount, AccountBalanceOperation.ADD)
        updateState(transactionEntry, TransactionStatus.COMPLETED)
    }

    @Transactional
    override fun updateState(transactionEntry: TransactionEntry, transactionStatus: TransactionStatus) {
        transactionEntry.transactionStatus = transactionStatus
        transactionRepository.save(transactionEntry)
    }

    private fun canFulfillTransaction(sourceAccount: Account, t: TransactionEntry): Boolean {
        return sourceAccount.balance >= t.amount
    }

    private fun validateTransactionRequest(request: TransactionRequest) {
        requireNotNull(request.sourceEndpointType)
        requireNotNull(request.destinationEndpointType)
        when (request.transactionType) {
            TransactionType.TRANSFER -> require(request.sourceEndpointType == TransactionEndpointType.ACCOUNT
                    && request.destinationEndpointType == TransactionEndpointType.ACCOUNT)
            TransactionType.DEPOSIT -> require(request.sourceEndpointType != TransactionEndpointType.ACCOUNT
                    && request.destinationEndpointType == TransactionEndpointType.ACCOUNT)
            TransactionType.WITHDRAW -> require(request.sourceEndpointType == TransactionEndpointType.ACCOUNT
                    && request.destinationEndpointType != TransactionEndpointType.ACCOUNT)
        }
        if (request.sourceEndpointType == TransactionEndpointType.ACCOUNT &&
                request.sourceAccountPin != accountsService.getAccount(UUID.fromString(request.sourceIdentifier)).pinNumber) {
            throw BaseException(ErrorCode.ACCOUNT_PIN_MISMATCH, "source account pin does not match provided pin")
        }

    }

    private fun convertToTransactionEntry(transactionRequest: TransactionRequest): TransactionEntry {
        return TransactionEntry(UUID.randomUUID(),
                transactionRequest.transactionType,
                TransactionStatus.INITIATED,
                transactionRequest.amount,
                transactionRequest.currency,
                transactionRequest.sourceEndpointType,
                transactionRequest.sourceIdentifier,
                transactionRequest.destinationEndpointType,
                transactionRequest.destinationIdentifier,
                transactionRequest.comment)
    }


}
