package ru.timakden.bank.model.dto.request

import java.math.BigDecimal
import java.time.ZonedDateTime

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 13.04.2020.
 */
data class CreateLedgerEntryRequest(
    val accountId: Long,
    val amount: BigDecimal,
    val operation: String,
    val operationTime: ZonedDateTime
)
