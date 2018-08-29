package net.yuzumone.tootrus.domain

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

abstract class UseCase<in P, out R>  {

    abstract suspend fun run(params: P): R

    operator fun invoke(params: P, onResult: (Result<R>) -> Unit = {}) {
        val job = async(CommonPool) { run(params) }
        launch(UI) {
            try {
                onResult(Success(job.await()))
            } catch (e: Exception) {
                onResult(Failure(e))
            }
        }
    }
}