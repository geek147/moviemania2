package com.envious.moviemania.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.envious.moviemania.R
import com.envious.moviemania.base.BaseFragment
import com.envious.moviemania.databinding.FragmentListMovieBinding
import com.envious.moviemania.ui.adapter.MovieAdapter
import com.envious.moviemania.utils.EndlessRecyclerViewScrollListener
import com.envious.moviemania.utils.Intent
import com.envious.moviemania.utils.State
import com.envious.moviemania.utils.ViewState

class ListMovieFragment :
    BaseFragment<Intent,
            State>() {

    companion object {
        val TAG = this::class.simpleName
        const val EXTRA_USER_LIST_MOVIE = "EXTRA_USER_LIST_MOVIE"

        @JvmStatic
        fun create(
            withGenre: Int
        ): ListMovieFragment {
            val fragment = ListMovieFragment()
            fragment.arguments = Bundle().apply {
                putInt(EXTRA_USER_LIST_MOVIE, withGenre)
            }

            return fragment
        }
    }

    private var _binding: FragmentListMovieBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MovieAdapter

    var withGenre   : Int? = null

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListMovieBinding.inflate(inflater, container, false)
        withGenre = arguments?.getInt(EXTRA_USER_LIST_MOVIE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) {
            invalidate(it)
        }

        setupRecyclerView()
        viewModel.onIntentReceived(Intent.GetMovieByGenre(withGenre = withGenre?:27))
    }

    private fun setupRecyclerView() {
        with(binding) {
            recyclerview.setHasFixedSize(true)
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            recyclerview.layoutManager = gridLayoutManager
            recyclerview.itemAnimator = null
            adapter = MovieAdapter(this@ListMovieFragment)
            adapter.setHasStableIds(true)
            recyclerview.adapter = adapter
            scrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
                override fun onLoadMore(
                    page: Int,
                    totalItemsCount: Int,
                    view: RecyclerView?,
                ) {
                    currentPage = page + 1
                    viewModel.onIntentReceived(Intent.LoadNextPopular(currentPage))
                }
            }
            recyclerview.addOnScrollListener(scrollListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override val layoutResourceId: Int
        get() = R.layout.fragment_popular

    override fun invalidate(state: State) {
        with(binding) {
            pgProgressList.visibility = if (state.showLoading) View.VISIBLE else View.GONE
        }

        when (state.viewState) {
            ViewState.EmptyListFirstInitMovieByGenre -> {
                with(binding) {
                    errorView.visibility = View.VISIBLE
                    errorView.run {
                        setUpErrorView(
                            title = resources.getString(R.string.empty_state_title),
                            message = resources.getString(R.string.empty_state_message)
                        )
                        binding.buttonRetry.setOnClickListener {
                            currentPage = 1
                            viewModel.onIntentReceived(Intent.GetPopular)
                        }
                    }
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.ErrorFirstInitMovieByGenre -> {
                with(binding) {
                    errorView.visibility = View.VISIBLE
                    errorView.run {
                        setUpErrorView()
                        binding.buttonRetry.setOnClickListener {
                            currentPage = 1
                            viewModel.onIntentReceived(Intent.GetPopular)
                        }
                    }
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.ErrorLoadMoreMovieByGenre -> {
                with(binding) {
                    recyclerview.visibility = View.VISIBLE
                }
            }
            ViewState.Idle -> {}
            ViewState.SuccessFirstInitMovieByGenre -> {
                with(binding) {
                    recyclerview.visibility = View.VISIBLE
                    adapter.setData(state.listMovieByGenre)
                    errorView.visibility = View.GONE
                }
            }
            ViewState.SuccessLoadMoreMovieByGenre -> {
                with(binding) {
                    recyclerview.visibility = View.VISIBLE
                    adapter.addData(state.listMovieByGenre)
                    errorView.visibility = View.GONE
                }
            }
            ViewState.EmptyListLoadMoreMovieByGenre-> {
                Toast.makeText(requireContext(), "new list is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
