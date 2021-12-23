package com.sample.grpc.server.metadata

import com.sample.grpc.server.metadata.ServerConstants.Companion.CTX_USER_ROLE
import io.grpc.*

class AuthInterceptor : ServerInterceptor {
    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        metadata: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT>? {
        val clientToken = metadata.get(ServerConstants.USER_TOKEN) as String

        if (isValidToken(clientToken)) {
            val userRole = this.extractUserRole(clientToken)
            val context = Context.current().withValue(CTX_USER_ROLE, userRole)
            return Contexts.interceptCall(context, call, metadata, next)
        } else {
            val status = Status.UNAUTHENTICATED.withDescription("invalid token/expired token")
            call.close(status, metadata)
        }

        return object : ServerCall.Listener<ReqT>() {}
    }

    private fun isValidToken(token: String): Boolean {
        return token.startsWith("user-secret-3") || token.startsWith("user-secret-2")
    }

    private fun extractUserRole(jwt: String): UserRole {
        return when {
            jwt.endsWith("prime") -> UserRole.PRIME
            else -> UserRole.STANDARD
        }
    }
}
