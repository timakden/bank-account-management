package ru.timakden.bank.exception

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 20.04.2020.
 */
class ValidationException(override val message: String) : RuntimeException(message)
