package ru.timakden.bank.model.dto.request

import ru.timakden.bank.model.entity.Client
import java.time.LocalDate

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 07.04.2020.
 */
data class CreateClientRequest(
    val fullName: String,
    val birthDate: LocalDate,
    val phoneNumber: String
) {
    fun toEntity(): Client = Client(fullName = fullName, birthDate = birthDate, phoneNumber = phoneNumber)

    fun isValid(): Boolean = fullName.isNotBlank() && phoneNumber.isNotBlank() && birthDate.isBefore(LocalDate.now())
}
