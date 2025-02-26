package com.example.newsclientapp.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsclientapp.data.model.Article
import com.example.newsclientapp.databinding.NewsListItemBinding

class NewsAdapter():RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(){

    private val callback=object:DiffUtil.ItemCallback<Article>(){

        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {  //first this is checked to see if item is old or new by url
            Log.i("my","DiffUtil")
            return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {  //if item is same then content is checked to see if it is same..so bcs of the first check we know that we which we are updating and which we are adding
            return oldItem==newItem
        }
    }

    val differ=AsyncListDiffer(this,callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        Log.i("my","onCreateViewHolder")
        val binding=NewsListItemBinding
            .inflate(LayoutInflater.from(parent.context),parent,false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        Log.i("my","getItemCount")
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        Log.i("my", "Current list size in adapter: ${differ.currentList.size}")
        val article=differ.currentList[position]
        holder.bind(article)
    }



    inner class NewsViewHolder(private val binding:NewsListItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(article: Article){
            Log.i("my","bind")
            binding.tvTitle.text=article.title
            binding.tvDescription.text=article.description
            binding.tvSource.text=article.source?.name
            binding.tvPublishedAt.text=article.publishedAt
            Glide.with(binding.ivArticleImage.context)
                .load(article.urlToImage)
                .into(binding.ivArticleImage)

            binding.root.setOnClickListener{
                onItemClickListener?.let{
                    it(article)
                }
            }
        }
    }

    private var onItemClickListener:((Article)->Unit)?=null

    fun setOnItemClickListener(listener:(Article)->Unit){
        onItemClickListener=listener
    }
}