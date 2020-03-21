package com.mfava.accountingengine.model

import java.math.BigDecimal
import java.util.*

class AccountRequest(
        val beneficiaryName : String,
        val pinNumber : String) {
}

class TransactionRequest(
        val sourceEndpointType: TransactionEndpointType,
        val sourceIdentifier : String?,
        val sourceAccountPin: String?,
        val destinationEndpointType: TransactionEndpointType,
        val destinationIdentifier : String?,
        val transactionType: TransactionType,
        val currency: Currency,
        val amount: BigDecimal,
        val comment: String?
){

}
