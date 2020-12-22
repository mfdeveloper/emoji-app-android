package com.blisschallenge.emojiapp.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.blisschallenge.emojiapp.R
import com.blisschallenge.emojiapp.databinding.FragmentHomeBinding

/**
 * About databinding classes not generated with "Binding" suffix
 *
 * See: [Data Binding class not generated](https://stackoverflow.com/questions/39483094/data-binding-class-not-generated)
 *
 * See also: [Generated binding classes](https://developer.android.com/topic/libraries/data-binding/generated-binding)
 */
class HomeFragment : Fragment() {

  private val viewModel: HomeViewModel by viewModels()
  private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        viewModel.navController = findNavController()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}