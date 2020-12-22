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

class EmojisListFragment : Fragment() {

    private val viewModel: EmojisListViewModel by viewModels()
    private lateinit var binding: FragmentEmojisListBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_emojis_list, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }
}