package ru.timakden.bank.handler

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation.SUPPORTS
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import ru.timakden.bank.exception.ValidationException
import ru.timakden.bank.model.dto.ClientDTO
import ru.timakden.bank.model.dto.converter.ClientDTOConverter
import ru.timakden.bank.model.dto.request.CreateClientRequest
import ru.timakden.bank.repository.ClientRepository

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 06.04.2020.
 */
@Component
@Transactional
class ClientHandler @Autowired constructor(private val repository: ClientRepository) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional(propagation = SUPPORTS, readOnly = true)
    fun getAll(request: ServerRequest): Mono<ServerResponse> {
        logger.info("Getting all clients from DB")
        logger.debug("Request: $request")

        return ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(ClientDTOConverter.toDTOs(repository.findAll()), ClientDTO::class.java)
    }

    fun create(request: ServerRequest): Mono<ServerResponse> {
        logger.info("Creating a client")
        logger.debug("Request: $request")

        return request.bodyToMono(CreateClientRequest::class.java)
            .flatMap {
                if (it.isValid()) {
                    Mono.just(repository.save(it.toEntity()))
                } else {
                    Mono.error<ValidationException>(ValidationException("Failed to validate request $it"))
                }
            }
            .flatMap { ServerResponse.status(HttpStatus.CREATED).build() }
            .doOnError {
                val status = when (it) {
                    is ValidationException -> BAD_REQUEST
                    else -> INTERNAL_SERVER_ERROR
                }

                ServerResponse.status(status).body(it.toMono(), Throwable::class.java)
            }
    }
}
