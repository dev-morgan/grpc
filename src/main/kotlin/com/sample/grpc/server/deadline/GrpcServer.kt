package com.sample.grpc.server.deadline

import io.grpc.ServerBuilder
import mu.KotlinLogging

fun main() {
    val server = ServerBuilder.forPort(6565)
        .addService(DeadlineService())
        .build()

    server.start()
    logger.info("GrpcServer started.")
    server.awaitTermination()
}

private val logger = KotlinLogging.logger {}
