package com.github.abedurftig

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class ExternalServiceConsumerIntegrationTest {

    @Test
    fun `should successfully load data from external service`() {
        Thread.sleep(10000)
        Assertions.assertThat(ExternalServiceConsumer().getDataFromExternalService())
            .isEqualTo(SomeDataCollectedFromExternalService("I was loaded from an external service", 42))
    }
}
