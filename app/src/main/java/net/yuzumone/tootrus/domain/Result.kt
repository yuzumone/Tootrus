package net.yuzumone.tootrus.domain

sealed class Result<out V>
data class Success<V>(val value: V) : Result<V>()
data class Failure<V>(val reason: Exception) : Result<V>()
