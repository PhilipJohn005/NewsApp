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
    private var isLastPage=false
    private var isLoading=false
    private var isScrolling=false
    private var Pages=0
    private var country="us"
    private var page=1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentNewsBinding = FragmentNewsBinding.inflate(inflater, container, false)
        Log.i("my","NewsFragment")
        return fragmentNewsBinding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
        newsAdapter=(activity as MainActivity).newsAdapter

        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("selected_article",it)
            }
            findNavController().navigate(
                R.id.action_newsFragment_to_infoFragment,
                bundle
            )
        }
        initRecyclerView()
        Log.i("my","going to viewNewsList")
        viewNewsList()

        setSearchView()
    }

    private fun viewNewsList() {
        viewModel.getNewsHeadLines(country,page)
        viewModel.newsHeadlines.observe(viewLifecycleOwner,{response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let{
                        Log.i("my","response.data")
                        Log.i("my", "Articles received: ${it.articles.size}")
                        newsAdapter.differ.submitList(it.articles.toList())

                        if (it.totalResults%20==0){
                            Pages=it.totalResults/20
                        }else{
                            Pages=it.totalResults/20+1
                        }
                        isLastPage=page==Pages
                    }
                }
                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(activity,"An error occurred:$it",Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading->{
                    showProgressBar()
                }
            }
        })
    }

    private fun initRecyclerView() {
        Log.i("my","initRecyclerView")
        //newsAdapter=NewsAdapter()
        fragmentNewsBinding.rvNews.apply {
            layoutManager=LinearLayoutManager(activity)
            adapter = newsAdapter
            addOnScrollListener(this@NewsFragment.onScrollListener)
        }

    }




    private fun showProgressBar(){
        Log.i("my","showProgressBar")
        isLoading=true
        fragmentNewsBinding.progressBar.visibility=View.VISIBLE
    }

    private fun hideProgressBar(){
        Log.i("my","hideProgressBar")
        isLoading=false
        fragmentNewsBinding.progressBar.visibility=View.INVISIBLE
    }


    private val onScrollListener=object:RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager=fragmentNewsBinding.rvNews.layoutManager as LinearLayoutManager

            val sizeOfCurrentList=layoutManager.itemCount
            val visibleItems=layoutManager.childCount
            val topPosition=layoutManager.findFirstVisibleItemPosition()

            val hasReachedToEnd=topPosition+visibleItems>=sizeOfCurrentList
            val shouldPaginate=!isLoading&&!isLastPage&&hasReachedToEnd&&isScrolling
            if(shouldPaginate){
                page++
                viewModel.getNewsHeadLines(country,page)
                isScrolling=false
            }

        }
    }

    //search

    private fun setSearchView(){
        fragmentNewsBinding.svNews.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query!=null){
                    viewModel.searchNews("us",query.toString(),page)
                    viewSearchedNews()
                    return false
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                MainScope().launch {
                    delay(2000)
                    viewModel.searchNews("us",newText.toString(),page)
                    viewSearchedNews()

                }
                return false
            }
        })
        fragmentNewsBinding.svNews.setOnCloseListener(object:SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                initRecyclerView()
                viewNewsList()
                return false
            }
        })
    }


    fun viewSearchedNews(){
        viewModel.searchedNews.observe(viewLifecycleOwner,{response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let{
                        Log.i("my","response.data")
                        Log.i("my", "Articles received: ${it.articles.size}")
                        newsAdapter.differ.submitList(it.articles.toList())

                        if (it.totalResults%20==0){
                            Pages=it.totalResults/20
                        }else{
                            Pages=it.totalResults/20+1
                        }
                        isLastPage=page==Pages
                    }
                }
                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(activity,"An error occurred:$it",Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading->{
                    showProgressBar()
                }
            }
        })
    }
}

