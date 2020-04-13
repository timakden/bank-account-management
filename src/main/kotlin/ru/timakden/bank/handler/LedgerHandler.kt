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
import ru.timakden.bank.model.dto.LedgerEntryDTO
import ru.timakden.bank.model.dto.converter.LedgerEntryDTOConverter
import ru.timakden.bank.model.dto.request.CreateLedgerEntryRequest
import ru.timakden.bank.model.entity.LedgerEntry
import ru.timakden.bank.model.enums.BankOperation
import ru.timakden.bank.repository.AccountRepository
import ru.timakden.bank.repository.LedgerRepository

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 06.04.2020.
 */
@Component
@Transactional
class LedgerHandler @Autowired constructor(
    private val repository: LedgerRepository,
    private val accountRepository: AccountRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional(propagation = SUPPORTS, readOnly = true)
    fun getAll(request: ServerRequest): Mono<ServerResponse> {
        logger.info("Getting all ledger entries from DB")
        logger.debug("Request: $request")

        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(LedgerEntryDTOConverter.toDTOs(repository.findAll()), LedgerEntryDTO::class.java)
    }

    fun create(request: ServerRequest): Mono<ServerResponse> {
        logger.info("Creating a ledger entry")
        logger.debug("Request: $request")

        return request.bodyToMono(CreateLedgerEntryRequest::class.java)
            .flatMap { createRequest ->
                Mono.justOrEmpty(
                    accountRepository.findByIdOrNull(createRequest.accountId)?.let { account ->
                        val ledgerEntry = LedgerEntry(
                            account = account,
                            amount = createRequest.amount,
                            operation = BankOperation.valueOf(createRequest.operation),
                            operationTime = createRequest.operationTime.toInstant()
                        )

                        repository.save(ledgerEntry)
                    }
                )
            }
            .flatMap { ServerResponse.status(CREATED).build() }
            .switchIfEmpty(ServerResponse.status(INTERNAL_SERVER_ERROR).build())
    }
}
