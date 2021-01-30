package com.fasdev.devloperlife.ui.fragment

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

class PostFragment: Fragment(), View.OnClickListener
{
    companion object {
        private const val TYPE_SECTION_KEY = "tsk"
        fun newInstance(typeSection: TypeSection) = PostFragment().apply {
            arguments = Bundle().apply {
                putEnum(TYPE_SECTION_KEY, typeSection)
            }
        }
    }

    private val typeSection: TypeSection
        get() {
            val defValue = TypeSection.LATEST
            return arguments?.getEnum(TYPE_SECTION_KEY, defValue) ?: defValue
        }

    private lateinit var binding: FragmentPostBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        binding = FragmentPostBinding.inflate(inflater)
        binding.fabNext.setOnClickListener(this)
        binding.fabReplay.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_next -> {
                //TODO: ADD IMPLEMENTATION
            }
            R.id.fab_replay -> {
                //TODO: ADD IMPL
            }
        }
    }
}