package com.example.newsclientapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsclientapp.databinding.FragmentInfoBinding
import com.example.newsclientapp.presentation.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!! // Use this to prevent memory leaks
    private lateinit var viewModel: NewsViewModel
    private val args: InfoFragmentArgs by navArgs() // Get arguments safely

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val article = args.selectedArticle // Getting the selected article

        viewModel = (activity as MainActivity).viewModel

        binding.wvInfo.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        binding.favSave.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Saved successfully", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}
