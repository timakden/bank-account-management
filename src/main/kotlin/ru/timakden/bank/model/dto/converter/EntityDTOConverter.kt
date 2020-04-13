package ru.timakden.bank.model.dto.converter

import reactor.core.publisher.Flux

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 07.04.2020.
 */
interface EntityDTOConverter<T, D> {
    fun toDTO(entity: T): D

    fun toDTOs(entities: Collection<T>) = Flux.fromIterable(entities.map { toDTO(it) })
}
