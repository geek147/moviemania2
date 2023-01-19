package com.envious.moviemania.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import com.envious.data.BuildConfig
import com.envious.domain.model.Movie
import com.envious.moviemania.R
import com.envious.moviemania.base.BaseFragment
import com.envious.moviemania.databinding.FragmentDetailBinding
import com.envious.moviemania.ui.adapter.UserReviewAdapter
import com.envious.moviemania.utils.BindingConverters
import com.envious.moviemania.utils.Intent
import com.envious.moviemania.utils.State
import com.envious.moviemania.utils.ViewState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class DetailFragment :
    BaseFragment<Intent,
            State>(){
    companion object {
        val TAG = this::class.simpleName
        const val EXTRA_USER_DETAIL = "EXTRA_USER_DETAIL:"

        @JvmStatic
        fun create(
            movie: Movie
        ): DetailFragment {
            val fragment = DetailFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(EXTRA_USER_DETAIL, movie)
            }

            return fragment
        }
    }


    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    var movie: Movie? = null
    var youTubePlayerView: YouTubePlayerView? = null

    private lateinit var adapter: UserReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        movie = arguments?.getParcelable(EXTRA_USER_DETAIL)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.state.observe(viewLifecycleOwner) {
            invalidate(it)
        }

        with(binding) {
            youTubePlayerView = ytPlayer
            lifecycle.addObserver(youTubePlayerView!!)
        }


        setupRecyclerView()
        setupUIData()
        viewModel.onIntentReceived(Intent.GetMovieVideo(movie?.id?:13))
        viewModel.onIntentReceived(Intent.GetUserReview(movie?.id?:12))
    }

    private fun setupRecyclerView() {
        with(binding) {
            recyclerview.setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(requireContext())
            recyclerview.layoutManager = linearLayoutManager
            recyclerview.itemAnimator = null
            adapter = UserReviewAdapter(requireContext())
            adapter.setHasStableIds(true)
            recyclerview.adapter = adapter
        }
    }

    private fun setupUIData () {

        activity?.title = movie?.title.orEmpty()

        with(binding) {
            tvTitle.text = movie?.title.orEmpty()
            tvOverview.text = movie?.overview.orEmpty()
            tvReleaseDate.text = movie?.releaseDate.orEmpty()
            BindingConverters.loadImage(ivPoster, BuildConfig.IMAGE_URL + movie?.posterPath)
            BindingConverters.loadImage(ivBackdrop, BuildConfig.BACKDROP_URL + movie?.backdropPath)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override val layoutResourceId: Int
        get() = R.layout.fragment_detail

    override fun invalidate(state: State) {
        with(binding) {
            pgProgressList.visibility = if (state.showLoading) View.VISIBLE else View.GONE
        }

        when (state.viewState) {
            ViewState.EmptyListFirstInitUserReview -> {
                with(binding) {
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.ErrorFirstInitUserReview -> {
                with(binding) {
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.SuccessFirstInitUserReview -> {
                with(binding) {
                    recyclerview.visibility = View.VISIBLE
                    adapter.setData(state.listUserReview)
                }
            }
            ViewState.EmptyListFirstInitMovieVideo -> {
                with(binding) {

                }
            }
            ViewState.ErrorFirstInitMovieVideo-> {
            }
            ViewState.SuccessFirstInitMovieVideo -> {
                with(binding) {
                    youTubePlayerView?.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                            val videoId = state.listMovieVideo[0].key
                            youTubePlayer.loadVideo(videoId, 0f)
                        }
                    })
                }
            }
            ViewState.SuccessLoadMoreUserReview -> {
                with(binding) {
                    recyclerview.visibility = View.VISIBLE
                    adapter.addData(state.listUserReview)
                }
            }
            ViewState.EmptyListLoadMoreUserReview-> {
                Toast.makeText(requireContext(), "new list is empty", Toast.LENGTH_SHORT).show()
            }
            ViewState.ErrorLoadMoreUserReview -> {
                with(binding) {
                    recyclerview.visibility = View.VISIBLE
                }
            }
        }
    }
}
