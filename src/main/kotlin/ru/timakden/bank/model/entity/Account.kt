package ru.timakden.bank.model.entity

import jakarta.persistence.*
import ru.timakden.bank.model.enums.Currency
import java.math.BigDecimal

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 03.04.2020.
 */
@Entity
@Table(name = "account")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "account_number", nullable = false)
    val number: String,

    @Column(name = "balance")
    val balance: BigDecimal,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "currency")
    val currency: Currency,

    @ManyToOne
    @JoinColumn(name = "client_id")
    val owner: Client,

    @OneToMany(
        cascade = [CascadeType.ALL],
        mappedBy = "account",
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    val ledgerEntries: MutableList<LedgerEntry> = mutableListOf()
)
