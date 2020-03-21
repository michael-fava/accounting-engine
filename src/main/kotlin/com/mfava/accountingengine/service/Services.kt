package com.mfava.accountingengine.service

import com.mfava.accountingengine.filters.TransactionFilter
import com.mfava.accountingengine.model.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.util.*

interface AccountsService {

    fun createAccount(request : AccountRequest) : Account

    fun getAllAccount() : List<Account>

    fun getAccount(id: UUID) : Account

    fun updateAccountBalance(account: Account, amount: BigDecimal, operation : AccountBalanceOperation) : Account
}

interface TransactionService{

    fun getTransactionsByFilter(filter : TransactionFilter, pageable: Pageable) : Page<TransactionEntry>

    fun createTransaction(request: TransactionRequest) : TransactionEntry

    fun cancelTransaction(transactionId: UUID)

    fun executeTransaction(transactionEntry: TransactionEntry)

    fun updateState(transactionEntry: TransactionEntry, transactionStatus: TransactionStatus)
}
