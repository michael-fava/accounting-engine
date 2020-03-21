package com.mfava.accountingengine.filters

import com.mfava.accountingengine.model.QTransactionEntry
import com.mfava.accountingengine.model.TransactionEndpointType
import com.mfava.accountingengine.model.TransactionStatus
import com.mfava.accountingengine.model.TransactionType
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.BooleanExpression
import java.util.*

class TransactionFilter(
        private val transactionId: UUID?,
        private val accountId: UUID?,
        private val transactionType: TransactionType?,
        private val transactionStatus: TransactionStatus?
) {

    fun toPredicate(): Predicate {
        var qtransactionEntry: QTransactionEntry = QTransactionEntry.transactionEntry
        var predicate: BooleanExpression = qtransactionEntry.transactionType.isNotNull

        if (transactionStatus != null) {
            predicate = predicate.and(qtransactionEntry.transactionStatus.eq(transactionStatus))
        }

        if (transactionType != null) {
            predicate = predicate.and(qtransactionEntry.transactionType.eq(transactionType))
        }

        if (accountId != null) {
            predicate = predicate.and(
                    (qtransactionEntry.sourceType.eq(TransactionEndpointType.ACCOUNT).and(qtransactionEntry.sourceIdentifier.eq(accountId.toString()))).or
                    (qtransactionEntry.destinationType.eq(TransactionEndpointType.ACCOUNT).and(qtransactionEntry.destinationIdentifier.eq(accountId.toString())))
            )
        }

        if (transactionId != null) {
            predicate = predicate.and(qtransactionEntry.id.eq(transactionId))
        }

        return predicate
    }

}
