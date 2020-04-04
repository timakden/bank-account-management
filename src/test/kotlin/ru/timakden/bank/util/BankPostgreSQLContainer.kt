package ru.timakden.bank.util

import org.testcontainers.containers.PostgreSQLContainer

/**
 * Custom [PostgreSQLContainer] that sets the following system properties after start:
 *
 * * `BANK_DB_URL`
 * * `BANK_DB_USER`
 * * `BANK_DB_PASSWORD`
 * * `BANK_LIQUIBASE_USER`
 * * `BANK_LIQUIBASE_PASSWORD`
 *
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 04.04.2020.
 */
object BankPostgreSQLContainer : PostgreSQLContainer<BankPostgreSQLContainer>("postgres:latest") {
    init {
        start()
    }

    override fun start() {
        super.start()

        System.setProperty("BANK_DB_URL", BankPostgreSQLContainer.jdbcUrl)
        System.setProperty("BANK_DB_USER", BankPostgreSQLContainer.username)
        System.setProperty("BANK_DB_PASSWORD", BankPostgreSQLContainer.password)
        System.setProperty("BANK_LIQUIBASE_USER", BankPostgreSQLContainer.username)
        System.setProperty("BANK_LIQUIBASE_PASSWORD", BankPostgreSQLContainer.password)
    }

    override fun stop() {
        // do nothing, JVM handles shut down
    }
}
