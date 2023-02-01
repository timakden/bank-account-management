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
import ru.timakden.bank.model.dto.LedgerEntryDTO
import ru.timakden.bank.model.dto.converter.LedgerEntryDTOConverter
import ru.timakden.bank.model.dto.request.CreateLedgerEntryRequest
import ru.timakden.bank.model.entity.LedgerEntry
import ru.timakden.bank.model.enums.BankOperation
import ru.timakden.bank.model.enums.BankOperation.DEPOSIT
import ru.timakden.bank.repository.AccountRepository
import ru.timakden.bank.repository.LedgerRepository
import ru.timakden.bank.validator.LedgerValidator

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 06.04.2020.
 */
@Component
@Transactional
class LedgerHandler @Autowired constructor(
    private val repository: LedgerRepository,
    private val accountRepository: AccountRepository,
    private val ledgerValidator: LedgerValidator
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
            .flatMap {
                if (ledgerValidator.isRequestValid(it)) {
                    val account = accountRepository.getReferenceById(it.accountId)
                    val operation = BankOperation.valueOf(it.operation)
                    val ledgerEntry = LedgerEntry(
                        account = account,
                        operation = operation,
                        amount = it.amount,
                        operationTime = it.operationTime.toInstant()
                    )

                    val updatedAccount = when (operation) {
                        DEPOSIT -> account.copy(balance = account.balance + it.amount)
                        else -> account.copy(balance = account.balance - it.amount)
                    }
                    accountRepository.save(updatedAccount)

                    Mono.just(repository.save(ledgerEntry))
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
