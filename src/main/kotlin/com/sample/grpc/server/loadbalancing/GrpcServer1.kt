package com.sample.grpc.server.loadbalancing

import io.grpc.ServerBuilder
import mu.KotlinLogging

fun main() {
    val server = ServerBuilder.forPort(7575)
        .addService(BankService())
        .build()

    server.start()
    logger.info("GrpcServer(7575) started.")
    server.awaitTermination()
}

private val logger = KotlinLogging.logger {}
