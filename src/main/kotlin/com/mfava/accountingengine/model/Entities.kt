package com.mfava.accountingengine.model

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*

@Entity
@EntityListeners(value = [AuditingEntityListener::class])
data class Account(
        @Id
        val id: UUID = UUID.randomUUID(),

        var beneficiaryName: String,
        var pinNumber: String,

        var balance: BigDecimal,
        var currency: Currency,

        @Enumerated(EnumType.STRING)
        val accountType: AccountType = AccountType.CUSTOMER,

        @CreationTimestamp
        var createdDate: OffsetDateTime = OffsetDateTime.now(),

        @UpdateTimestamp
        var lastModifiedDate: OffsetDateTime = OffsetDateTime.now()
)

@Entity
@EntityListeners(value = [AuditingEntityListener::class])
data class TransactionEntry(

        @Id
        val id: UUID = UUID.randomUUID(),

        @Enumerated(EnumType.STRING)
        val transactionType: TransactionType? = null,

        @Enumerated(EnumType.STRING)
        var transactionStatus: TransactionStatus? = TransactionStatus.INITIATED,
        val amount: BigDecimal,
        val currency: Currency,

        @Enumerated(EnumType.STRING)
        val sourceType: TransactionEndpointType,
        val sourceIdentifier: String?,

        @Enumerated(EnumType.STRING)
        val destinationType: TransactionEndpointType,
        val destinationIdentifier: String?,
        var comment : String?,

        @CreationTimestamp
        var createdDate: OffsetDateTime = OffsetDateTime.now(),

        @UpdateTimestamp
        var lastModifiedDate: OffsetDateTime = OffsetDateTime.now()

) {


}
