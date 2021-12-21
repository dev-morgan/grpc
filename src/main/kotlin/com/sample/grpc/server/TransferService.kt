package com.sample.grpc.server

import com.sample.models.TransferRequest
import com.sample.models.TransferResponse
import com.sample.models.TransferServiceGrpc
import io.grpc.stub.StreamObserver

class TransferService : TransferServiceGrpc.TransferServiceImplBase() {
    override fun transfer(responseObserver: StreamObserver<TransferResponse>): StreamObserver<TransferRequest> {
        return TransferStreamingRequest(responseObserver)
    }
}
