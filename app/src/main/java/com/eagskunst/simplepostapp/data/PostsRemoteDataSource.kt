package com.eagskunst.simplepostapp.data

import androidx.annotation.VisibleForTesting
import com.eagskunst.simplepostapp.commons.GenericException
import com.eagskunst.simplepostapp.domain.entity.PostEntity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsRemoteDataSource @Inject constructor() {

    companion object {
        const val GENERIC_ERROR_MSG = "Something wrong happened. Try again later"
        const val DEFAULT_DELAY_TIME = 2L
    }

    private var firstTime = true
    val posts = mutableListOf(
        PostEntity("Hello World", "Lorem ipsum dolor sit", added = true),
        PostEntity("Hi", "This is the first post", added = true),
    )
        @VisibleForTesting get
    private val postsSubject = BehaviorSubject.create<List<PostEntity>>()

    fun getPosts(): Observable<List<PostEntity>> {
        // Simulates API Call
        if (firstTime) {
            firstTime = false
            return Observable.error<List<PostEntity>>(GenericException(GENERIC_ERROR_MSG))
                .delay(DEFAULT_DELAY_TIME, TimeUnit.SECONDS)
        }

        postsSubject.onNext(posts)
        return postsSubject.delay(DEFAULT_DELAY_TIME, TimeUnit.SECONDS)
    }

    fun removePost(post: PostEntity): Observable<List<PostEntity>> {
        Timber.d("Removing post $post")
        postsSubject.onNext(posts.apply { remove(post) })
        return postsSubject.delay(DEFAULT_DELAY_TIME, TimeUnit.SECONDS)
    }

    fun addPost(post: PostEntity): Observable<List<PostEntity>> {
        Timber.d("Adding post $post")
        postsSubject.onNext(
            posts.apply {
                add(post.copy(added = true))
            },
        )
        return postsSubject.delay(DEFAULT_DELAY_TIME, TimeUnit.SECONDS)
    }

    fun searchPosts(input: String): Observable<List<PostEntity>> {
        Timber.d("Searching posts with [$input]")
        val filteredPosts = if (input.isEmpty()) {
            posts
        } else {
            posts.filter { post -> post.name.contains(input, ignoreCase = true) }
        }
        postsSubject.onNext(filteredPosts)
        return postsSubject
    }
}
