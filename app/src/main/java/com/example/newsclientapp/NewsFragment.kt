package com.example.newsclientapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsclientapp.data.util.Resource
import com.example.newsclientapp.databinding.FragmentNewsBinding
import com.example.newsclientapp.presentation.adapter.NewsAdapter
import com.example.newsclientapp.presentation.viewmodel.NewsViewModel
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewsFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var fragmentNewsBinding: FragmentNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    private var isLastPage = false
    private var isLoading = false
    private var isScrolling = false
    private var Pages = 0
    private var country = "us"
    private var page = 1 // Tracks the current page for pagination

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentNewsBinding = FragmentNewsBinding.inflate(inflater, container, false)
        return fragmentNewsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        newsAdapter = (activity as MainActivity).newsAdapter

        // Set item click listener
        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putParcelable("selected_article", article)
            }
            findNavController().navigate(
                R.id.action_newsFragment_to_infoFragment,
                bundle
            )
        }

        // Initialize RecyclerView
        initRecyclerView()

        // Fetch the first page of news headlines
        page = 1 // Reset page to 1 when the fragment is created or recreated
        viewNewsList()

        // Set up search view
        setSearchView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.searchedNews.removeObservers(viewLifecycleOwner)
    }

    private fun viewNewsList() {
        viewModel.getNewsHeadLines(country, page)
        viewModel.newsHeadlines.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        // If it's the first page, replace the list; otherwise, append to the existing list
                        val newList = if (page == 1) {
                            it.articles
                        } else {
                            val oldList = newsAdapter.differ.currentList.toMutableList()
                            val newArticles = it.articles.filter { article -> article !in oldList }
                            oldList.addAll(newArticles)
                            oldList
                        }

                        newsAdapter.differ.submitList(newList)

                        // Calculate total pages
                        if (it.totalResults % 20 == 0) {
                            Pages = it.totalResults / 20
                        } else {
                            Pages = it.totalResults / 20 + 1
                        }
                        isLastPage = page == Pages
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(activity, "An error occurred: $it", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun initRecyclerView() {
        fragmentNewsBinding.rvNews.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = newsAdapter
            addOnScrollListener(this@NewsFragment.onScrollListener)
        }
    }

    private fun showProgressBar() {
        isLoading = true
        fragmentNewsBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        isLoading = false
        fragmentNewsBinding.progressBar.visibility = View.INVISIBLE
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = fragmentNewsBinding.rvNews.layoutManager as LinearLayoutManager
            val sizeOfCurrentList = layoutManager.itemCount
            val visibleItems = layoutManager.childCount
            val topPosition = layoutManager.findFirstVisibleItemPosition()

            val hasReachedToEnd = topPosition + visibleItems >= sizeOfCurrentList
            val shouldPaginate = !isLoading && !isLastPage && hasReachedToEnd && isScrolling
            if (shouldPaginate) {
                page++
                viewModel.getNewsHeadLines(country, page)
                isScrolling = false
            }
        }
    }

    private fun setSearchView() {
        fragmentNewsBinding.svNews.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    page = 1  // Reset pagination for search
                    newsAdapter.differ.submitList(emptyList()) // Clear current list
                    viewModel.searchNews(country, query, page)
                    observeSearchedNews()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                MainScope().launch {
                    delay(500) // Add a small delay to avoid too many API calls
                    if (newText.isNullOrEmpty()) {
                        // Reset to normal news when search is empty
                        page = 1
                        viewModel.getNewsHeadLines(country, page)
                        viewNewsList()
                    } else {
                        page = 1  // Reset pagination for search
                        newsAdapter.differ.submitList(emptyList()) // Clear current list
                        viewModel.searchNews(country, newText, page)
                        observeSearchedNews()
                    }
                }
                return false
            }
        })

        fragmentNewsBinding.svNews.setOnCloseListener {
            // Reset to normal news when search is closed
            page = 1
            viewModel.getNewsHeadLines(country, page)
            viewNewsList()
            false
        }
    }

    private fun observeSearchedNews() {
        viewModel.searchedNews.removeObservers(viewLifecycleOwner) // Remove old observer
        viewModel.searchedNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles) // Directly update the list

                        if (it.totalResults % 20 == 0) {
                            Pages = it.totalResults / 20
                        } else {
                            Pages = it.totalResults / 20 + 1
                        }
                        isLastPage = page == Pages
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(activity, "An error occurred: $it", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }
}