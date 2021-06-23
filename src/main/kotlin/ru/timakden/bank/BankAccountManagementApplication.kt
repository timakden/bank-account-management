package ru.timakden.bank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BankAccountManagementApplication

fun main(args: Array<String>) {
    runApplication<BankAccountManagementApplication>(*args)
}
