package com.fasdev.devloperlife.ui.fragment.post.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
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

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.currentPost.observe(viewLifecycleOwner) {
            it?.let {
                Glide.with(this)
                        .load(it.gifURL)
                        .transition(withCrossFade())
                        .into(binding.imagePost)

                binding.textPost.text = it.description
            }
        }

        viewModel.isEnableBackBtn.observe(viewLifecycleOwner) {
            binding.fabReplay.isEnabled = it
        }
        viewModel.getNextPost(typeSection)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_next -> {
                viewModel.getNextPost(typeSection)
            }
            R.id.fab_replay -> {
                viewModel.getBackPost(typeSection)
            }
        }
    }
}