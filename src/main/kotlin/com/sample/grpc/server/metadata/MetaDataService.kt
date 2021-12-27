package com.sample.grpc.server.metadata

import com.sample.grpc.server.AccountDatabase
import com.sample.models.*
import io.grpc.Context
import io.grpc.Metadata
import io.grpc.Status
import io.grpc.protobuf.ProtoUtils
import io.grpc.stub.StreamObserver
import mu.KLogging

class MetaDataService : BankServiceGrpc.BankServiceImplBase() {
    companion object : KLogging()

    override fun getBalance(request: BalanceCheckRequest, responseObserver: StreamObserver<Balance>) {
        val accountNumber = request.accountNumber
        val userRole = ServerConstants.CTX_USER_ROLE.get() // from context
        val userRole1 = ServerConstants.CTX_USER_ROLE1.get() // from context
        var amount = AccountDatabase.getBalance(accountNumber)

        amount = if (userRole.isPrime()) amount else (amount - 15)

        logger.info { "$userRole : $userRole1" }

        val balance = Balance.newBuilder()
            .setAmount(amount)
            .build()

        // simulate time-consuming call
        responseObserver.onNext(balance)
        responseObserver.onCompleted()
    }

    override fun withdraw(request: WithdrawRequest, responseObserver: StreamObserver<Money>) {
        val accountNumber = request.accountNumber
        val amount = request.amount // 10, 20, 30 ..
        val balance = AccountDatabase.getBalance(accountNumber)

        if (amount < 10 || (amount % 10) != 0) {
            val metadata = Metadata()
            val errorKey = ProtoUtils.keyForProto(WithdrawalError.getDefaultInstance())
            val withdrawalError = WithdrawalError.newBuilder()
                .setAmount(balance)
                .setErrorMessage(ErrorMessage.ONLY_TEN_MULTIPLES)
                .build()
            metadata.put(errorKey, withdrawalError)

            responseObserver.onError(Status.FAILED_PRECONDITION.asRuntimeException(metadata))
            return
        }

        if (balance < amount) {
            val metadata = Metadata()
            val errorKey = ProtoUtils.keyForProto(WithdrawalError.getDefaultInstance())
            val withdrawalError = WithdrawalError.newBuilder()
                .setAmount(balance)
                .setErrorMessage(ErrorMessage.INSUFFICIENT_BALANCE)
                .build()
            metadata.put(errorKey, withdrawalError)

            responseObserver.onError(Status.FAILED_PRECONDITION.asRuntimeException(metadata))
            return
        }

        run {
            for (i in 0 until (amount / 10)) {
                val money = Money.newBuilder().setValue(10).build()

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
