package com.github.abedurftig

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class ExternalServiceConsumerIntegrationTest {

    @Test
    fun `should successfully load data from external service`() {
        Assertions.assertThat(ExternalServiceConsumer().getDataFromExternalService())
            .isEqualTo(ExternalServiceData("I was loaded from an external service", 42))
    }
}
