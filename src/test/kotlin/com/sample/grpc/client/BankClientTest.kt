package com.sample.grpc.client

import com.sample.models.BalanceCheckRequest
import com.sample.models.BankServiceGrpc
import io.grpc.ManagedChannelBuilder
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import mu.KLogging

class BankClientTest : FunSpec({
    lateinit var blockingStub: BankServiceGrpc.BankServiceBlockingStub

    beforeTest {
        val managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
            .usePlaintext()
            .build()

        blockingStub = BankServiceGrpc.newBlockingStub(managedChannel)
    }

    test("balance amount") {
        val balanceCheckRequest = BalanceCheckRequest.newBuilder()
            .setAccountNumber(5)
            .build()

        val balance = blockingStub.getBalance(balanceCheckRequest)

        logger.info("Received -> $balance")

        balance.amount.shouldBe(50)
    }

}) {
    companion object : KLogging()
}