package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pageDetail.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.ImageManipulation
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.FragmentPageDetailBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pageDetail.viewModel.PageDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.min

@AndroidEntryPoint
class PageDetailFragment : Fragment(), MenuProvider {
    private val viewModel: PageDetailViewModel by viewModels()
    private val args: PageDetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentPageDetailBinding

    private var isEditMode: Boolean = false
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onCreate(args.pageId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPageDetailBinding.inflate(inflater)


        // binding.ivPage.transitionName = "pageTransition${args.pageId}"
        // sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.page_transition)

        binding.apply {
            // ivPage.transitionName = "pageTransition${args.pageId}"

            cvBoxesCanvas.setOnCanvasItemUpdateListener { canvasItem ->
                val boxCanvasItem = canvasItem as BoxCanvasItem
                boxCanvasItem.apply {
                    x = x.coerceIn(0f, cvBoxesCanvas.width - width)
                    y = y.coerceIn(0f, cvBoxesCanvas.height - height)
                }

                viewModel.updateBox(
                    boxCanvasItem.toDomain(
                        cvBoxesCanvas.width.toFloat(),
                        cvBoxesCanvas.height.toFloat()
                    )
                )
            }

            cvBoxesCanvas.setOnCanvasItemSelectListener { canvasItem ->
                if (isEditMode) viewModel.selectBox(canvasItem?.id)
            }

            cvBoxesCanvas.setOnCanvasItemDeleteListener { canvasItem ->
                viewModel.deleteBox(canvasItem.id)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility =
                if (isLoading) View.VISIBLE
                else View.GONE
        }

        viewModel.pageModel.observe(viewLifecycleOwner) { pageModel ->
            binding.apply {
                ivPage.setImageURI(pageModel.imageUri.toUri())
                ivPage.post {
                    cvBoxesCanvas.layoutParams = cvBoxesCanvas.layoutParams.apply {
                        val scaleWith: Double = ivPage.width.toDouble() / ivPage.drawable.intrinsicWidth
                        val scaleHeight: Double = ivPage.height.toDouble() / ivPage.drawable.intrinsicHeight
                        val scaleBoth = min(scaleWith, scaleHeight)

                        height = (ivPage.drawable.intrinsicHeight.toDouble() * scaleBoth).toInt()
                        width = (ivPage.drawable.intrinsicWidth.toDouble() * scaleBoth).toInt()
                    }

                    viewModel.getBoxes()
                }
            }
        }

        viewModel.boxesModel.observe(viewLifecycleOwner) { boxesModel ->
            binding.cvBoxesCanvas.run {
                updateCanvasItems(boxesModel.map { boxModel ->
                    boxModel.toCanvas(
                        layoutParams.width.toFloat(),
                        layoutParams.height.toFloat(),
                        requireContext()
                    )
                })
            }
        }

        viewModel.selectedBoxId.observe(viewLifecycleOwner) { selectedBoxId ->
            binding.cvBoxesCanvas.selectCanvasItem(selectedBoxId)
        }

        viewModel.isEditMode.observe(viewLifecycleOwner) { isEditMode ->
            this.isEditMode = isEditMode
            updateMenuVisibility()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_page_detail, menu)

        this.menu = menu
        updateMenuVisibility()
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_add_box -> {
                viewModel.insertNewBox()
                true
            }

            R.id.action_disable_edit_mode -> {
                viewModel.disableEditMode()
                true
            }

            R.id.action_enable_edit_mode -> {
                viewModel.enableEditMode()
                true
            }

            R.id.action_delete_page -> {
                viewModel.deletePage()
                findNavController().navigateUp()
                true
            }

            R.id.action_share_page -> {
                viewModel.pageModel.value?.let { pageModel ->
                    val shareImage = ImageManipulation.drawRectanglesOnImage(
                        requireContext(),
                        Uri.parse(pageModel.imageUri),
                        viewModel.boxesModel.value
                    )
                    val shareIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Mira esta foto!")
                        putExtra(Intent.EXTRA_TITLE, "Comparte tu imagen!")
                        putExtra(Intent.EXTRA_STREAM, shareImage)

                        type = "image/jpeg"
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    }

                    startActivity(Intent.createChooser(shareIntent, null))

                    // SaveToMediaStore.deleteImage(requireContext(), shareImage)
                }
                true
            }

            R.id.action_process_page -> {
                viewModel.processPage()
                true
            }

            else -> false
        }
    }

    private fun updateMenuVisibility() {
        menu?.setGroupVisible(R.id.group_page_edit_mode, isEditMode)
        menu?.setGroupVisible(R.id.group_page_detail, !isEditMode)
    }
}