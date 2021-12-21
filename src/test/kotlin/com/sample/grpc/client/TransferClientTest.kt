package com.sample.grpc.client

import com.sample.models.TransferRequest
import com.sample.models.TransferServiceGrpc
import io.grpc.ManagedChannelBuilder
import io.kotest.core.spec.style.FunSpec
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ThreadLocalRandom

class TransferClientTest : FunSpec({

    lateinit var stub: TransferServiceGrpc.TransferServiceStub

    beforeTest {
        val managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().build()
        stub = TransferServiceGrpc.newStub(managedChannel)
    }

    test("transfer") {
        val latch = CountDownLatch(1)
        val response = TransferStreamingResponse(latch)
        val requestStreamObserver = stub.transfer(response)

        for (i in 1..100) {
            val request = TransferRequest.newBuilder()
                .setFromAccount(ThreadLocalRandom.current().nextInt(1, 11))
                .setToAccount(ThreadLocalRandom.current().nextInt(1, 11))
                .setAmount(ThreadLocalRandom.current().nextInt(1, 21))
                .build()

            requestStreamObserver.onNext(request)
        }
        requestStreamObserver.onCompleted()
        latch.await()
    }
})
