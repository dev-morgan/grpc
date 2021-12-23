package com.sample.grpc.server.rpctypes

import com.sample.grpc.server.AccountDatabase
import com.sample.models.* // ktlint-disable no-wildcard-imports
import io.grpc.Status
import io.grpc.stub.StreamObserver

class BankService : BankServiceGrpc.BankServiceImplBase() {

    override fun getBalance(request: BalanceCheckRequest, responseObserver: StreamObserver<Balance>) {
        val accountNumber = request.accountNumber
        val balance = Balance.newBuilder()
            .setAmount(AccountDatabase.getBalance(accountNumber))
            .build()
        responseObserver.onNext(balance)
        responseObserver.onCompleted()
    }

    override fun withdraw(request: WithdrawRequest, responseObserver: StreamObserver<Money>) {
        val accountNumber = request.accountNumber
        val amount = request.amount // 10, 20, 30 ..
        val balance = AccountDatabase.getBalance(accountNumber)

        if (balance < amount) {
            val status = Status.FAILED_PRECONDITION.withDescription("No enough money. You have only $balance")
            responseObserver.onError(status.asRuntimeException())
            return
        }

        for (i in 0 until (amount / 10)) {
            val money = Money.newBuilder().setValue(10).build()
            responseObserver.onNext(money)
            AccountDatabase.deductBalance(accountNumber, money.value)
            Thread.sleep(1000)
        }

        responseObserver.onCompleted()
    }

    override fun cashDeposit(responseObserver: StreamObserver<Balance>): StreamObserver<DepositRequest> {
        return CashStreamingRequest(responseObserver)
    }
}
