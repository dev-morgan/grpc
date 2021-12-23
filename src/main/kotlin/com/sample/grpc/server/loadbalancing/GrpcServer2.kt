package com.sample.grpc.server.loadbalancing

import io.grpc.ServerBuilder
import mu.KotlinLogging

fun main() {
    val server = ServerBuilder.forPort(6565)
        .addService(BankService())
        .build()

    server.start()
    logger.info("GrpcServer(6565) started.")
    server.awaitTermination()
}

private val logger = KotlinLogging.logger {}
