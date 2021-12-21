package com.sample.grpc.client

import com.sample.models.BalanceCheckRequest
import com.sample.models.BankServiceGrpc
import com.sample.models.WithdrawRequest
import io.grpc.ManagedChannelBuilder
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import mu.KLogging
import java.util.concurrent.CountDownLatch

class BankClientTest : FunSpec({
    lateinit var blockingStub: BankServiceGrpc.BankServiceBlockingStub
    lateinit var bankServiceStub: BankServiceGrpc.BankServiceStub

    beforeTest {
        val managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().build()

        blockingStub = BankServiceGrpc.newBlockingStub(managedChannel)
        bankServiceStub = BankServiceGrpc.newStub(managedChannel)
    }

    test("balance amount") {
        val balanceCheckRequest = BalanceCheckRequest.newBuilder().setAccountNumber(5).build()

        val balance = blockingStub.getBalance(balanceCheckRequest)

        logger.info("Received -> $balance")

        balance.amount.shouldBe(50)
    }

    test("withdraw") {
        val request = WithdrawRequest.newBuilder().setAccountNumber(7).setAmount(40).build()
        blockingStub.withdraw(request).forEachRemaining { money ->
            logger.info("Received -> ${money.value}")
        }
    }

    test("withdrawAsync") {
        val latch = CountDownLatch(1)

        val request = WithdrawRequest.newBuilder().setAccountNumber(10).setAmount(50).build()
        bankServiceStub.withdraw(request, MoneySteamingResponse(latch))
        latch.await()
    }
}) {
    companion object : KLogging()
}
