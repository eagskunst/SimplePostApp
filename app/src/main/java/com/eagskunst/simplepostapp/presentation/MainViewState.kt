package com.eagskunst.simplepostapp.presentation

import com.eagskunst.simplepostapp.domain.entity.PostEntity

sealed class MainViewState {
    object Loading : MainViewState()
    data class GeneralError(val message: String) : MainViewState()
    data class Posts(val posts: List<PostEntity>) : MainViewState()
}
