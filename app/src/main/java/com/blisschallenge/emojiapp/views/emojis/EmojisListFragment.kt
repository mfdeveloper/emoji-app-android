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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmojisListFragment : Fragment() {

    private val viewModel: EmojisListViewModel by viewModels()
    private lateinit var binding: FragmentEmojisListBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        val imageAdapter = ImageAdapter()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_emojis_list, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.recyclerEmojiList.adapter = imageAdapter

        viewModel.itemsUrls.observe(viewLifecycleOwner) { urls: List<String> ->
            imageAdapter.submitList(urls)
        }

        return binding.root
    }
}