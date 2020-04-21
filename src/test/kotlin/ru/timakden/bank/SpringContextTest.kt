package ru.timakden.bank

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

/**
 * @author Denis Timakov (timakden88@gmail.com)
 * Created on 04.04.2020.
 */
class SpringContextTest : BaseTest() {
    @Autowired
    private lateinit var context: ApplicationContext

    @Test
    fun contextLoads() {
        assertThat(context).isNotNull
    }
}
