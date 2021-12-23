package com.sample.grpc.client.loadbalancing

import com.sample.grpc.client.rpctypes.BalanceStreamObserver
import com.sample.models.BalanceCheckRequest
import com.sample.models.BankServiceGrpc
import com.sample.models.DepositRequest
import io.grpc.ManagedChannelBuilder
import io.kotest.core.spec.style.FunSpec
import mu.KLogging
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ThreadLocalRandom

class NginxTestClient : FunSpec({
    lateinit var blockingStub: BankServiceGrpc.BankServiceBlockingStub
    lateinit var bankServiceStub: BankServiceGrpc.BankServiceStub

    beforeTest {
        val managedChannel = ManagedChannelBuilder.forAddress("localhost", 8585) // nginx port
            .usePlaintext().build()

        blockingStub = BankServiceGrpc.newBlockingStub(managedChannel)
        bankServiceStub = BankServiceGrpc.newStub(managedChannel)
    }

    test("getBalance") {
        for (i in 0..10) {
            Thread.sleep(500)
            val balanceCheckRequest = BalanceCheckRequest.newBuilder()
                .setAccountNumber(ThreadLocalRandom.current().nextInt(1, 10))
                .build()
            val balance = blockingStub.getBalance(balanceCheckRequest)

            logger.info("Received -> $balance")
        }
    }

    test("cashStreamingRequest") {
        val latch = CountDownLatch(1)
        val streamObserver = bankServiceStub.cashDeposit(BalanceStreamObserver(latch))
        for (i in 0..10) {
            val depositRequest = DepositRequest.newBuilder()
                .setAccountNumber(8)
                .setAmount(10)
                .build()
            streamObserver.onNext(depositRequest)
        }
        streamObserver.onCompleted()
        latch.await()
    }
}) {
    companion object : KLogging()
}
