package com.sample.grpc.client.metadata

import io.grpc.Metadata
import io.grpc.Metadata.ASCII_STRING_MARSHALLER

class ClientConstants {
    companion object {
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
