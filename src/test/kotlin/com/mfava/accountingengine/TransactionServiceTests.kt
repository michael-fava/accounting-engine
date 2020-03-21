package com.mfava.accountingengine

import com.mfava.accountingengine.filters.TransactionFilter
import com.mfava.accountingengine.model.*
import com.mfava.accountingengine.service.AccountsService
import com.mfava.accountingengine.service.TransactionService
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import java.lang.reflect.UndeclaredThrowableException
import java.math.BigDecimal
import java.util.*


@ActiveProfiles(value = ["test"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = ["app.scheduling.enable=false"])
class TransactionServiceTests() {

    @Autowired
    lateinit var transactionService: TransactionService

    @Autowired
    lateinit var accountsService: AccountsService

    @Autowired
    lateinit var flyway: Flyway

    lateinit var test_account1 : Account

    lateinit var test_account2 : Account

    @BeforeEach
    fun clearDatabase() {
        flyway.clean()
        flyway.migrate()
        test_account1 = accountsService.createAccount(AccountRequest("TestAccount1", "1111"))
        test_account2 = accountsService.createAccount(AccountRequest("TestAccount2", "2222"))
    }


    @Test
    fun createDepositTransactionSuccessTest() {
        val transaction = transactionService.createTransaction(TransactionRequest(TransactionEndpointType.CARD,
                                  "4444333322221111",
                                 null,
                                                TransactionEndpointType.ACCOUNT,
                                                test_account1.id.toString(),
                                                TransactionType.DEPOSIT,
                                                test_account1.currency,
                                                BigDecimal.TEN,
                                      "Test Deposit from Card"))

        assertEquals(TransactionStatus.INITIATED, transaction.transactionStatus)
    }

    @Test
    fun executeDepositTransactionSuccessTest(){
        val transaction = transactionService.createTransaction(TransactionRequest(TransactionEndpointType.CARD,
                "4444333322221111",
                null,
                TransactionEndpointType.ACCOUNT,
                test_account1.id.toString(),
                TransactionType.DEPOSIT,
                test_account1.currency,
                BigDecimal.TEN,
                "Test Deposit from Card"))

        assertEquals(TransactionStatus.INITIATED, transaction.transactionStatus)

        transactionService.executeTransaction(transaction)

        assertEquals(TransactionStatus.COMPLETED,transactionService.getTransactionsByFilter(TransactionFilter(transaction.id, null, null, null), Pageable.unpaged()).get().findFirst().get().transactionStatus)
        assertEquals(10.00, accountsService.getAccount(test_account1.id).balance.toDouble())
    }

    @Test
    fun executeInterAccountTransferSuccessTest(){
        loadTestAccount1_100Euro()

        val transaction = transactionService.createTransaction(TransactionRequest(TransactionEndpointType.ACCOUNT,
                test_account1.id.toString(),
                "1111",
                TransactionEndpointType.ACCOUNT,
                test_account2.id.toString(),
                TransactionType.TRANSFER,
                test_account1.currency,
                BigDecimal.TEN,
                "Test inter account transfer"))

        assertEquals(TransactionStatus.INITIATED, transaction.transactionStatus)

        transactionService.executeTransaction(transaction)

        assertEquals(TransactionStatus.COMPLETED,transactionService.getTransactionsByFilter(TransactionFilter(transaction.id, null, null, null), Pageable.unpaged()).get().findFirst().get().transactionStatus)
        assertEquals(10.00, accountsService.getAccount(test_account2.id).balance.toDouble())
        assertEquals(90.00, accountsService.getAccount(test_account1.id).balance.toDouble())

    }


    @Test
    fun executeAccountWithdrawSuccessTest(){
        loadTestAccount1_100Euro()

        val transaction = transactionService.createTransaction(TransactionRequest(TransactionEndpointType.ACCOUNT,
                test_account1.id.toString(),
                "1111",
                TransactionEndpointType.BANK,
                "TESTIBAN000012345655",
                TransactionType.WITHDRAW,
                test_account1.currency,
                BigDecimal.TEN,
                "Test Withdraw from Account"))

        assertEquals(TransactionStatus.INITIATED, transaction.transactionStatus)

        transactionService.executeTransaction(transaction)

        assertEquals(TransactionStatus.COMPLETED,transactionService.getTransactionsByFilter(TransactionFilter(transaction.id, null, null, null), Pageable.unpaged()).get().findFirst().get().transactionStatus)
        assertEquals(90.00, accountsService.getAccount(test_account1.id).balance.toDouble())
        assertEquals(10.00, accountsService.getAccount(UUID.fromString(AccountType.SYSTEM_WITHDRAW.accountId)).balance.toDouble())

    }


    @Test
    fun cancelTransactionSuccessTest(){
        val transaction = transactionService.createTransaction(TransactionRequest(TransactionEndpointType.ACCOUNT,
                test_account1.id.toString(),
                "1111",
                TransactionEndpointType.BANK,
                "TESTIBAN000012345655",
                TransactionType.WITHDRAW,
                test_account1.currency,
                BigDecimal.TEN,
                "Test Withdraw from Account"))

        assertEquals(TransactionStatus.INITIATED, transaction.transactionStatus)

        transactionService.cancelTransaction(transaction.id)

        assertEquals(TransactionStatus.CANCELLED, transactionService.getTransactionsByFilter(TransactionFilter(transaction.id, null, null, null), Pageable.unpaged()).get().findFirst().get().transactionStatus)
    }

    @Test
    fun getTransactionSuccessTest(){
        loadTestAccount1_100Euro()
        loadTestAccount1_100Euro()

        assertEquals(2, transactionService.getTransactionsByFilter(TransactionFilter(null, test_account1.id, null, null), Pageable.unpaged()).numberOfElements)

    }

    fun loadTestAccount1_100Euro(){
        val transaction = transactionService.createTransaction(TransactionRequest(TransactionEndpointType.CARD,
                "4444333322221111",
                null,
                TransactionEndpointType.ACCOUNT,
                test_account1.id.toString(),
                TransactionType.DEPOSIT,
                test_account1.currency,
                BigDecimal(100),
                "Deposit 100 from Card loadTestAccount1_100Euro"))

        transactionService.executeTransaction(transaction)
    }
}
