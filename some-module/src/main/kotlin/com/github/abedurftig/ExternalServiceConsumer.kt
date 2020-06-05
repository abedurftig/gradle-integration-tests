package com.github.abedurftig

class ExternalServiceConsumer {

    companion object {
        private const val MAGIC_NUMBER = 42
    }

    fun getDataFromExternalService() =
        SomeDataCollectedFromExternalService("I was loaded from an external service", MAGIC_NUMBER)

    fun getMessage() = "Hello World, I am consuming a service!"
}

data class SomeDataCollectedFromExternalService(
    private val message: String,
    private val someNumber: Int
)
