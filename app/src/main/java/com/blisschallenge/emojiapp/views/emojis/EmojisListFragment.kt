package com.blisschallenge.emojiapp.views.emojis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.blisschallenge.emojiapp.R
import com.blisschallenge.emojiapp.databinding.FragmentEmojisListBinding
import com.blisschallenge.emojiapp.views.adapters.ImageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmojisListFragment : Fragment() {

    private val viewModel: EmojisListViewModel by viewModels()
    private lateinit var binding: FragmentEmojisListBinding
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        imageAdapter = ImageAdapter()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_emojis_list, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.recyclerEmojiList.adapter = imageAdapter

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {

        viewModel.itemsUrls.observe(viewLifecycleOwner) { urls ->
            imageAdapter.submitList(urls)
        }

        binding.swipeContainer.setOnRefreshListener {
            imageAdapter.clear()

            viewModel.fetchData {
                binding.swipeContainer.isRefreshing = false
            }
        }
    }
}