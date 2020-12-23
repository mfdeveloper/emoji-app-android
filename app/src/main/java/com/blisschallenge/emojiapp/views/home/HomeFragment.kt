package com.blisschallenge.emojiapp.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.blisschallenge.emojiapp.R
import com.blisschallenge.emojiapp.databinding.FragmentHomeBinding
import com.blisschallenge.emojiapp.helpers.DataState
import com.blisschallenge.emojiapp.models.entities.Emoji
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import dagger.hilt.android.AndroidEntryPoint

/**
 * About databinding classes not generated with "Binding" suffix
 *
 * See: [Data Binding class not generated](https://stackoverflow.com/questions/39483094/data-binding-class-not-generated)
 *
 * See also: [Generated binding classes](https://developer.android.com/topic/libraries/data-binding/generated-binding)
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

  private val viewModel: HomeViewModel by viewModels()
  private lateinit var binding: FragmentHomeBinding

  var showProgress = MutableLiveData(View.GONE)
  var showImage = MutableLiveData(View.VISIBLE)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        viewModel.navController = findNavController()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.viewModel = viewModel
        binding.home = this
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.dataState.observe(viewLifecycleOwner) {
            showProgress.value = if (it == DataState.START) View.VISIBLE else View.GONE
            showImage.value = if (it == DataState.START) View.INVISIBLE else View.VISIBLE
        }

        viewModel.emoji.observe(viewLifecycleOwner) { emoji: Emoji ->

            // TODO: See why Glide.placeholder() method doesn't work
            Glide.with(binding.root)
                .load(emoji.url)
                .centerCrop()
                .transform(CircleCrop())
                .into(binding.imageEmoji)
        }
    }
}