package com.sample.grpc.client

import com.sample.models.Money
import io.grpc.stub.StreamObserver
import mu.KLogging
import java.util.concurrent.CountDownLatch

class MoneySteamingResponse(
    private val latch: CountDownLatch
) : StreamObserver<Money> {
    companion object : KLogging()

    override fun onNext(money: Money) {
        logger.info("Received async : ${money.value}")
    }

    override fun onError(t: Throwable) {
        logger.error { t.message }
        latch.countDown()
    }

    override fun onCompleted() {
        logger.info("Server is done.")
        latch.countDown()
    }
}
