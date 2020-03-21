package com.mfava.accountingengine.model

import java.util.*

enum class TransactionType{
    DEPOSIT,
    WITHDRAW,
    TRANSFER
}

enum class TransactionStatus{
    INITIATED,
    PENDING,
    COMPLETED,
    FAILED,
    CANCELLED
}


enum class TransactionEndpointType{
    ACCOUNT,
    CARD,
    BANK
}

enum class AccountType(val accountId: String? = null){
    CUSTOMER,
    SYSTEM_DEPOSIT("aed7b6bb-ae40-4be5-b621-1c6b4f8c3e9e"),
    SYSTEM_WITHDRAW("67db6240-3557-4629-83b1-4f1cd9708615");


}

enum class AccountBalanceOperation{
    ADD,
    SUBTRACT
}
