package ru.timakden.bank.model.dto.converter

import ru.timakden.bank.model.dto.AccountDTO
import ru.timakden.bank.model.entity.Account

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 07.04.2020.
 */
object AccountDTOConverter : EntityDTOConverter<Account, AccountDTO> {
    override fun toDTO(entity: Account) = with(entity) {
        AccountDTO(id, number, balance, currency.name, owner.id, owner.fullName)
    }
}
