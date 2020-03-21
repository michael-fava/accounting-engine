package com.mfava.accountingengine

import com.mfava.accountingengine.model.Account
import com.mfava.accountingengine.model.AccountBalanceOperation
import com.mfava.accountingengine.model.AccountRequest
import com.mfava.accountingengine.service.AccountsService
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
import java.lang.reflect.UndeclaredThrowableException
import java.math.BigDecimal
import java.util.*


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountServiceTests() {

    @Autowired
    lateinit var accountsService: AccountsService

    @Autowired
    lateinit var flyway: Flyway

    @BeforeEach
    fun clearDatabase() {
        flyway.clean()
        flyway.migrate()
    }

    @Test
    fun createAccountSuccessTest() {
        val account = accountsService.createAccount(AccountRequest("TestAccountBeneficiary", "1111"))
        assertEquals("TestAccountBeneficiary", account.beneficiaryName)
        assertEquals("1111", account.pinNumber)
    }

    @Test
    fun createAccountFailTest() {
        assertThrows<DataIntegrityViolationException> { val account = accountsService.createAccount(AccountRequest("TestAccountBeneficiary", "11111")) }
    }

    @Test
    fun getAllAccountEmptyList() {
        assertEquals(0, accountsService.getAllAccount().size)
    }

    @Test
    fun getAllAccountList() {
        val account = accountsService.createAccount(AccountRequest("TestAccountBeneficiary", "1111"))
        assertNotEquals(0, accountsService.getAllAccount().size)
    }

    @Test
    fun updateAccountBalanceSuccessTest() {
        val account = accountsService.createAccount(AccountRequest("TestAccountBeneficiary", "1111"))
        val updatedAccount = accountsService.updateAccountBalance(account, BigDecimal.TEN, AccountBalanceOperation.ADD)
        assertEquals(BigDecimal.TEN, updatedAccount.balance)
    }

    @Test
    fun updateAccountBalanceFailTest() {
        val account = Account(UUID.randomUUID(), "TestUnsavedAccount", "1111", BigDecimal.ZERO, Currency.getInstance("EUR"))
        assertThrows<UndeclaredThrowableException> { accountsService.updateAccountBalance(account, BigDecimal.TEN, AccountBalanceOperation.ADD) }
    }

}
