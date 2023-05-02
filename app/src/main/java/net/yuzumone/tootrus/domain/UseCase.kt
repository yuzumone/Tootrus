package net.yuzumone.tootrus.domain

import kotlinx.coroutines.*

abstract class UseCase<in P, out R> {

    abstract suspend fun run(params: P): R

    operator fun invoke(params: P, scope: CoroutineScope, onResult: (Result<R>) -> Unit = {}) {
        val job = scope.async(Dispatchers.Default) { run(params) }
        scope.launch(Dispatchers.Main) {
            try {
                onResult(Success(job.await()))
            } catch (e: Exception) {
                onResult(Failure(e))
            }
        }
    }
}
