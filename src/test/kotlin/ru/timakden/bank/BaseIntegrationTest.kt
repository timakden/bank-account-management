package ru.timakden.bank

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import ru.timakden.bank.util.BankPostgreSQLContainer

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 04.04.2020.
 */
@Testcontainers
@ExtendWith(SpringExtension::class)
@SpringBootTest
abstract class BaseIntegrationTest {
    @Container
    private val postgreSQLContainer: PostgreSQLContainer<BankPostgreSQLContainer> = BankPostgreSQLContainer
}
