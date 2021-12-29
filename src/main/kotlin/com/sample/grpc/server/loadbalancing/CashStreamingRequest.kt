package com.sample.grpc.server.loadbalancing

import com.sample.grpc.server.AccountDatabase
import com.sample.models.Balance
import com.sample.models.DepositRequest
import io.grpc.stub.StreamObserver
import mu.KLogging

class CashStreamingRequest(
    private val balanceStreamObserver: StreamObserver<Balance>
) : StreamObserver<DepositRequest> {
    companion object : KLogging()

    private var accountBalance: Int = 0

    override fun onNext(request: DepositRequest) {
        val accountNumber = request.accountNumber
        val amount = request.amount

        logger.info("Received cash deposit for $accountNumber")

        this.accountBalance = AccountDatabase.addBalance(accountNumber, amount)
    }

    override fun onError(t: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun onCompleted() {
        val balance = Balance.newBuilder().apply { amount = accountBalance }.build()
        balanceStreamObserver.onNext(balance)
        balanceStreamObserver.onCompleted()
    }
}
