package net.yuzumone.tootrus.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class UseCase<in P, out R>  {

    abstract suspend fun run(params: P): R

    operator fun invoke(params: P, onResult: (Result<R>) -> Unit = {}) {
        val job = GlobalScope.async(Dispatchers.Default) { run(params) }
        GlobalScope.launch(Dispatchers.Main) {
            try {
                onResult(Success(job.await()))
            } catch (e: Exception) {
                onResult(Failure(e))
            }
        }
    }
}