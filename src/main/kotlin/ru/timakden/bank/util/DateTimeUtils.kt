package ru.timakden.bank.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 07.04.2020.
 */
object DateTimeUtils {
    fun Instant.toZonedDateTime(zoneId: ZoneId): ZonedDateTime = ZonedDateTime.ofInstant(this, zoneId)
    fun Instant.toUTCDateTime(): ZonedDateTime = this.toZonedDateTime(ZoneOffset.UTC)
}
