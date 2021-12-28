package com.sample.grpc.server.ssl

import com.sample.models.*
import io.grpc.stub.StreamObserver

class BankService : BankServiceGrpc.BankServiceImplBase() {

    override fun getBalance(request: BalanceCheckRequest?, responseObserver: StreamObserver<Balance>?) {
        super.getBalance(request, responseObserver)
    }

    override fun withdraw(request: WithdrawRequest?, responseObserver: StreamObserver<Money>?) {
        super.withdraw(request, responseObserver)
    }

    override fun cashDeposit(responseObserver: StreamObserver<Balance>?): StreamObserver<DepositRequest> {
        return super.cashDeposit(responseObserver)
    }
}
