package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pageDetail.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
class PageDetailFragment : Fragment(), IPageDetailView {
    private val viewModel: PageDetailViewModel by viewModels()

    private var binding: FragmentPageDetailBinding? = null

    private var isEditMode: Boolean = false
    private var isViewIdleVisible: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pageId = arguments?.getInt(Keys.PAGE_ID)!!

        viewModel.onCreate(pageId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPageDetailBinding.inflate(inflater)

        // binding.ivPage.transitionName = "pageTransition${args.pageId}"
        // sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.page_transition)

        binding?.apply {
            // ivPage.transitionName = "pageTransition${args.pageId}"

            ivProcessedState.elevation = 15f
            ivProcessedState.setOnLongClickListener {
                viewModel.pageModel.value?.let { page ->
                    Snackbar.make(
                        it,
                        when (page.processState) {
                            PageState.Processing -> R.string.page_is_being_processed
                            PageState.Processed -> R.string.page_has_been_processed
                            PageState.FailedToProcess -> R.string.page_has_failed_to_process
                            PageState.NotProcessed -> R.string.page_has_not_been_processed
                        },
                        Snackbar.LENGTH_SHORT
                    ).show()
                    true
                }
                false
            }

            cvBoxesCanvas.setOnCanvasItemUpdateListener { canvasItem ->
                val boxCanvasItem = canvasItem as BoxCanvasItem
                boxCanvasItem.apply {
                    x = if (width <= cvBoxesCanvas.width)
                        x.coerceIn(0f, cvBoxesCanvas.width - width)
                    else 0f
                    y = if (height <= cvBoxesCanvas.height)
                        y.coerceIn(0f, cvBoxesCanvas.height - height)
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

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // binding.progressBar.visibility =
            //     if (isLoading) View.VISIBLE
            //     else View.GONE
        }

        viewModel.pageModel.observe(viewLifecycleOwner) { pageModel ->
            binding?.apply {
                updateProcessingState(pageModel.processState)
                updateCanvasVisibility()

                ivPageShimmer.setImageURI(pageModel.imageUri.toUri())
                ivPage.apply {
                    setImageURI(pageModel.imageUri.toUri())
                    post {
                        alignCanvasToImage()

                        viewModel.boxesModel.value?.let { boxesModel ->
                            updateCanvasItems(boxesModel)
                        }
                    }
                }
            }
        }

        viewModel.boxesModel.observe(viewLifecycleOwner) { boxesModel ->
            updateCanvasItems(boxesModel)
        }

        viewModel.selectedBoxId.observe(viewLifecycleOwner) { selectedBoxId ->
            binding?.apply { cvBoxesCanvas.selectCanvasItem(selectedBoxId) }
        }

        viewModel.isEditMode.observe(viewLifecycleOwner) { isEditMode ->
            this.isEditMode = isEditMode
        }

        viewModel.isPageDeleted.observe(viewLifecycleOwner) { isPageDeleted ->
            if (isPageDeleted) findNavController().navigateUp()
        }
    }

    private fun updateProcessingState(processState: PageState) {
        binding?.apply {
            if (processState == PageState.Processing) {
                shimmer.apply {
                    startShimmer()
                    visibility = View.VISIBLE
                }


                clPageDetail.apply {
                    visibility = View.GONE
                }

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
                    visibility = when (processState) {
                        PageState.Processed, PageState.FailedToProcess -> View.VISIBLE
                        else -> View.GONE
                    }

                    setImageResource(
                        when (processState) {
                            PageState.Processed -> R.drawable.ic_check
                            PageState.FailedToProcess -> R.drawable.ic_close
                            else -> R.drawable.ic_close
                        }
                    )

                    setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            when (processState) {
                                PageState.Processed -> R.color.green
                                PageState.FailedToProcess -> R.color.red
                                else -> R.color.gray
                            }
                        )
                    )
                }

                clPageDetail.apply {
                    visibility = View.VISIBLE
                }

                shimmer.apply {
                    visibility = View.GONE
                    stopShimmer()
                }
            }
        }
    }

    private fun alignCanvasToImage() {
        binding?.apply {
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
        binding?.cvBoxesCanvas?.run {
            updateCanvasItems(boxesModel.map { boxModel ->
                boxModel.toCanvas(
                    layoutParams.width.toFloat(),
                    layoutParams.height.toFloat(),
                    requireContext()
                )
            })
        }
    }

    private fun updateCanvasVisibility() {
        binding?.apply {
            cvBoxesCanvas.visibility =
                if (isViewIdleVisible && viewModel.pageModel.value?.processState == PageState.Processed) View.VISIBLE else View.GONE
        }
    }

    override fun setEditMode(isEditMode: Boolean) {
        if (isEditMode) {
            viewModel.enableEditMode()
        } else {
            viewModel.disableEditMode()
        }
    }

    override fun actionAddBox() {
        viewModel.insertNewBox()
    }

    override fun actionDeletePage() {
        confirmPageDelete()
    }

    override fun actionSharePage() {
        sharePage()
    }

    override fun actionProcessPage() {
        confirmProcessPage()
    }

    override fun setDisplayCanvas(display: Boolean) {
        isViewIdleVisible = display
        updateCanvasVisibility()
    }

    private fun confirmPageDelete() {
        DialogsFactory.confirmationDialog(
            context = requireContext(),
            title = getString(R.string.confirm_delete_page_title),
            question = getString(R.string.confirm_delete_page),
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
                putExtra(Intent.EXTRA_TEXT, getString(R.string.page_share_message))
                putExtra(Intent.EXTRA_TITLE, getString(R.string.page_share_title))
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
            title = getString(R.string.confirm_process_page_title),
            question = getString(R.string.confirm_process_page),
            onConfirmAction = { viewModel.processPage() },
            onCancelAction = { dialog -> dialog.dismiss() }
        )
    }

    companion object Keys {
        const val PAGE_ID = "pageId"
    }
}