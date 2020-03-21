package com.mfava.accountingengine.service.impl

import com.mfava.accountingengine.exception.BaseException
import com.mfava.accountingengine.exception.ErrorCode
import com.mfava.accountingengine.model.Account
import com.mfava.accountingengine.model.AccountBalanceOperation
import com.mfava.accountingengine.model.AccountRequest
import com.mfava.accountingengine.model.AccountType
import com.mfava.accountingengine.repo.AccountRepository
import com.mfava.accountingengine.service.AccountsService
import com.mfava.accountingengine.utils.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Service
class AccountServiceImpl(val accountRepository: AccountRepository, val modelMapper: ModelMapper) : AccountsService {

    @Transactional
    override fun createAccount(request: AccountRequest): Account {
        validateAccountRequest(request)
        val newAccount = Account(UUID.randomUUID(), request.beneficiaryName, request.pinNumber, BigDecimal.ZERO, Currency.getInstance("EUR"))
        return accountRepository.save(newAccount)
    }

    override fun getAllAccount(): List<Account> {
        return accountRepository.findAllByAccountTypeLike(AccountType.CUSTOMER)
    }

    override fun getAccount(id: UUID): Account {
        return accountRepository.findById(id).orElseThrow { -> BaseException(ErrorCode.ACCOUNT_NOT_FOUND, "Account not found by supplied id") }
    }

    @Transactional
    override fun updateAccountBalance(account: Account, amount: BigDecimal, operation: AccountBalanceOperation): Account {
        accountRepository.findById(account.id).orElseThrow { -> BaseException(ErrorCode.ACCOUNT_NOT_FOUND, "provided Account not found in database") }
        when (operation){
            AccountBalanceOperation.ADD -> account.balance += amount
            AccountBalanceOperation.SUBTRACT -> account.balance -= amount
        }
        return accountRepository.save(account)
    }

    private fun validateAccountRequest(request: AccountRequest) {
        requireNotNull(request.beneficiaryName)
        requireNotNull(request.pinNumber)
    }
}
