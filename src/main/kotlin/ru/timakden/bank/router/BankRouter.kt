package ru.timakden.bank.router

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.router
import ru.timakden.bank.handler.AccountHandler
import ru.timakden.bank.handler.ClientHandler
import ru.timakden.bank.handler.LedgerHandler
import ru.timakden.bank.util.Constants.CONTEXT_PATH


/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 05.04.2020.
 */
@Configuration
class BankRouter @Autowired constructor(
    private val clientHandler: ClientHandler,
    private val accountHandler: AccountHandler,
    private val ledgerHandler: LedgerHandler
) {
    @Bean
    fun clientsRoute() = router {
        (accept(APPLICATION_JSON) and CONTEXT_PATH).nest {
            GET("/clients", clientHandler::getAll)
            POST("/clients", clientHandler::create)
        }
    }

    @Bean
    fun getAllAccountsRoute() = router {
        (accept(APPLICATION_JSON) and CONTEXT_PATH).nest {
            GET("/accounts", accountHandler::getAll)
            POST("/accounts", accountHandler::create)
        }
    }

    @Bean
    fun getAllLedgerEntriesRoute() = router {
        (accept(APPLICATION_JSON) and CONTEXT_PATH).nest {
            GET("/ledger", ledgerHandler::getAll)
            POST("/ledger", ledgerHandler::create)
        }
    }
}
