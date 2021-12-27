package com.sample.grpc.client.rpctypes

import com.sample.grpc.client.metadata.ClientConstants.Companion.WITHDRAWAL_ERROR_KEY
import com.sample.models.Money
import io.grpc.Status
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

    override fun onError(throwable: Throwable) {
        val metaData = Status.trailersFromThrowable(throwable)
        val withdrawalError = metaData?.get(WITHDRAWAL_ERROR_KEY)
        withdrawalError?.let {
            logger.error { "${withdrawalError.amount} : ${withdrawalError.errorMessage}" }
        }

        latch.countDown()
    }

    override fun onCompleted() {
        logger.info("Server is done.")
        latch.countDown()
    }
}
