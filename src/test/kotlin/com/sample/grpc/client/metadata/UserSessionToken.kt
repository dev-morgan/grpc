package com.sample.grpc.client.metadata

import com.sample.grpc.client.metadata.ClientConstants.Companion.USER_TOKEN
import io.grpc.CallCredentials
import io.grpc.Metadata
import java.util.concurrent.Executor

class UserSessionToken(
    private val jwt: String
) : CallCredentials() {

    override fun applyRequestMetadata(requestInfo: RequestInfo, appExecutor: Executor, applier: MetadataApplier) {
        appExecutor.execute {
            val metadata = Metadata()
            metadata.put(USER_TOKEN, this.jwt)
            applier.apply(metadata)
//            applier.fail()
        }
    }

    override fun thisUsesUnstableApi() {
        TODO("Not yet implemented")
    }
}
