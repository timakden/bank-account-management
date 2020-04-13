package ru.timakden.bank.model.dto

import java.math.BigDecimal

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 07.04.2020.
 */
data class AccountDTO(
    val id: Long,
    val number: String,
    val balance: BigDecimal,
    val currency: String,
    val ownerId: Long,
    val ownerName: String
)
