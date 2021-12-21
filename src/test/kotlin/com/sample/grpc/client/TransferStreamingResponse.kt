package com.sample.grpc.client

import com.sample.models.TransferResponse
import io.grpc.stub.StreamObserver
import mu.KLogging
import java.util.concurrent.CountDownLatch

class TransferStreamingResponse(
    private val latch: CountDownLatch
) : StreamObserver<TransferResponse> {
    companion object : KLogging()

    override fun onNext(response: TransferResponse) {
        logger.info("Status : ${response.status}")
        response.accountsList.forEach {
            logger.info("${it.accountNumber} : ${it.amount}")
        }

        logger.info("-----------------------")
    }

    override fun onError(t: Throwable) {
        latch.countDown()
    }

    override fun onCompleted() {
        logger.info("all transfers done.")
        latch.countDown()
    }
}
