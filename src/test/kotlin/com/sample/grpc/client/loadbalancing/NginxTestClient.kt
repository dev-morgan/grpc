package com.sample.grpc.client.loadbalancing

import com.sample.models.BalanceCheckRequest
import com.sample.models.BankServiceGrpc
import io.grpc.ManagedChannelBuilder
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import mu.KLogging
import java.util.concurrent.ThreadLocalRandom

class NginxTestClient : FunSpec({
    lateinit var blockingStub: BankServiceGrpc.BankServiceBlockingStub

    beforeTest {
        val managedChannel = ManagedChannelBuilder.forAddress("localhost", 8585) // nginx port
            .usePlaintext().build()

        blockingStub = BankServiceGrpc.newBlockingStub(managedChannel)
    }

    test("getBalance") {
        for (i in 1..10) {
            Thread.sleep(500)
            val balanceCheckRequest = BalanceCheckRequest.newBuilder()
                .setAccountNumber(ThreadLocalRandom.current().nextInt(1, 10))
                .build()
            val balance = blockingStub.getBalance(balanceCheckRequest)

            logger.info("Received -> $balance")
            balance.amount.shouldNotBeNull()
        }
    }
}) {
    companion object : KLogging()
}
