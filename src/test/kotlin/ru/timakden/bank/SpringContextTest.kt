package ru.timakden.bank

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

class SpringContextTest : BaseIntegrationTest() {
    @Autowired
    private lateinit var context: ApplicationContext

    @Test
    fun contextLoads() {
        assertThat(context).isNotNull
    }

}
