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
import ru.timakden.bank.model.dto.ClientDTO
import ru.timakden.bank.model.dto.request.CreateClientRequest
import ru.timakden.bank.model.entity.Client
import ru.timakden.bank.repository.ClientRepository
import ru.timakden.bank.util.Constants.CLIENTS_PATH
import java.time.LocalDate
import java.time.Month

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 20.04.2020.
 */
@AutoConfigureWebTestClient
class ClientHandlerTest : BaseTest() {
    @Autowired
    private lateinit var clientRepository: ClientRepository

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @AfterEach
    fun cleanUp() {
        clientRepository.deleteAllInBatch()
    }

    @Test
    fun `getAll() returns list of all clients`() {
        val client1 = Client(
            fullName = "John Doe",
            birthDate = LocalDate.of(1974, Month.APRIL, 5),
            phoneNumber = "+35 (234) 567-89-00"
        )

        val client2 = Client(
            fullName = "Mary Doe",
            birthDate = LocalDate.of(1980, Month.DECEMBER, 29),
            phoneNumber = "+1 (764) 400-86-45"
        )

        clientRepository.saveAll(listOf(client1, client2))

        webTestClient.get()
            .uri(CLIENTS_PATH)
            .exchange()
            .expectStatus().isOk
            .expectBodyList<ClientDTO>()
            .hasSize(2)
            .value<WebTestClient.ListBodySpec<ClientDTO>> {
                with(it.first()) {
                    assertThat(fullName).isEqualTo(client1.fullName)
                    assertThat(birthDate).isEqualTo(client1.birthDate)
                    assertThat(phoneNumber).isEqualTo(client1.phoneNumber)
                }

                with(it.last()) {
                    assertThat(fullName).isEqualTo(client2.fullName)
                    assertThat(birthDate).isEqualTo(client2.birthDate)
                    assertThat(phoneNumber).isEqualTo(client2.phoneNumber)
                }
            }
    }

    @Test
    fun `getAll() returns empty list when there are no clients`() {
        webTestClient.get()
            .uri(CLIENTS_PATH)
            .exchange()
            .expectStatus().isOk
            .expectBodyList<ClientDTO>()
            .hasSize(0)
    }

    @Test
    fun `create() creates a new client`() {
        val request = CreateClientRequest("John Doe", LocalDate.of(1974, Month.APRIL, 5), "+35 (234) 567-89-00")

        webTestClient.post()
            .uri(CLIENTS_PATH)
            .body(request.toMono(), CreateClientRequest::class.java)
            .exchange()
            .expectStatus().isCreated

        val clients = clientRepository.findAll()

        assertThat(clients).hasSize(1)
        assertThat(clients.first().fullName).isEqualTo(request.fullName)
        assertThat(clients.first().birthDate).isEqualTo(request.birthDate)
        assertThat(clients.first().phoneNumber).isEqualTo(request.phoneNumber)
    }

    @Test
    fun `create() throws a ValidationException when request is invalid`() {
        val request = CreateClientRequest("", LocalDate.of(1974, Month.APRIL, 5), "+35 (234) 567-89-00")

        webTestClient.post()
            .uri(CLIENTS_PATH)
            .body(request.toMono(), CreateClientRequest::class.java)
            .exchange()
            .expectStatus().isBadRequest

        val clients = clientRepository.findAll()

        assertThat(clients).isEmpty()
    }
}
