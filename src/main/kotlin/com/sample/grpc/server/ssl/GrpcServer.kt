package com.sample.grpc.server.ssl

import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder
import mu.KotlinLogging
import java.io.File

fun main() {

    val sslContext = GrpcSslContexts.configure(
        SslContextBuilder.forServer(
            File("ssl-path/localhost.crt"),
            File("ssl-path/localhost.pem")
        )
    ).build()

    val server = NettyServerBuilder.forPort(6565)
        .sslContext(sslContext)
        .addService(BankService())
        .build()

    server.start()
    logger.info("GrpcServer started.")
    server.awaitTermination()
}

private val logger = KotlinLogging.logger {}
