package ru.timakden.bank.model.dto

import java.time.LocalDate

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 07.04.2020.
 */
data class ClientDTO(
    val id: Long,
    val fullName: String,
    val birthDate: LocalDate,
    val phoneNumber: String
)
