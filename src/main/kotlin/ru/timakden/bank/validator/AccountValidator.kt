package ru.timakden.bank.validator

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.timakden.bank.model.dto.request.CreateAccountRequest
import ru.timakden.bank.model.enums.Currency
import ru.timakden.bank.repository.ClientRepository
import java.math.BigDecimal

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 20.04.2020.
 */
@Component
class AccountValidator @Autowired constructor(private val clientRepository: ClientRepository) {
    fun isRequestValid(request: CreateAccountRequest): Boolean {
        val clientExists = clientRepository.existsById(request.clientId)
        val currencyExists = try {
            Currency.valueOf(request.currency)
            true
        } catch (e: Exception) {
            false
        }

        return clientExists && request.accountNumber.isNotBlank() && currencyExists && request.balance > BigDecimal.ZERO
    }
}
