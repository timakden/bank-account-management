package ru.timakden.bank.repository

import ru.timakden.bank.model.entity.LedgerEntry

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 05.04.2020.
 */
interface LedgerRepository {
    fun findAll(): List<LedgerEntry>
}
