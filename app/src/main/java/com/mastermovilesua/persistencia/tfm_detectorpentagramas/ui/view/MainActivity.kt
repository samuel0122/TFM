package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.ImagenesCargadas
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.SaveToMediaStore
import android.content.res.Configuration;
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.fragment.app.add
import androidx.recyclerview.widget.GridLayoutManager
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters.GridSpacingItemDecoration
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters.ImagenesRecyclerGridAdapter
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters.OnGridItemClickAction
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.ActivityMainBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.toDomain
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnGridItemClickAction {

    private lateinit var binding: ActivityMainBinding

    private lateinit var booksListFragment: ImagenesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        booksListFragment = ImagenesFragment()

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(binding.mainFragment.id, booksListFragment)
        }

    }

    override fun onGridItemClickAction(gridItemId: Int) {
        navigateToViewImage(gridItemId)
    }

    private fun navigateToViewImage(imageId: Int) {
        startActivity(
            Intent(this@MainActivity, ViewImageDetailedActivity::class.java).apply {
                putExtra(ViewImageDetailedActivity.EXTRAS.EXTRA_IMAGE_ID, imageId)
            }
        )
    }
}