package com.mfava.accountingengine.dto

import com.mfava.accountingengine.model.TransactionEndpointType
import com.mfava.accountingengine.model.TransactionStatus
import com.mfava.accountingengine.model.TransactionType
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

class ApiAccount (
    val id: UUID,
    val beneficiaryName: String,
    val balance: BigDecimal,
    val currency: Currency
)

class ApiAccountCreateRequest (
    val beneficiaryName: String,

    @field: Pattern(regexp = "/d{4}", message = "Pin should be in 4 digit format")
    val pinNumber: String
)

class ApiTransactionFilter(
        val transactionId: UUID?,
        val accountId: UUID?,
        val transactionType: TransactionType?,
        val transactionStatus: TransactionStatus?) {
}

class ApiTransactionEntry(
        val id: UUID,
        val transactionType: TransactionType,
        val transactionStatus: TransactionStatus,
        val amount: BigDecimal,
        val currency: Currency,
        val createdDate: OffsetDateTime,
        val lastModifiedDate: OffsetDateTime
)

class ApiTransactionRequest(
        val sourceEndpointType: TransactionEndpointType,
        val sourceIdentifier : String?,
        val sourceAccountPin: String?,
        val destinationEndpointType: TransactionEndpointType,
        val destinationIdentifier : String?,
        val transactionType: TransactionType,
        val currency: Currency,
        val amount: BigDecimal,
        val comment: String?
)
