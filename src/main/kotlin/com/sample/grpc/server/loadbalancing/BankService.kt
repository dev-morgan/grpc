package com.sample.grpc.server.loadbalancing

import com.sample.grpc.server.rpctypes.AccountDatabase
import com.sample.models.Balance
import com.sample.models.BalanceCheckRequest
import com.sample.models.BankServiceGrpc
import io.grpc.stub.StreamObserver
import mu.KLogging

class BankService : BankServiceGrpc.BankServiceImplBase() {
    companion object : KLogging()

    override fun getBalance(request: BalanceCheckRequest, responseObserver: StreamObserver<Balance>) {
        val accountNumber = request.accountNumber
        logger.info("Received the request for $accountNumber")

        val balance = Balance.newBuilder()
            .setAmount(AccountDatabase.getBalance(accountNumber))
            .build()
        responseObserver.onNext(balance)
        responseObserver.onCompleted()
    }
}
