package com.sample.grpc.client

import com.sample.models.Balance
import io.grpc.stub.StreamObserver
import mu.KLogging
import java.util.concurrent.CountDownLatch

class BalanceStreamObserver(
    private val latch: CountDownLatch
) : StreamObserver<Balance> {
    companion object : KLogging()

    override fun onNext(balance: Balance) {
        logger.info("Final Balance : ${balance.amount}")
    }

    override fun onError(t: Throwable) {
        latch.countDown()
    }

    override fun onCompleted() {
        logger.info("Server is done.")
        latch.countDown()
    }
}
