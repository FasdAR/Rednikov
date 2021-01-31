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
import com.fasdev.devloperlife.ui.fragment.post.viewModel.PostViewModel
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class PostFragment: Fragment(), DIAware, View.OnClickListener
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

        viewModel.currentPost.observe(viewLifecycleOwner) {
            setVisibleExHostLayout(false)
            it?.let {
                setVisibleMainLayout(true)
                binding.nullPost.isVisible = false

                binding.progressBar.isVisible = true
                Glide.with(this)
                        .load(it.gifURL)
                        .transition(withCrossFade())
                        .addListener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                setVisibleMainLayout(false)
                                setVisibleExHostLayout(true)
                                binding.progressBar.isVisible = false
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.progressBar.isVisible = false
                                return false
                            }
                        })
                        .into(binding.imagePost)

                binding.textPost.text = it.description
            } ?: kotlin.run {
                setVisibleMainLayout(false)
                binding.nullPost.isVisible = true
                binding.progressBar.isVisible = false
            }
        }

        viewModel.isEnableBackBtn.observe(viewLifecycleOwner) {
            binding.fabReplay.isEnabled = it
        }

        viewModel.unknownHost.observe(viewLifecycleOwner) {
            setVisibleMainLayout(false)
            setVisibleExHostLayout(it)
            binding.progressBar.isVisible = false
        }

        viewModel.isShowLoading.observe(viewLifecycleOwner) {
            if (it) {
                setVisibleMainLayout(false)
                binding.progressBar.isVisible = it
            }
        }

        viewModel.typeSection = typeSection
    }

    private fun setVisibleMainLayout(isVisible: Boolean) {
        binding.fabReplay.isVisible = isVisible
        binding.fabNext.isVisible = isVisible
        binding.postCard.isVisible = isVisible
    }

    private fun setVisibleExHostLayout(isVisible: Boolean) {
        binding.exHost.isVisible = isVisible
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_next -> {
                viewModel.getNextPost()
            }
            R.id.fab_replay -> {
                viewModel.getBackPost()
            }
            R.id.repeat_btn -> {
                //setVisibleExHostLayout(false)

                viewModel.reloadPost()
            }
        }
    }
}