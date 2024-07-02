package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters.OnGridItemClickAction
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnGridItemClickAction {

    private lateinit var binding: ActivityMainBinding

    // private lateinit var booksListFragment: ImagenesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        booksListFragment = ImagenesFragment()

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(binding.mainFragment.id, booksListFragment)
        }
        */
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