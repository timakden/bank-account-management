package ru.timakden.bank.handler

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList
import reactor.kotlin.core.publisher.toMono
import ru.timakden.bank.BaseTest
import ru.timakden.bank.model.dto.AccountDTO
import ru.timakden.bank.model.dto.request.CreateAccountRequest
import ru.timakden.bank.model.entity.Account
import ru.timakden.bank.model.entity.Client
import ru.timakden.bank.model.enums.Currency
import ru.timakden.bank.repository.AccountRepository
import ru.timakden.bank.repository.ClientRepository
import ru.timakden.bank.util.Constants.ACCOUNTS_PATH
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 21.04.2020.
 */
@AutoConfigureWebTestClient
class AccountHandlerTest : BaseTest() {
    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var clientRepository: ClientRepository

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @AfterEach
    fun cleanUp() {
        accountRepository.deleteAllInBatch()
        clientRepository.deleteAllInBatch()
    }

    @Test
    fun `getAll() returns list of all accounts`() {
        val client = clientRepository.save(
            Client(
                fullName = "John Doe",
                birthDate = LocalDate.of(1974, Month.APRIL, 5),
                phoneNumber = "+35 (234) 567-89-00"
            )
        )

        val account1 = Account(
            number = "123-ABC",
            balance = BigDecimal(5000),
            currency = Currency.EUR,
            owner = client
        )

        val account2 = Account(
            number = "678-XYZ",
            balance = BigDecimal(1000.50),
            currency = Currency.USD,
            owner = client
        )

        accountRepository.saveAll(listOf(account1, account2))

        webTestClient.get()
            .uri(ACCOUNTS_PATH)
            .exchange()
            .expectStatus().isOk
            .expectBodyList<AccountDTO>()
            .hasSize(2)
            .value<WebTestClient.ListBodySpec<AccountDTO>> {
                with(it.first()) {
                    assertThat(number).isEqualTo(account1.number)
                    assertThat(balance).isEqualTo(account1.balance)
                    assertThat(currency).isEqualTo(account1.currency.name)
                    assertThat(ownerId).isEqualTo(account1.owner.id)
                }

                with(it.last()) {
                    assertThat(number).isEqualTo(account2.number)
                    assertThat(balance).isEqualTo(account2.balance)
                    assertThat(currency).isEqualTo(account2.currency.name)
                    assertThat(ownerId).isEqualTo(account2.owner.id)
                }
            }
    }

    @Test
    fun `getAll() returns empty list when there are no accounts`() {
        webTestClient.get()
            .uri(ACCOUNTS_PATH)
            .exchange()
            .expectStatus().isOk
            .expectBodyList<AccountDTO>()
            .hasSize(0)
    }

    @Test
    fun `create() creates a new account`() {
        val client = clientRepository.save(
            Client(
                fullName = "John Doe",
                birthDate = LocalDate.of(1974, Month.APRIL, 5),
                phoneNumber = "+35 (234) 567-89-00"
            )
        )

        val request = CreateAccountRequest("123-ABC", BigDecimal(5000), Currency.RUB.name, client.id)

        webTestClient.post()
            .uri(ACCOUNTS_PATH)
            .body(request.toMono(), CreateAccountRequest::class.java)
            .exchange()
            .expectStatus().isCreated

        val accounts = accountRepository.findAll()

        assertThat(accounts).hasSize(1)
        assertThat(accounts.first().number).isEqualTo(request.accountNumber)
        assertThat(accounts.first().balance).isEqualTo(request.balance)
        assertThat(accounts.first().currency.name).isEqualTo(request.currency)
        assertThat(accounts.first().owner.id).isEqualTo(request.clientId)
    }

    @Test
    fun `create() throws a ValidationException when request is invalid`() {
        val request = CreateAccountRequest("123-ABC", BigDecimal(5000), Currency.RUB.name, 42L)

        webTestClient.post()
            .uri(ACCOUNTS_PATH)
            .body(request.toMono(), CreateAccountRequest::class.java)
            .exchange()
            .expectStatus().isBadRequest
    }
}
