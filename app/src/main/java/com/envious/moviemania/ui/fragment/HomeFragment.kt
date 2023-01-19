package com.envious.moviemania.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.envious.moviemania.R
import com.envious.moviemania.base.BaseFragment
import com.envious.moviemania.databinding.FragmentHomeBinding
import com.envious.moviemania.ui.adapter.GenreAdapter
import com.envious.moviemania.utils.Intent
import com.envious.moviemania.utils.State
import com.envious.moviemania.utils.ViewState

class HomeFragment :
    BaseFragment<Intent,
            State>(){
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: GenreAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) {
            invalidate(it)
        }

        setupRecyclerView()
        viewModel.onIntentReceived(Intent.GetGenres)
    }

    private fun setupRecyclerView() {
        with(binding) {
            recyclerview.setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(requireContext())
            recyclerview.layoutManager = linearLayoutManager
            recyclerview.itemAnimator = null
            adapter = GenreAdapter(this@HomeFragment)
            adapter.setHasStableIds(true)
            recyclerview.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override val layoutResourceId: Int
        get() = R.layout.fragment_home

    override fun invalidate(state: State) {
        with(binding) {
            pgProgressList.visibility = if (state.showLoading) View.VISIBLE else View.GONE
        }

        when (state.viewState) {
            ViewState.EmptyListFirstInitGenre -> {
                with(binding) {
                    errorView.visibility = View.VISIBLE
                    errorView.run {
                        setUpErrorView(
                            title = resources.getString(R.string.empty_state_title),
                            message = resources.getString(R.string.empty_state_message)
                        )
                        binding.buttonRetry.setOnClickListener {
                            viewModel.onIntentReceived(Intent.GetFavorites)
                        }
                    }
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.ErrorFirstInitGenre -> {
                with(binding) {
                    errorView.visibility = View.VISIBLE
                    errorView.run {
                        setUpErrorView()
                        binding.buttonRetry.setOnClickListener {
                            viewModel.onIntentReceived(Intent.GetTopRated)
                        }
                    }
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.SuccessFirstInitGenre -> {
                with(binding) {
                    recyclerview.visibility = View.VISIBLE
                    adapter.setData(state.listGenre)
                    errorView.visibility = View.GONE
                }
            }
        }
    }
}
