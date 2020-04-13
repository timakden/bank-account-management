package ru.timakden.bank.handler

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation.SUPPORTS
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import ru.timakden.bank.model.dto.ClientDTO
import ru.timakden.bank.model.dto.converter.ClientDTOConverter
import ru.timakden.bank.model.dto.request.CreateClientRequest
import ru.timakden.bank.model.entity.Client
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
                val client = Client(fullName = it.fullName, birthDate = it.birthDate, phoneNumber = it.phoneNumber)
                Mono.just(repository.save(client))
            }
            .flatMap { ServerResponse.status(CREATED).build() }
            .switchIfEmpty(ServerResponse.status(INTERNAL_SERVER_ERROR).build())
    }
}
