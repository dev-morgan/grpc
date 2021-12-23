package com.sample.grpc.client.dealine

import io.grpc.*
import java.util.concurrent.TimeUnit

class DeadLineInterceptor : ClientInterceptor {
    override fun <ReqT : Any, RespT : Any> interceptCall(
        method: MethodDescriptor<ReqT, RespT>,
        callOptions: CallOptions,
        channel: Channel
    ): ClientCall<ReqT, RespT> {

        callOptions.deadline?.let {
            callOptions == callOptions.withDeadline(Deadline.after(4, TimeUnit.SECONDS))
        }

        return channel.newCall(method, callOptions)
    }
}
