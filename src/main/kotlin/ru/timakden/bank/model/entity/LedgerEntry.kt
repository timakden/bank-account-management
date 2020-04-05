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
data class LedgerEntry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    val account: Account,

    @Column(name = "amount", nullable = false)
    val amount: BigDecimal,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "operation", nullable = false)
    val operation: BankOperation,

    @Column(name = "operation_time", nullable = false)
    val operationTime: Instant
)
