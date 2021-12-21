package com.sample.grpc.server

import io.grpc.ServerBuilder
import mu.KotlinLogging

fun main() {
    val server = ServerBuilder.forPort(6565)
        .addService(BankService())
        .addService(TransferService())
        .build()

    server.start()
    logger.info("GrpcServer started.")
    server.awaitTermination()
}

private val logger = KotlinLogging.logger {}
