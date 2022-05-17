package ru.timakden.bank.handler

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList
import reactor.kotlin.core.publisher.toMono
import ru.timakden.bank.BaseTest
import ru.timakden.bank.model.dto.LedgerEntryDTO
import ru.timakden.bank.model.dto.request.CreateAccountRequest
import ru.timakden.bank.model.dto.request.CreateLedgerEntryRequest
import ru.timakden.bank.model.entity.Account
import ru.timakden.bank.model.entity.Client
import ru.timakden.bank.model.entity.LedgerEntry
import ru.timakden.bank.model.enums.BankOperation
import ru.timakden.bank.model.enums.Currency
import ru.timakden.bank.repository.AccountRepository
import ru.timakden.bank.repository.ClientRepository
import ru.timakden.bank.repository.LedgerRepository
import ru.timakden.bank.util.Constants.LEDGER_PATH
import ru.timakden.bank.util.DateTimeUtils.toUTCDateTime
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.Month

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 21.04.2020.
 */
@AutoConfigureWebTestClient
class LedgerHandlerTest : BaseTest() {
    @Autowired
    private lateinit var ledgerRepository: LedgerRepository

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var clientRepository: ClientRepository

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @AfterEach
    fun cleanUp() {
        ledgerRepository.deleteAllInBatch()
        accountRepository.deleteAllInBatch()
        clientRepository.deleteAllInBatch()
    }

    @Test
    fun `getAll() returns list of all ledger entries`() {
        val client = clientRepository.save(
            Client(
                fullName = "John Doe",
                birthDate = LocalDate.of(1974, Month.APRIL, 5),
                phoneNumber = "+35 (234) 567-89-00"
            )
        )

        val account = accountRepository.save(
            Account(
                number = "123-ABC",
                balance = BigDecimal(5000),
                currency = Currency.EUR,
                owner = client
            )
        )

        val ledgerEntry1 = LedgerEntry(
            account = account,
            amount = BigDecimal(100),
            operation = BankOperation.WITHDRAWAL,
            operationTime = Instant.now()
        )

        val ledgerEntry2 = LedgerEntry(
            account = account,
            amount = BigDecimal(100),
            operation = BankOperation.DEPOSIT,
            operationTime = Instant.now()
        )

        ledgerRepository.saveAll(listOf(ledgerEntry1, ledgerEntry2))

        webTestClient.get()
            .uri(LEDGER_PATH)
            .exchange()
            .expectStatus().isOk
            .expectBodyList<LedgerEntryDTO>()
            .hasSize(2)
            .value<WebTestClient.ListBodySpec<LedgerEntryDTO>> {
                with(it.first()) {
                    assertThat(accountId).isEqualTo(ledgerEntry1.account.id)
                    assertThat(amount).isEqualTo(ledgerEntry1.amount)
                    assertThat(operation).isEqualTo(ledgerEntry1.operation.name)
                }

                with(it.last()) {
                    assertThat(accountId).isEqualTo(ledgerEntry2.account.id)
                    assertThat(amount).isEqualTo(ledgerEntry2.amount)
                    assertThat(operation).isEqualTo(ledgerEntry2.operation.name)
                }
            }
    }

    @Test
    fun `getAll() returns empty list when there are no ledger entries`() {
        webTestClient.get()
            .uri(LEDGER_PATH)
            .exchange()
            .expectStatus().isOk
            .expectBodyList<LedgerEntryDTO>()
            .hasSize(0)
    }

    @Test
    fun `create() creates a new ledger entry`() {
        val client = clientRepository.save(
            Client(
                fullName = "John Doe",
                birthDate = LocalDate.of(1974, Month.APRIL, 5),
                phoneNumber = "+35 (234) 567-89-00"
            )
        )

        val account = accountRepository.save(
            Account(
                number = "123-ABC",
                balance = BigDecimal(5000),
                currency = Currency.EUR,
                owner = client
            )
        )

        val request = CreateLedgerEntryRequest(
            account.id,
            BigDecimal(100),
            BankOperation.DEPOSIT.name,
            Instant.now().toUTCDateTime()
        )

        webTestClient.post()
            .uri(LEDGER_PATH)
            .body(request.toMono(), CreateAccountRequest::class.java)
            .exchange()
            .expectStatus().isCreated

        val ledgerEntries = ledgerRepository.findAll()

        assertThat(ledgerEntries).hasSize(1)
        assertThat(ledgerEntries.first().operation.name).isEqualTo(request.operation)
        assertThat(ledgerEntries.first().account.id).isEqualTo(request.accountId)
        assertThat(ledgerEntries.first().amount).isEqualTo(request.amount)
    }

    @Test
    fun `create() throws a ValidationException when request is invalid`() {
        val request = CreateLedgerEntryRequest(
            42L,
            BigDecimal(100),
            BankOperation.DEPOSIT.name,
            Instant.now().toUTCDateTime()
        )

        webTestClient.post()
            .uri(LEDGER_PATH)
            .body(request.toMono(), CreateLedgerEntryRequest::class.java)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `compares two Instants`() {
        val instant1 = Instant.parse("2007-12-03T10:15:30.00Z").toUTCDateTime()
        val instant2 = Instant.parse("2007-12-03T10:15:30.00Z").toUTCDateTime()
        assertThat(instant1).isEqualTo(instant2)
    }
}
