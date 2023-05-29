package com.eagskunst.simplepostapp.domain

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import timber.log.Timber

abstract class BaseUseCase<R : Any, Params>(
    private val backgroundScheduler: BackgroundScheduler,
    private val mainScheduler: MainScheduler,
) {
    protected val disposable = CompositeDisposable()

    abstract fun createObservable(
        params: Params?,
        onSuccess: (R) -> Unit,
        onError: (Throwable?) -> Unit,
    ): Observable<R>

    fun execute(
        params: Params?,
        onSuccess: (R) -> Unit,
        onError: (Throwable?) -> Unit,
    ) {
        createObservable(params, onSuccess, onError)
            .subscribeOn(backgroundScheduler.getScheduler())
            .observeOn(mainScheduler.getScheduler())
            .subscribeBy(
                onNext = onSuccess,
                onError = onError,
                onComplete = { Timber.d("Completed use case ${this.javaClass.simpleName}") },
            ).addTo(disposable)
    }

    fun stop() {
        disposable.clear()
    }
}
