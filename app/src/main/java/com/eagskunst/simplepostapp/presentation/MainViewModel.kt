package com.eagskunst.simplepostapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eagskunst.simplepostapp.domain.entity.PostEntity
import com.eagskunst.simplepostapp.domain.usecase.AddPostUseCase
import com.eagskunst.simplepostapp.domain.usecase.DeletePostUseCase
import com.eagskunst.simplepostapp.domain.usecase.GetPostsUseCase
import com.eagskunst.simplepostapp.domain.usecase.SearchPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val addPostUseCase: AddPostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val getPostsUseCase: GetPostsUseCase,
    private val searchPostsUseCase: SearchPostsUseCase,
) : ViewModel() {

    private val _viewState = MutableLiveData<MainViewState>(MainViewState.Loading)
    val viewState: LiveData<MainViewState>
        get() = _viewState

    init {
        getPosts()
    }

    fun getPosts() {
        _viewState.value = MainViewState.Loading
        getPostsUseCase.execute(
            params = null,
            onSuccess = this::onNewPosts,
            onError = this::onPostsError,
        )
    }

    fun addPost(name: String, description: String) {
        addPostUseCase.execute(
            params = PostEntity(name = name, description = description),
            onSuccess = this::onNewPosts,
            onError = this::onPostsError,
        )
    }

    fun deletePost(post: PostEntity) {
        deletePostUseCase.execute(
            params = post,
            onSuccess = this::onNewPosts,
            onError = this::onPostsError,
        )
    }

    fun searchPosts(input: String?) {
        searchPostsUseCase.stop()
        getPostsUseCase.stop()
        searchPostsUseCase.execute(
            params = input,
            onSuccess = this::onNewPosts,
            onError = this::onPostsError,
        )
    }

    private fun onNewPosts(posts: List<PostEntity>) {
        Timber.d("new posts: $posts")
        _viewState.value = MainViewState.Posts(posts)
    }

    private fun onPostsError(error: Throwable?) {
        _viewState.value = MainViewState.GeneralError(error?.message ?: "Unexpected error")
    }

    override fun onCleared() {
        super.onCleared()
        addPostUseCase.stop()
        deletePostUseCase.stop()
        getPostsUseCase.stop()
        searchPostsUseCase.stop()
    }
}
