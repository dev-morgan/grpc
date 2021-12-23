package com.sample.grpc.server.metadata

import io.grpc.*

class AuthInterceptor : ServerInterceptor {
    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        metadata: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT>? {
        val clientToken = metadata.get(ServerConstants.USER_TOKEN) as String

        if (isValidate(clientToken)) {
            return next.startCall(call, metadata)
        } else {
            val status = Status.UNAUTHENTICATED.withDescription("invalid token/expired token")
            call.close(status, metadata)
        }

        return object : ServerCall.Listener<ReqT>() {}
    }

    private fun isValidate(token: String): Boolean {
        return token == "user-secret-3"
    }
}
