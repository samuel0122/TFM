package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pageDetail.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ID

class PagesPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    private val pagesId = mutableListOf<ID>()

    fun submitList(newPages: List<ID>) {
        pagesId.clear()
        pagesId.addAll(newPages)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return pagesId.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = PageDetailFragment()

        fragment.arguments = Bundle().apply {
            putInt(PageDetailFragment.Keys.PAGE_ID, pagesId[position])
        }

        return fragment
    }
}