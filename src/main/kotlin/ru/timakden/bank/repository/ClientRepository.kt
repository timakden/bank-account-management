package ru.timakden.bank.repository

import ru.timakden.bank.model.entity.Client

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 05.04.2020.
 */
interface ClientRepository {
    fun findAll(): List<Client>
}
