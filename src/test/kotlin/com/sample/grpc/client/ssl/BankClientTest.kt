package com.sample.grpc.client.ssl

import com.sample.models.BalanceCheckRequest
import com.sample.models.BankServiceGrpc
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import mu.KLogging
import java.io.File

class BankClientTest : FunSpec({
    lateinit var blockingStub: BankServiceGrpc.BankServiceBlockingStub

    beforeTest {
        val sslContext = GrpcSslContexts.forClient()
            .trustManager(File("ssl-path/ca.cert.pem"))
            .build()

        val managedChannel = NettyChannelBuilder.forAddress("localhost", 6565)
            .sslContext(sslContext)
            .build()

        blockingStub = BankServiceGrpc.newBlockingStub(managedChannel)
    }

    test("getBalance") {
        val balanceCheckRequest = BalanceCheckRequest.newBuilder().setAccountNumber(5).build()

        val balance = blockingStub.getBalance(balanceCheckRequest)

        logger.info("Received -> $balance")

        balance.amount.shouldBe(50)
    }
}) {
    companion object : KLogging()
}
