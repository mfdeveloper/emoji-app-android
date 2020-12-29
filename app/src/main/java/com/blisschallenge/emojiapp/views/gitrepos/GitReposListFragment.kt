package com.blisschallenge.emojiapp.views.gitrepos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.blisschallenge.emojiapp.R
import com.blisschallenge.emojiapp.databinding.FragmentGitReposListBinding
import com.blisschallenge.emojiapp.views.adapters.TextListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GitReposListFragment : Fragment() {

    private val viewModel: GitReposViewModel by viewModels()
    private val args: GitReposListFragmentArgs by navArgs()
    private lateinit var binding: FragmentGitReposListBinding
    private lateinit var textAdapter: TextListAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        textAdapter = TextListAdapter()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_git_repos_list, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerReposList.adapter = textAdapter

        setupListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.profileName.value = args.profileName
    }

    private fun setupListeners() {

        viewModel.profileName.observe(viewLifecycleOwner) { name ->
            viewModel.fetchData(profileName = name)
        }

        viewModel.itemsValues.observe(viewLifecycleOwner) { textData ->
            textAdapter.submitList(textData)
        }

    }
}