package com.sample.grpc.server.rpctypes

import com.sample.grpc.server.AccountDatabase
import com.sample.models.Balance
import com.sample.models.DepositRequest
import io.grpc.stub.StreamObserver

class CashStreamingRequest(
    private val balanceStreamObserver: StreamObserver<Balance>
) : StreamObserver<DepositRequest> {
    private var accountBalance: Int = 0

    override fun onNext(request: DepositRequest) {
        val accountNumber = request.accountNumber
        val amount = request.amount

        this.accountBalance = AccountDatabase.addBalance(accountNumber, amount)
    }

    override fun onError(t: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun onCompleted() {
        val balance = Balance.newBuilder().apply { this.amount = accountBalance }.build()
        balanceStreamObserver.onNext(balance)
        balanceStreamObserver.onCompleted()
    }
}
