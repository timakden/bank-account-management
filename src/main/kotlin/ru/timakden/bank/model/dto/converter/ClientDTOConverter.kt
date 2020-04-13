package ru.timakden.bank.model.dto.converter

import ru.timakden.bank.model.dto.ClientDTO
import ru.timakden.bank.model.entity.Client

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 07.04.2020.
 */
object ClientDTOConverter : EntityDTOConverter<Client, ClientDTO> {
    override fun toDTO(entity: Client) = with(entity) {
        ClientDTO(id, fullName, birthDate, phoneNumber)
    }
}
