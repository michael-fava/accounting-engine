package com.mfava.accountingengine.repo

import com.mfava.accountingengine.model.Account
import com.mfava.accountingengine.model.AccountType
import com.mfava.accountingengine.model.TransactionEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.LockModeType

@Repository
interface AccountRepository : JpaRepository<Account, UUID>{

    fun findAllByAccountTypeLike(accountType: AccountType) : List<Account>
}

@Repository
interface TransactionRepository : JpaRepository<TransactionEntry, UUID>, QuerydslPredicateExecutor<TransactionEntry> {


}

