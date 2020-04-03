package ru.timakden.bank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class BankAccountManagementApplication

fun main(args: Array<String>) {
    runApplication<BankAccountManagementApplication>(*args)
}
