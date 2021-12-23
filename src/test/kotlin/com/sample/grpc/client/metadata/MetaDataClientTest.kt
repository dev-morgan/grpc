package com.sample.grpc.client.metadata

import com.sample.grpc.client.rpctypes.MoneySteamingResponse
import com.sample.models.BalanceCheckRequest
import com.sample.models.BankServiceGrpc
import com.sample.models.WithdrawRequest
import io.grpc.Deadline
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import io.grpc.stub.MetadataUtils
import io.kotest.core.spec.style.FunSpec
import mu.KLogging
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

class MetaDataClientTest : FunSpec({
    lateinit var blockingStub: BankServiceGrpc.BankServiceBlockingStub
    lateinit var bankServiceStub: BankServiceGrpc.BankServiceStub

    beforeTest {
        val managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
            .intercept(MetadataUtils.newAttachHeadersInterceptor(ClientConstants.getClientToken()))
            .usePlaintext().build()

        blockingStub = BankServiceGrpc.newBlockingStub(managedChannel)
        bankServiceStub = BankServiceGrpc.newStub(managedChannel)
    }

    test("getBalance") {
        val balanceCheckRequest = BalanceCheckRequest.newBuilder()
            .setAccountNumber(7)
            .build()

        for (i in 0..20) {
            val random = ThreadLocalRandom.current().nextInt(1, 4)
            logger.info { "random : $random" }

            try {
                val balance = blockingStub
                    .withCallCredentials(UserSessionToken("user-secret-$random:standard"))
                    .getBalance(balanceCheckRequest)
                logger.info("Received -> $balance")
            } catch (e: StatusRuntimeException) {
                logger.error { e.message }
            }
        }
    }

    test("withdraw") {
        val request = WithdrawRequest.newBuilder()
            .setAccountNumber(6)
            .setAmount(50)
            .build()

        try {
            blockingStub
                .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                .withdraw(request)
                .forEachRemaining { money ->
                    logger.info("Received -> ${money.value}")
                }
        } catch (e: StatusRuntimeException) {
            //
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
