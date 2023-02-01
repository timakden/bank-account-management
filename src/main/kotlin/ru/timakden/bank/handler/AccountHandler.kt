package ru.timakden.bank.handler

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation.SUPPORTS
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import ru.timakden.bank.exception.ValidationException
import ru.timakden.bank.model.dto.AccountDTO
import ru.timakden.bank.model.dto.converter.AccountDTOConverter
import ru.timakden.bank.model.dto.request.CreateAccountRequest
import ru.timakden.bank.model.entity.Account
import ru.timakden.bank.model.enums.Currency
import ru.timakden.bank.repository.AccountRepository
import ru.timakden.bank.repository.ClientRepository
import ru.timakden.bank.validator.AccountValidator

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 06.04.2020.
 */
@Component
@Transactional
class AccountHandler @Autowired constructor(
    private val repository: AccountRepository,
    private val clientRepository: ClientRepository,
    private val accountValidator: AccountValidator
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
            .flatMap {
                if (accountValidator.isRequestValid(it)) {
                    val client = clientRepository.getReferenceById(it.clientId)
                    val account = Account(
                        number = it.accountNumber,
                        balance = it.balance,
                        currency = Currency.valueOf(it.currency),
                        owner = client
                    )
                    Mono.just(repository.save(account))
                } else {
                    Mono.error<ValidationException>(ValidationException("Failed to validate request $it"))
                }
            }
            .flatMap { ServerResponse.status(CREATED).build() }
            .onErrorResume {
                when (it) {
                    is ValidationException -> ServerResponse.badRequest().body(it.message.toMono(), String::class.java)
                    else -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(it.toMono(), Throwable::class.java)
                }
            }
    }
}
