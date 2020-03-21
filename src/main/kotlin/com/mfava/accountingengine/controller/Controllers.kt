package com.mfava.accountingengine.controller

import com.mfava.accountingengine.dto.*
import com.mfava.accountingengine.filters.TransactionFilter
import com.mfava.accountingengine.model.AccountRequest
import com.mfava.accountingengine.model.TransactionRequest
import com.mfava.accountingengine.service.AccountsService
import com.mfava.accountingengine.service.TransactionService
import com.mfava.accountingengine.utils.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@Validated
@RestController
@RequestMapping("/accounts")
class AccountsController(val accountsService: AccountsService,
                         val modelMapper: ModelMapper) {

    @PostMapping
    fun createAccount(@Valid @RequestBody request: ApiAccountCreateRequest): ApiAccount {
        return modelMapper.map(accountsService.createAccount(modelMapper.map(request, AccountRequest::class.java)), ApiAccount::class.java)
    }

    @GetMapping
    fun getAllAccounts(): List<ApiAccount> {
        return modelMapper.mapAsList(accountsService.getAllAccount(), ApiAccount::class.java)
    }

    @GetMapping(value = ["/{id}"])
    fun getAccountById(@PathVariable id: UUID): ApiAccount {
        return modelMapper.map(accountsService.getAccount(id), ApiAccount::class.java)
    }
}

@RestController
@RequestMapping("/transactions")
class TransactionsController(val transactionService: TransactionService, val modelMapper: ModelMapper) {

    @GetMapping
    fun getTransactionsByFilter(filter: ApiTransactionFilter, pageable: Pageable): Page<ApiTransactionEntry> {
        return transactionService.getTransactionsByFilter(modelMapper.map(filter, TransactionFilter::class.java), pageable).map { t -> modelMapper.map(t, ApiTransactionEntry::class.java) }
    }

    @PostMapping
    fun createTransaction(@RequestBody @Valid transactionRequest: ApiTransactionRequest): ApiTransactionEntry {
        return modelMapper.map(transactionService.createTransaction(modelMapper.map(transactionRequest, TransactionRequest::class.java)), ApiTransactionEntry::class.java)
    }

    @DeleteMapping(value = ["/{id}"])
    fun cancelTransaction(@PathVariable id: UUID){
        transactionService.cancelTransaction(id)
    }
}
