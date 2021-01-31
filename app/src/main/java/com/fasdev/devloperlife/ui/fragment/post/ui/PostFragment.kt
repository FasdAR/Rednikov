package com.fasdev.devloperlife.ui.fragment.post.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.fasdev.devlife.core.common.model.TypeSection
import com.fasdev.devloperlife.R
import com.fasdev.devloperlife.app.util.getEnum
import com.fasdev.devloperlife.app.util.putEnum
import com.fasdev.devloperlife.databinding.FragmentPostBinding
import com.fasdev.devloperlife.ui.fragment.post.viewModel.PostEvent
import com.fasdev.devloperlife.ui.fragment.post.viewModel.PostState
import com.fasdev.devloperlife.ui.fragment.post.viewModel.PostViewModel
import com.fasdev.devloperlife.ui.mvi.MviView
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class PostFragment: Fragment(), DIAware, View.OnClickListener, MviView<PostState>
{
    companion object {
        private const val TYPE_SECTION_KEY = "tsk"
        fun newInstance(typeSection: TypeSection) = PostFragment().apply {
            arguments = Bundle().apply {
                putEnum(TYPE_SECTION_KEY, typeSection)
            }
        }
    }

    override val di: DI by di()

    private val typeSection: TypeSection
        get() {
            val defValue = TypeSection.LATEST
            return arguments?.getEnum(TYPE_SECTION_KEY, defValue) ?: defValue
        }

    private lateinit var binding: FragmentPostBinding
    private val viewModel: PostViewModel by instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        binding = FragmentPostBinding.inflate(inflater)

        binding.fabNext.setOnClickListener(this)
        binding.fabReplay.setOnClickListener(this)
        binding.repeatBtn.setOnClickListener(this)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.handleEvent(PostEvent.SetTypeSection(typeSection))
    }

    private fun setVisibleMainLayout(isVisible: Boolean) {
        binding.fabReplay.isVisible = isVisible
        binding.fabNext.isVisible = isVisible
        binding.postCard.isVisible = isVisible
    }

    private fun setVisibleExHostLayout(isVisible: Boolean) {
        binding.exHost.isVisible = isVisible
    }

    private fun setVisibleLoadedLayout(isVisible: Boolean) {
        binding.progressBar.isVisible = isVisible
    }

    private fun setVisibleNullLayout(isVisible: Boolean) {
        binding.nullPost.isVisible = isVisible
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_next -> {
                viewModel.handleEvent(PostEvent.GetNextPost)
            }
            R.id.fab_replay -> {
                viewModel.handleEvent(PostEvent.GetBackPost)
            }
            R.id.repeat_btn -> {
                viewModel.handleEvent(PostEvent.ReloadPost)
            }
        }
    }

    override fun render(state: PostState)
    {
        when (state) {
            PostState.UnknownHost -> {
                setVisibleMainLayout(false)
                setVisibleNullLayout(false)
                setVisibleLoadedLayout(false)

                setVisibleExHostLayout(true)
            }
            PostState.NullPost -> {
                setVisibleMainLayout(false)
                setVisibleExHostLayout(false)
                setVisibleLoadedLayout(false)

                setVisibleNullLayout(true)
            }
            is PostState.PostInfo -> {
                setVisibleMainLayout(true)
                setVisibleExHostLayout(false)
                setVisibleNullLayout(false)

                Log.d("STATE_POST", state.isLoaded.toString() + " " + state.isLoadedImage.toString())

                if (!state.isLoadedImage && !state.gifUrl.isNullOrEmpty()) {
                    Glide.with(this)
                            .load(state.gifUrl)
                            .addListener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(e: GlideException?, model: Any?,
                                                          target: Target<Drawable>?,
                                                          isFirstResource: Boolean): Boolean
                                {
                                    viewModel.handleEvent(PostEvent.ImageErrorLoad)
                                    return false
                                }

                                override fun onResourceReady(resource: Drawable?, model: Any?,
                                                             target: Target<Drawable>?,
                                                             dataSource: DataSource?,
                                                             isFirstResource: Boolean): Boolean
                                {
                                    viewModel.handleEvent(PostEvent.ImageLoaded)
                                    return false
                                }
                            })
                            .transition(withCrossFade())
                            .into(binding.imagePost)
                }

                binding.fabReplay.isEnabled = !state.isLatest

                val isLoaded = state.isLoaded && state.isLoadedImage

                if (isLoaded) {
                    binding.textPost.text = state.description
                    setVisibleLoadedLayout(false)
                }
                else {
                    binding.textPost.text = ""
                    setVisibleLoadedLayout(true)
                }
            }
        }
    }
}