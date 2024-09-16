package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pageDetail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.FragmentPagesPagerBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pageDetail.viewModel.PagesPagerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PagesPagerFragment : Fragment(), MenuProvider {
    private val viewModel: PagesPagerViewModel by viewModels()
    private val args: PagesPagerFragmentArgs by navArgs()

    private lateinit var binding: FragmentPagesPagerBinding
    private lateinit var pagerAdapter: PagesPagerAdapter

    private var isEditMode: Boolean = false
    private var menu: Menu? = null

    private var currentIndex: Int? = null

    private val currentPageDetailView: IPageDetailView?
        get() = childFragmentManager.findFragmentByTag("f${binding.viewPager.currentItem}") as? IPageDetailView

    private val previousPageDetailView: IPageDetailView?
        get() = childFragmentManager.findFragmentByTag("f${binding.viewPager.currentItem - 1}") as? IPageDetailView

    private val nextPageDetailView: IPageDetailView?
        get() = childFragmentManager.findFragmentByTag("f${binding.viewPager.currentItem + 1}") as? IPageDetailView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onCreate(args.bookId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPagesPagerBinding.inflate(inflater, container, false)

        pagerAdapter = PagesPagerAdapter(this)

        binding.viewPager.apply {
            adapter = pagerAdapter
            offscreenPageLimit = 2
            isSaveEnabled = false

            registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback(
                ) {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        updatePageIndex()
                        currentIndex = position
                    }

                    override fun onPageScrollStateChanged(state: Int) {
                        super.onPageScrollStateChanged(state)
                        when (state) {
                            ViewPager2.SCROLL_STATE_IDLE -> {
                                currentPageDetailView?.setDisplayCanvas(true)
                            }

                            else -> {
                                previousPageDetailView?.setDisplayCanvas(false)
                                currentPageDetailView?.setDisplayCanvas(false)
                                nextPageDetailView?.setDisplayCanvas(false)
                            }
                        }
                    }
                }
            )
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(Keys.CURRENT_ITEM_INDEX, binding.viewPager.currentItem)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = args.title

        activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        savedInstanceState?.let {
            currentIndex = it.getInt(Keys.CURRENT_ITEM_INDEX)
        }

        viewModel.pages.observe(viewLifecycleOwner) { pages ->
            pagerAdapter.submitList(pages)

            if (currentIndex == null) {
                currentIndex = pages.indexOf(args.pageId)
            }

            binding.viewPager.setCurrentItem(currentIndex!!, false)

            updatePageIndex()
        }

        viewModel.isEditMode.observe(viewLifecycleOwner) { isEditMode ->
            currentPageDetailView?.let { currentPageView ->
                this.isEditMode = isEditMode

                currentPageView.setEditMode(isEditMode)

                binding.viewPager.isUserInputEnabled = isEditMode.not()
                updateMenuVisibility()
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_page_detail, menu)

        this.menu = menu
        updateMenuVisibility()
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return currentPageDetailView?.let { currentPageDetail ->
            when (menuItem.itemId) {
                R.id.action_add_box -> {
                    currentPageDetail.actionAddBox()
                    true
                }

                R.id.action_disable_edit_mode -> {
                    viewModel.setEditMode(false)
                    true
                }

                R.id.action_enable_edit_mode -> {
                    viewModel.setEditMode(true)
                    true
                }

                R.id.action_delete_page -> {
                    currentPageDetail.actionDeletePage()
                    true
                }

                R.id.action_share_page -> {
                    currentPageDetail.actionSharePage()
                    true
                }

                R.id.action_process_page -> {
                    currentPageDetail.actionProcessPage()
                    true
                }

                else -> false
            }
        } ?: false
    }

    private fun updateMenuVisibility() {
        menu?.setGroupVisible(R.id.group_page_edit_mode, isEditMode)
        menu?.setGroupVisible(R.id.group_page_detail, !isEditMode)
    }

    private fun updatePageIndex() {
        binding.apply {
            tvPageIndex.text = "${viewPager.currentItem + 1}/${viewModel.pages.value?.size}"
        }
    }

    private companion object Keys {
        const val CURRENT_ITEM_INDEX = "currentItem"
    }
}