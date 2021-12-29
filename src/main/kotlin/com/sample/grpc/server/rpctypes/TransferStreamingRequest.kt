package com.sample.grpc.server.rpctypes

import com.sample.grpc.server.AccountDatabase
import com.sample.models.Account
import com.sample.models.TransferRequest
import com.sample.models.TransferResponse
import com.sample.models.TransferStatus
import io.grpc.stub.StreamObserver

class TransferStreamingRequest(
    var transferResponseStreamObserver: StreamObserver<TransferResponse>
) : StreamObserver<TransferRequest> {

    override fun onNext(request: TransferRequest) {
        val fromAccount = request.fromAccount
        val toAccount = request.toAccount
        val amount = request.amount
        val balance = AccountDatabase.getBalance(fromAccount)

        var status = TransferStatus.FAILED
        if (balance >= amount && fromAccount != toAccount) {
            AccountDatabase.deductBalance(fromAccount, amount)
            AccountDatabase.addBalance(toAccount, amount)
            status = TransferStatus.SUCCESS
        }

        val fromAccountInfo = Account.newBuilder().apply {
            this.accountNumber = fromAccount
            this.amount = AccountDatabase.getBalance(fromAccount)
        }.build()

        val toAccountInfo = Account.newBuilder().apply {
            this.accountNumber = toAccount
            this.amount = AccountDatabase.getBalance(toAccount)
        }.build()

        val response = TransferResponse.newBuilder().apply {
            this.status = status
            this.addAccounts(fromAccountInfo)
            this.addAccounts(toAccountInfo)
        }.build()

        this.transferResponseStreamObserver.onNext(response)
    }

    override fun onError(t: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onCompleted() {
        AccountDatabase.printAccountDetails()
        this.transferResponseStreamObserver.onCompleted()
    }
}
