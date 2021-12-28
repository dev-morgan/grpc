package com.sample.grpc.client.metadata

import com.sample.models.WithdrawalError
import io.grpc.Metadata
import io.grpc.Metadata.ASCII_STRING_MARSHALLER
import io.grpc.protobuf.ProtoUtils

class ClientConstants {
    companion object {
        var WITHDRAWAL_ERROR_KEY: Metadata.Key<WithdrawalError> =
            ProtoUtils.keyForProto(WithdrawalError.getDefaultInstance())
        var USER_TOKEN: Metadata.Key<String> = Metadata.Key.of("user-token", ASCII_STRING_MARSHALLER)
        private var METADATA = Metadata()

        init {
            METADATA.put(Metadata.Key.of("client-token", ASCII_STRING_MARSHALLER), "bank-client-secret")
        }

        fun getClientToken(): Metadata {
            return METADATA
        }
    }
}
