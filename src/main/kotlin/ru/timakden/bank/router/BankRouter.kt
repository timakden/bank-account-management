package ru.timakden.bank.router

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Flux
import ru.timakden.bank.model.entity.Account
import ru.timakden.bank.model.entity.Client
import ru.timakden.bank.model.entity.LedgerEntry
import ru.timakden.bank.repository.AccountRepository
import ru.timakden.bank.repository.ClientRepository
import ru.timakden.bank.repository.LedgerRepository
import ru.timakden.bank.util.Constants.CONTEXT_PATH


/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 05.04.2020.
 */
@Configuration
class BankRouter @Autowired constructor(
    private val clientRepository: ClientRepository,
    private val accountRepository: AccountRepository,
    private val ledgerRepository: LedgerRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun getAllClientsRoute() = router {
        GET("$CONTEXT_PATH/clients") {
            logger.info("Getting all clients from DB")

            ServerResponse.ok().body(Flux.fromIterable(clientRepository.findAll()), Client::class.java)
        }
    }

    @Bean
    fun getAllAccountsRoute() = router {
        GET("$CONTEXT_PATH/accounts") {
            logger.info("Getting all accounts from DB")

            ServerResponse.ok().body(Flux.fromIterable(accountRepository.findAll()), Account::class.java)
        }
    }

    @Bean
    fun getAllLedgerEntriesRoute() = router {
        GET("$CONTEXT_PATH/ledger") {
            logger.info("Getting all ledger entries from DB")

            ServerResponse.ok().body(Flux.fromIterable(ledgerRepository.findAll()), LedgerEntry::class.java)
        }
    }
}
