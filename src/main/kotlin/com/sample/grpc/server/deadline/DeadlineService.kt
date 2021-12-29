package com.sample.grpc.server.deadline

import com.google.common.util.concurrent.Uninterruptibles
import com.sample.grpc.server.AccountDatabase
import com.sample.models.* // ktlint-disable no-wildcard-imports
import io.grpc.Context
import io.grpc.Status
import io.grpc.stub.StreamObserver
import mu.KLogging
import java.util.concurrent.TimeUnit

class DeadlineService : BankServiceGrpc.BankServiceImplBase() {
    companion object : KLogging()

    override fun getBalance(request: BalanceCheckRequest, responseObserver: StreamObserver<Balance>) {
        val accountNumber = request.accountNumber
        val balance = Balance.newBuilder().apply {
            amount = AccountDatabase.getBalance(accountNumber)
        }.build()

        // simulate time-consuming call
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS)
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

        run {
            for (i in 0 until (amount / 10)) {
                val money = Money.newBuilder().apply { value = 10 }.build()
                // simulate time-consuming call
                Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS)

                if (Context.current().isCancelled) {
                    return@run
                }

                responseObserver.onNext(money)
                logger.info("Delivered $10")
                AccountDatabase.deductBalance(accountNumber, money.value)
            }
        }

        logger.info("Completed!")
        responseObserver.onCompleted()
    }
}
