package com.sample.grpc.client.loadbalancing

import io.grpc.NameResolver

class TempNameResolver(
    val service: String
) : NameResolver() {
    override fun getServiceAuthority(): String {
        return "temp"
    }

    override fun shutdown() {
        TODO("Not yet implemented")
    }

    override fun refresh() {
        super.refresh()
    }

    override fun start(listener: Listener2) {
        val instances = ServiceRegistry.getInstances(service)
        val resolutionResult = ResolutionResult.newBuilder()
            .setAddresses(instances)
            .build()
        listener.onResult(resolutionResult)
    }
}
