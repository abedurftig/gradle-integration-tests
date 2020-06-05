package com.github.abedurftig

import feign.Feign
import feign.Logger
import feign.RequestLine
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import feign.okhttp.OkHttpClient
import feign.slf4j.Slf4jLogger

internal interface ExternalService {
    @RequestLine("GET /some/thing")
    fun someThing(): ExternalServiceData
}

data class ExternalServiceData(
    private val message: String? = null,
    private val someNumber: Int? = null
)

class ExternalServiceConsumer {

    fun getDataFromExternalService() =
        getClient().someThing()

    fun getMessage() = "Hello World, I am consuming a service!"

    private fun getClient() = Feign.builder()
        .client(OkHttpClient())
        .decoder(GsonDecoder())
        .encoder(GsonEncoder())
        .logger(Slf4jLogger("ExternalService.Logger"))
        .logLevel(Logger.Level.FULL)
        .target(ExternalService::class.java, "http://localhost:9999")
}
