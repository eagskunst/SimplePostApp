package com.eagskunst.simplepostapp.data

import com.eagskunst.simplepostapp.commons.GenericException
import com.eagskunst.simplepostapp.domain.PostEntity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class PostsRemoteDataSourceTest {
    private val dataSource = PostsRemoteDataSource()
    private val testScheduler = TestScheduler()

    @Before
    fun setUp() {
        RxJavaPlugins.reset()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
    }

    @After
    fun tearDown() = RxJavaPlugins.reset()

    @Test
    fun given_firstTime_when_getPosts_then_observableReturnsError() {
        val testObserver = dataSource.getPosts().test()
        advanceTime {
            testObserver.assertFailure(GenericException::class.java)
        }
    }

    @Test
    fun given_secondTime_when_getPosts_then_observableReturnsError() {
        val testObserver = executeGetPostsTwoTimes().test()
        advanceTime {
            testObserver.assertValue(dataSource.posts)
        }
    }

    private fun executeGetPostsTwoTimes(): Observable<List<PostEntity>> {
        val testObserver = dataSource.getPosts().test()
        advanceTime { testObserver.dispose() }
        return dataSource.getPosts().also { advanceTime {} }
    }

    @Test
    fun when_removePost_then_observableReturnsListWithoutRemovedPost() {
        val localPosts = mutableListOf<PostEntity>().apply { addAll(dataSource.posts) }
        with(dataSource) {
            executeGetPostsTwoTimes().test().dispose()
            localPosts.remove(posts.last())
            val testObserver = dataSource.removePost(posts.last()).test()
            advanceTime {
                testObserver.assertValue(localPosts)
            }
        }
    }

    @Test
    fun when_addPost_then_observableReturnsListWithoutAddedPost() {
        val localPosts = mutableListOf<PostEntity>().apply { addAll(dataSource.posts) }
        val newPost = PostEntity("new post", "hi!!!")
        with(dataSource) {
            executeGetPostsTwoTimes().test().dispose()
            localPosts.add(newPost.copy(added = true))
            val observer = addPost(newPost).test()
            advanceTime {
                observer.assertValue(localPosts)
            }
        }
    }

    @Test
    fun given_somePostsWith1InItsName_when_searchWith1AsWord_then_observableContainsOnlyPostWithThatWord() {
        val posts = (1..10).map {
            PostEntity(
                name = it.toString().repeat(10),
                description = "lorem ipsum",
                added = true,
            )
        }
        val observables = posts.map {
            dataSource.addPost(it)
        }
        advanceTime(observables.size * PostsRemoteDataSource.DEFAULT_DELAY_TIME + 1) {
            observables.forEach { it.test().dispose() }
        }
        dataSource.searchPosts("1").test()
            .assertValue(
                posts.filter { it.name.contains("1") || it.description.contains("1") },
            )
    }

    @Test
    fun given_somePostsWith1InItsDescription_when_searchWith1AsWord_then_observableContainsOnlyPostWithThatWord() {
        val posts = (1..10).map {
            PostEntity(
                name = "lorem ipsum $it",
                description = it.toString().repeat(10),
                added = true,
            )
        }
        val observables = posts.map {
            dataSource.addPost(it)
        }
        advanceTime(observables.size * PostsRemoteDataSource.DEFAULT_DELAY_TIME + 1) {
            observables.forEach { it.test().dispose() }
        }
        dataSource.searchPosts("1").test()
            .assertValue(
                posts.filter { it.name.contains("1") || it.description.contains("1") },
            )
    }

    @Test
    fun given_somePosts_when_searchWithEmptyInput_then_observableContainsAllPosts() {
        val posts = (1..10).map {
            PostEntity(
                name = "lorem ipsum $it",
                description = it.toString().repeat(10),
                added = true,
            )
        }
        val observables = posts.map {
            dataSource.addPost(it)
        }
        advanceTime(observables.size * PostsRemoteDataSource.DEFAULT_DELAY_TIME + 1) {
            observables.forEach { it.test().dispose() }
        }
        dataSource.searchPosts("").test()
            .assertValue(dataSource.posts)
    }

    private inline fun advanceTime(
        time: Long = PostsRemoteDataSource.DEFAULT_DELAY_TIME + 1,
        doAfterAdvance: () -> Unit,
    ) {
        testScheduler.advanceTimeBy(time, TimeUnit.SECONDS)
        doAfterAdvance()
    }
}
