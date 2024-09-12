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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.ImageManipulation
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.FragmentPageDetailBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BoxItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageState
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.DialogsFactory
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

            ivProcessedState.elevation = 15f
            ivProcessedState.setOnLongClickListener {
                viewModel.pageModel.value?.let { page ->
                    when (page.processState) {
                        PageState.Processing -> {
                            Snackbar.make(
                                it,
                                "Page is being processed by server.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            true
                        }

                        PageState.Processed -> {
                            Snackbar.make(
                                it,
                                "Page has been successfully processed by server.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            true
                        }

                        PageState.FailedToProcess -> {
                            Snackbar.make(
                                it,
                                "Page has failed to be processed by server.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            true
                        }

                        else -> false
                    }
                }
                false
            }

            cvBoxesCanvas.setOnCanvasItemUpdateListener { canvasItem ->
                val boxCanvasItem = canvasItem as BoxCanvasItem
                boxCanvasItem.apply {
                    x = if (width <= cvBoxesCanvas.width) x.coerceIn(0f, cvBoxesCanvas.width - width)
                    else 0f
                    y = if (height <= cvBoxesCanvas.height) y.coerceIn(0f, cvBoxesCanvas.height - height)
                    else 0f

                    if (x + width > cvBoxesCanvas.width)
                        width = cvBoxesCanvas.width - x
                    if (y + height > cvBoxesCanvas.height)
                        height = cvBoxesCanvas.height - y
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

        (activity as AppCompatActivity).supportActionBar?.title = args.title

        activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // binding.progressBar.visibility =
            //     if (isLoading) View.VISIBLE
            //     else View.GONE
        }

        viewModel.pageModel.observe(viewLifecycleOwner) { pageModel ->
            binding.apply {
                if (pageModel.processState == PageState.Processing) {
                    shimmer.apply {
                        startShimmer()
                        visibility = View.VISIBLE
                    }

                    clPageDetail.visibility = View.GONE

                    ivProcessedState.apply {
                        visibility = View.VISIBLE
                        setImageResource(R.drawable.ic_upload)
                        setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray
                            )
                        )
                    }
                } else {
                    ivProcessedState.apply {
                        when (pageModel.processState) {
                            PageState.Processed -> {
                                visibility = View.VISIBLE
                                setImageResource(R.drawable.ic_check)
                                setColorFilter(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.green
                                    )
                                )
                            }

                            PageState.FailedToProcess -> {
                                visibility = View.VISIBLE
                                setImageResource(R.drawable.ic_close)
                                setColorFilter(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.red
                                    )
                                )
                            }

                            else -> {
                                visibility = View.GONE
                            }
                        }
                    }

                    clPageDetail.visibility = View.VISIBLE

                    shimmer.apply {
                        visibility = View.GONE
                        stopShimmer()
                    }
                }

                ivPageShimmer.setImageURI(pageModel.imageUri.toUri())
                ivPage.setImageURI(pageModel.imageUri.toUri())
                ivPage.post {
                    alignCanvasToImage()

                    viewModel.boxesModel.value?.let { boxesModel ->
                        updateCanvasItems(boxesModel)
                    }
                }
            }
        }

        viewModel.boxesModel.observe(viewLifecycleOwner) { boxesModel ->
            updateCanvasItems(boxesModel)
        }

        viewModel.selectedBoxId.observe(viewLifecycleOwner) { selectedBoxId ->
            binding.cvBoxesCanvas.selectCanvasItem(selectedBoxId)
        }

        viewModel.isEditMode.observe(viewLifecycleOwner) { isEditMode ->
            this.isEditMode = isEditMode
            updateMenuVisibility()
        }

        viewModel.isPageDeleted.observe(viewLifecycleOwner) { isPageDeleted ->
            if (isPageDeleted) findNavController().navigateUp()
        }
    }

    private fun alignCanvasToImage() {
        binding.apply {
            cvBoxesCanvas.layoutParams = cvBoxesCanvas.layoutParams.apply {
                val scaleWith: Double =
                    ivPage.width.toDouble() / ivPage.drawable.intrinsicWidth
                val scaleHeight: Double =
                    ivPage.height.toDouble() / ivPage.drawable.intrinsicHeight
                val scaleBoth = min(scaleWith, scaleHeight)

                height = (ivPage.drawable.intrinsicHeight.toDouble() * scaleBoth).toInt()
                width = (ivPage.drawable.intrinsicWidth.toDouble() * scaleBoth).toInt()
            }
        }
    }

    private fun updateCanvasItems(boxesModel: List<BoxItem>) {
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
                confirmPageDelete()
                true
            }

            R.id.action_share_page -> {
                sharePage()
                true
            }

            R.id.action_process_page -> {
                confirmProcessPage()
                true
            }

            else -> false
        }
    }

    private fun updateMenuVisibility() {
        menu?.setGroupVisible(R.id.group_page_edit_mode, isEditMode)
        menu?.setGroupVisible(R.id.group_page_detail, !isEditMode)
    }

    private fun confirmPageDelete() {
        DialogsFactory.confirmationDialog(
            context = requireContext(),
            title = "Confirm delete page",
            question = "Are you sure you want to delete current page?",
            onConfirmAction = { viewModel.deletePage() },
            onCancelAction = { dialog -> dialog.dismiss() }
        )
    }

    private fun sharePage() {
        viewModel.pageModel.value?.let { pageModel ->
            val shareImage = ImageManipulation.drawRectanglesOnImage(
                requireContext(),
                Uri.parse(pageModel.imageUri),
                viewModel.boxesModel.value
            )
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Look at the staffs of this music sheet image!")
                putExtra(Intent.EXTRA_TITLE, "Share your page.")
                putExtra(Intent.EXTRA_STREAM, shareImage)

                type = "image/jpeg"
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            startActivity(Intent.createChooser(shareIntent, null))
        }
    }

    private fun confirmProcessPage() {
        DialogsFactory.confirmationDialog(
            context = requireContext(),
            title = "Confirm process page",
            question = "Are you sure you want to current page to process? Current bounding boxes will be override by new ones.",
            onConfirmAction = { viewModel.processPage() },
            onCancelAction = { dialog -> dialog.dismiss() }
        )
    }
}