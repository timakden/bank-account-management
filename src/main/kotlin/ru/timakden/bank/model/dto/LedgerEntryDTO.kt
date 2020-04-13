package ru.timakden.bank.model.dto

import java.math.BigDecimal
import java.time.ZonedDateTime

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 07.04.2020.
 */
data class LedgerEntryDTO(
    val id: Long,
    val accountId: Long,
    val accountNumber: String,
    val amount: BigDecimal,
    val operation: String,
    val operationTime: ZonedDateTime
)
