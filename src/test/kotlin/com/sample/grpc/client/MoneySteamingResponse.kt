package com.sample.grpc.client

import com.sample.models.Money
import io.grpc.stub.StreamObserver
import mu.KLogging

class MoneySteamingResponse : StreamObserver<Money> {
    companion object : KLogging()

    override fun onNext(money: Money) {
        logger.info("Received async : ${money.value}")
    }

    override fun onError(t: Throwable) {
        logger.error { t.message }
    }

    override fun onCompleted() {
        logger.info("Server is done.")
    }
}
