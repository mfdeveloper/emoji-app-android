package com.blisschallenge.emojiapp.views.avatars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.blisschallenge.emojiapp.R
import com.blisschallenge.emojiapp.databinding.FragmentAvatarsListBinding
import com.blisschallenge.emojiapp.models.entities.ProfileInfo
import com.blisschallenge.emojiapp.views.adapters.ImageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvatarsListFragment : Fragment() {

    private val viewModel: AvatarsListViewModel by viewModels()
    private lateinit var binding: FragmentAvatarsListBinding
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        imageAdapter = ImageAdapter()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_avatars_list, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerAvatarsList.adapter = imageAdapter

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {

        viewModel.itemsUrls.observe(viewLifecycleOwner) { urls ->
            imageAdapter.submitList(urls)
        }

        imageAdapter.onItemRemoved = { position, _ ->

            val profile: ProfileInfo? = viewModel.dataState.value?.data?.get(position)
            if (profile != null) {
                viewModel.remove(profile)
            }
        }
    }
}