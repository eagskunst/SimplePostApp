package com.eagskunst.simplepostapp.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.eagskunst.simplepostapp.databinding.ActivityMainBinding
import com.eagskunst.simplepostapp.presentation.viewholder.postView
import com.eagskunst.simplepostapp.presentation.viewholder.searchBarView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_TEXT_KEY = "search_text"
    }

    private lateinit var binding: ActivityMainBinding
    var searchText: String = ""

    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchText = savedInstanceState?.getString(SEARCH_TEXT_KEY, "") ?: ""
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViewListeners()
        viewModel.viewState.observe(this) { state ->
            handleNewState(state)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH_TEXT_KEY, searchText)
        super.onSaveInstanceState(outState)
    }

    private fun setViewListeners() {
        with(binding) {
            srMain.setOnRefreshListener { viewModel.getPosts() }
            fabAddPost.setOnClickListener {
                NewPostFormBottomSheetFragment.show(this@MainActivity)
            }
        }
    }

    private fun handleNewState(
        state: MainViewState,
    ) {
        binding.srMain.isRefreshing = state is MainViewState.Loading
        binding.fabAddPost.isEnabled = state is MainViewState.Posts
        when (state) {
            is MainViewState.GeneralError -> {
                Snackbar.make(binding.root, state.message, Snackbar.LENGTH_SHORT).show()
            }

            MainViewState.Loading -> {
                binding.srMain.isRefreshing = true
            }

            is MainViewState.Posts -> {
                showPostsLists(state)
            }
        }
    }

    private fun showPostsLists(state: MainViewState.Posts) {
        binding.rvMain.withModels {
            searchBarView {
                id("searchBar")
                savedText(searchText)
                onImeClick { text ->
                    searchText = text
                    viewModel.searchPosts(text)
                }
            }
            state.posts.forEach { post ->
                postView {
                    id(post.id)
                    post(post)
                    onDeleteClick { _, _, _, _ ->
                        viewModel.deletePost(post)
                    }
                }
            }
        }
    }
}
