package ru.timakden.bank.handler

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation.SUPPORTS
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import ru.timakden.bank.model.dto.AccountDTO
import ru.timakden.bank.model.dto.converter.AccountDTOConverter
import ru.timakden.bank.model.dto.request.CreateAccountRequest
import ru.timakden.bank.model.entity.Account
import ru.timakden.bank.model.enums.Currency
import ru.timakden.bank.repository.AccountRepository
import ru.timakden.bank.repository.ClientRepository

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 06.04.2020.
 */
@Component
@Transactional
class AccountHandler @Autowired constructor(
    private val repository: AccountRepository,
    private val clientRepository: ClientRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional(propagation = SUPPORTS, readOnly = true)
    fun getAll(request: ServerRequest): Mono<ServerResponse> {
        logger.info("Getting all accounts from DB")
        logger.debug("Request: $request")

        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(AccountDTOConverter.toDTOs(repository.findAll()), AccountDTO::class.java)
    }

    fun create(request: ServerRequest): Mono<ServerResponse> {
        logger.info("Creating an account")
        logger.debug("Request: $request")

        return request.bodyToMono(CreateAccountRequest::class.java)
            .flatMap { createRequest ->
                Mono.justOrEmpty(
                    clientRepository.findByIdOrNull(createRequest.clientId)?.let { client ->
                        val account = Account(
                            number = createRequest.accountNumber,
                            currency = Currency.valueOf(createRequest.currency),
                            balance = createRequest.balance,
                            owner = client
                        )

                        repository.save(account)
                    }
                )
            }
            .flatMap { ServerResponse.status(CREATED).build() }
            .switchIfEmpty(ServerResponse.status(INTERNAL_SERVER_ERROR).build())
    }
}
