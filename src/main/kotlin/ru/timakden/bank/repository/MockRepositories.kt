package ru.timakden.bank.repository

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.timakden.bank.model.entity.Account
import ru.timakden.bank.model.entity.Client
import ru.timakden.bank.model.entity.LedgerEntry
import ru.timakden.bank.model.enums.BankOperation.DEPOSIT
import ru.timakden.bank.model.enums.BankOperation.WITHDRAWAL
import ru.timakden.bank.model.enums.Currency.*
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.Month

@Component
class MockClientRepository : ClientRepository {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun findAll(): List<Client> {
        logger.info("Mocking DB response, returning hard-coded list of clients")

        return listOf(
            Client(
                1,
                "Lemmy Kilmister",
                LocalDate.of(1945, Month.DECEMBER, 24),
                "+1 (234) 567-89-00",
                emptyList()
            ),
            Client(
                2,
                "Till Lindemann",
                LocalDate.of(1963, Month.JANUARY, 4),
                "+3 (754) 084-46-78",
                emptyList()
            ),
            Client(
                3,
                "Ozzy Osbourne",
                LocalDate.of(1948, Month.DECEMBER, 3),
                "+56 (787) 235-06-44",
                emptyList()
            )
        )
    }
}

@Component
class MockAccountRepository : AccountRepository {
    @Autowired
    private lateinit var clientRepository: ClientRepository

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun findAll(): List<Account> {
        logger.info("Mocking DB response, returning hard-coded list of accounts")

        val clients = clientRepository.findAll()

        return listOf(
            Account(1, "1234", BigDecimal.valueOf(123446.879), USD, clients[0], emptyList()),
            Account(2, "65687", BigDecimal.valueOf(7654679.6), EUR, clients[1], emptyList()),
            Account(3, "AGYD679", BigDecimal.valueOf(568.32), RUB, clients[2], emptyList())
        )
    }
}

@Component
class MockLedgerRepository : LedgerRepository {
    @Autowired
    private lateinit var accountRepository: AccountRepository

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun findAll(): List<LedgerEntry> {
        logger.info("Mocking DB response, returning hard-coded list of ledger entries")

        val accounts = accountRepository.findAll()

        return listOf(
            LedgerEntry(1, accounts[0], BigDecimal.valueOf(100), WITHDRAWAL, Instant.now()),
            LedgerEntry(2, accounts[1], BigDecimal.valueOf(6586.78), WITHDRAWAL, Instant.now()),
            LedgerEntry(3, accounts[2], BigDecimal.valueOf(5879.98), DEPOSIT, Instant.now())
        )
    }
}
