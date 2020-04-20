package ru.timakden.bank.validator

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.timakden.bank.model.dto.request.CreateLedgerEntryRequest
import ru.timakden.bank.model.enums.BankOperation
import ru.timakden.bank.repository.AccountRepository
import java.math.BigDecimal

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 20.04.2020.
 */
@Component
class LedgerValidator @Autowired constructor(private val accountRepository: AccountRepository) {
    fun isRequestValid(request: CreateLedgerEntryRequest): Boolean {
        val accountExists = accountRepository.existsById(request.accountId)
        val operationExists = try {
            BankOperation.valueOf(request.operation)
            true
        } catch (e: Exception) {
            false
        }

        return accountExists && operationExists && request.amount > BigDecimal.ZERO
    }
}
