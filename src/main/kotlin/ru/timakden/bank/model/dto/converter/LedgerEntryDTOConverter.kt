package ru.timakden.bank.model.dto.converter

import ru.timakden.bank.model.dto.LedgerEntryDTO
import ru.timakden.bank.model.entity.LedgerEntry
import ru.timakden.bank.util.DateTimeUtils.toUTCDateTime

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 07.04.2020.
 */
object LedgerEntryDTOConverter : EntityDTOConverter<LedgerEntry, LedgerEntryDTO> {
    override fun toDTO(entity: LedgerEntry) = with(entity) {
        LedgerEntryDTO(id, account.id, account.number, amount, operation.name, operationTime.toUTCDateTime())
    }
}
