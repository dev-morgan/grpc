package com.sample.grpc.server.metadata

import io.grpc.ServerBuilder
import mu.KotlinLogging

fun main() {
    val server = ServerBuilder.forPort(6565)
        .intercept(AuthInterceptor())
        .addService(MetaDataService())
        .build()

    server.start()
    logger.info("GrpcServer started.")
    server.awaitTermination()
}

private val logger = KotlinLogging.logger {}
