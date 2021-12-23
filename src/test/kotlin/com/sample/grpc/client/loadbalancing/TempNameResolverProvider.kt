package com.sample.grpc.client.loadbalancing

import io.grpc.NameResolver
import io.grpc.NameResolverProvider
import mu.KLogging
import java.net.URI

class TempNameResolverProvider() : NameResolverProvider() {
    companion object : KLogging()

    override fun newNameResolver(targetUri: URI, args: NameResolver.Args): NameResolver {
        logger.info { "Looking for service : $targetUri" }
        return TempNameResolver(targetUri.authority)
    }

    override fun getDefaultScheme(): String = "http"

    override fun isAvailable(): Boolean = true

    override fun priority(): Int = 5
}
