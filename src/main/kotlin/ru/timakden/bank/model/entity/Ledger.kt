package ru.timakden.bank.model.entity

import ru.timakden.bank.model.enums.BankOperation
import java.math.BigDecimal
import java.time.Instant
import javax.persistence.*

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 03.04.2020.
 */
@Entity
@Table(name = "ledger")
data class Ledger(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @ManyToOne
    @JoinColumn(name = "account_id")
    val account: Account,

    @Column(name = "amount")
    val amount: BigDecimal,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "operation")
    val operation: BankOperation,

    @Column(name = "operation_time")
    val operationTime: Instant
)
