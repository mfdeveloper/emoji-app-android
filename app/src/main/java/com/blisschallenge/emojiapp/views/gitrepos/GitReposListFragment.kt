package com.blisschallenge.emojiapp.views.gitrepos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.blisschallenge.emojiapp.R
import com.blisschallenge.emojiapp.databinding.FragmentGitReposListBinding
import com.blisschallenge.emojiapp.views.adapters.ReposPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GitReposListFragment : Fragment() {

    private val listViewModel: GitReposListViewModel by viewModels()
    private val args: GitReposListFragmentArgs by navArgs()
    private lateinit var binding: FragmentGitReposListBinding
    private lateinit var adapter: ReposPagingAdapter

    private var paginateJob: Job? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        adapter = ReposPagingAdapter()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_git_repos_list, container, false)

        binding.viewModel = listViewModel
        binding.lifecycleOwner = this

        setupList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupList() {
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerReposList.addItemDecoration(divider)

        binding.recyclerReposList.adapter = adapter

        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            adapter.loadStateFlow
                    // Only emit when REFRESH LoadState for RemoteMediator changes.
                    .distinctUntilChangedBy { it.refresh }
                    // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                    .filter { it.refresh is LoadState.NotLoading }
                    .collect { binding.recyclerReposList.scrollToPosition(0) }
        }
    }

    private fun setupListeners() {

        paginateJob?.cancel()
        paginateJob = viewLifecycleOwner.lifecycleScope.launch {
            listViewModel.searchReposBy(profileName = args.profileName).collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }
}