package com.sample.grpc.server.metadata

import io.grpc.Context
import io.grpc.Metadata

class ServerConstants {
    companion object {
        var TOKEN: Metadata.Key<String> = Metadata.Key.of("client-token", Metadata.ASCII_STRING_MARSHALLER)
        var USER_TOKEN: Metadata.Key<String> = Metadata.Key.of("user-token", Metadata.ASCII_STRING_MARSHALLER)
        var CTX_USER_ROLE: Context.Key<UserRole> = Context.key("user-role")
        var CTX_USER_ROLE1: Context.Key<UserRole> = Context.key("user-role1")
    }
}
