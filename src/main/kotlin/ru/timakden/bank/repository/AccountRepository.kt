package ru.timakden.bank.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.timakden.bank.model.entity.Account

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 05.04.2020.
 */
@Repository
interface AccountRepository : JpaRepository<Account, Long>
