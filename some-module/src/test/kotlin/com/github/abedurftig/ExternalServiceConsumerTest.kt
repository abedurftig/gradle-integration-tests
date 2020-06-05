package com.github.abedurftig

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class ExternalServiceConsumerTest {

    @Test
    fun `should return the correct message`() {
        Assertions.assertThat(ExternalServiceConsumer().getMessage())
            .isEqualTo("Hello World, I am consuming a service!")
//        Assertions.fail<Any>("Just because")
    }
}
