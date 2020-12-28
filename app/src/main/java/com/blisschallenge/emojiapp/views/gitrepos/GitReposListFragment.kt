package com.blisschallenge.emojiapp.views.gitrepos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.blisschallenge.emojiapp.R
import com.blisschallenge.emojiapp.databinding.FragmentGitReposListBinding

class GitReposListFragment : Fragment() {

    private val viewModel: GitReposViewModel by viewModels()
    private lateinit var binding: FragmentGitReposListBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_git_repos_list, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}