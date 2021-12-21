package com.sample.grpc.server

import mu.KLogging

/**
 * This is a DB
 *
 * 1=> 10
 * 2=> 20
 * ..
 * 10 => 100
 */
class AccountDatabase {
    companion object : KLogging() {

        private val balanceMap = (1..10).associateWith { it * 10 } as HashMap

        fun getBalance(accountId: Int): Int {
            return balanceMap[accountId]!!
        }

        fun addBalance(accountId: Int, amount: Int): Int {
            return balanceMap.computeIfPresent(accountId) { _, v -> v + amount }!!
        }

        fun deductBalance(accountId: Int, amount: Int): Int {
            return balanceMap.computeIfPresent(accountId) { _, v -> v - amount }!!
        }

        fun printAccountDetails() {
            logger.info("details -> $balanceMap")
        }
    }
}
