package ru.timakden.bank.model.entity

import java.time.LocalDate
import javax.persistence.*

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 03.04.2020.
 */
@Entity
@Table(name = "client")
data class Client(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "full_name", nullable = false)
    val fullName: String,

    @Column(name = "birth_date", nullable = false)
    val birthDate: LocalDate,

    @Column(name = "phone_number", nullable = false)
    val phoneNumber: String,

    @OneToMany(
        cascade = [CascadeType.ALL],
        mappedBy = "owner",
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    val accounts: List<Account>
)
