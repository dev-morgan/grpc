package com.sample.grpc.server

import com.sample.models.Balance
import com.sample.models.BalanceCheckRequest
import com.sample.models.BankServiceGrpc
import io.grpc.stub.StreamObserver

class BankService : BankServiceGrpc.BankServiceImplBase() {

    override fun getBalance(request: BalanceCheckRequest, responseObserver: StreamObserver<Balance>) {
        val accountNumber = request.accountNumber
        val balance = Balance.newBuilder()
            .setAmount(accountNumber * 10)
            .build()
        responseObserver.onNext(balance)
        responseObserver.onCompleted()
    }
}