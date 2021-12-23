package com.sample.grpc.client.loadbalancing

import io.grpc.EquivalentAddressGroup
import java.net.InetSocketAddress

object ServiceRegistry {

    private val addressMap = hashMapOf<String, List<EquivalentAddressGroup>>()

    fun register(service: String, instances: List<String>) {
        val addressGroups =
            instances.map { it.split(":") }
                .map { InetSocketAddress(it[0], Integer.parseInt(it[1])) }
                .map { EquivalentAddressGroup(it) }
                .toList()

        addressMap[service] = addressGroups
    }

    fun getInstances(service: String): List<EquivalentAddressGroup>? {
        return addressMap[service]
    }
}
