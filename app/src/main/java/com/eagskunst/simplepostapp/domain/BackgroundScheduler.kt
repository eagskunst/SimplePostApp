package com.eagskunst.simplepostapp.domain

import io.reactivex.rxjava3.core.Scheduler

fun interface BackgroundScheduler {
    fun getScheduler(): Scheduler
}