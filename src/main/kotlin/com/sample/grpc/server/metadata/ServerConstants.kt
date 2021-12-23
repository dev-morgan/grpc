package com.sample.grpc.server.metadata

import io.grpc.Metadata

class ServerConstants {
    companion object {
        var TOKEN: Metadata.Key<String> = Metadata.Key.of("client-token", Metadata.ASCII_STRING_MARSHALLER)
        var USER_TOKEN: Metadata.Key<String> = Metadata.Key.of("user-token", Metadata.ASCII_STRING_MARSHALLER)
    }
}
