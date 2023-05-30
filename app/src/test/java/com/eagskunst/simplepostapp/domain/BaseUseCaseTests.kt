package com.eagskunst.simplepostapp.domain

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Before

abstract class BaseUseCaseTests<UseCase : BaseUseCase<*, *>> {
    @MockK
    lateinit var backgroundScheduler: BackgroundScheduler

    @MockK
    lateinit var mainScheduler: MainScheduler

    protected val testScheduler = TestScheduler()

    @InjectMockKs
    lateinit var useCase: UseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { backgroundScheduler.getScheduler() } returns testScheduler
        every { mainScheduler.getScheduler() } returns testScheduler
    }
}
