package com.sample.grpc.server

import io.grpc.ServerBuilder

fun main() {
    val server = ServerBuilder.forPort(6565)
        .addService(BankService())
        .build()

    server.start()
    server.awaitTermination()
}
