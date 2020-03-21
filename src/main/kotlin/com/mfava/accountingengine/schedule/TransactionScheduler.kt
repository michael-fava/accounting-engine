package com.mfava.accountingengine.schedule

import com.mfava.accountingengine.filters.TransactionFilter
import com.mfava.accountingengine.model.TransactionStatus
import com.mfava.accountingengine.service.TransactionService
import org.springframework.context.annotation.Profile
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.function.Consumer


@Component
@Profile(value = ["!test"])
class TransactionScheduler(val transactionService: TransactionService) {


    @Scheduled(initialDelay = 10000L, fixedDelay = 1000L)
    fun executeIntiatedTransactions() {

        transactionService.getTransactionsByFilter(TransactionFilter(null, null, null, TransactionStatus.INITIATED), Pageable.unpaged()).get()
                .peek { tr -> transactionService.updateState(tr, TransactionStatus.PENDING) }
                .forEach(Consumer { t -> transactionService.executeTransaction(t) })
    }
}
