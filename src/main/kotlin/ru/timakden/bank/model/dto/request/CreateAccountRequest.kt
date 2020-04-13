package ru.timakden.bank.model.dto.request

import java.math.BigDecimal

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 13.04.2020.
 */
data class CreateAccountRequest(
    val accountNumber: String,
    val balance: BigDecimal,
    val currency: String,
    val clientId: Long
)
