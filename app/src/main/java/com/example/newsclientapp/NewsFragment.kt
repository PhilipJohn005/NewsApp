package com.example.newsclientapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsclientapp.data.util.Resource
import com.example.newsclientapp.databinding.FragmentNewsBinding
import com.example.newsclientapp.presentation.adapter.NewsAdapter
import com.example.newsclientapp.presentation.viewmodel.NewsViewModel

class NewsFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var fragmentNewsBinding: FragmentNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    private var country="us"
    private var page=1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
        initRecyclerView()
        viewNewsList()
    }

    private fun viewNewsList() {
        viewModel.getNewsHeadLines(country,page)
        viewModel.newsHeadlines.observe(viewLifecycleOwner,{response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let{
                        newsAdapter.differ.submitList(it.articles.toList())
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
        newsAdapter=NewsAdapter()
        fragmentNewsBinding.rvNews.apply {
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)
        }
    }

    private fun showProgressBar(){
        fragmentNewsBinding.progressBar.visibility=View.VISIBLE
    }

    private fun hideProgressBar(){
        fragmentNewsBinding.progressBar.visibility=View.INVISIBLE
    }
}