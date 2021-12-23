package com.sample.grpc.server.metadata

enum class UserRole {
    PRIME,
    STANDARD;

    fun isPrime(): Boolean {
        return this == PRIME
    }
}
