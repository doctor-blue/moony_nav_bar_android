package com.devcomentry.moonynavbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import com.devcomentry.library.IndicatorPosition
import com.devcomentry.library.IndicatorType
import com.devcomentry.moonynavbar.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        testProperties()
        //testWithFragment()

//        navController = findNavController(R.id.main_fragment)
//        no_name_bottombar.setupWithNavController(navController)

    }


    override fun onSupportNavigateUp(): Boolean {
//        navController.navigateUp()
        return true
    }

    private fun testProperties() {
        binding.rgIndicatorPosition.setOnCheckedChangeListener { _, i ->
            if (i == binding.rbBottom.id) {
                binding.moonyNavBar.indicatorPosition = IndicatorPosition.BOTTOM
            } else {
                binding.moonyNavBar.indicatorPosition = IndicatorPosition.TOP

            }
        }

        binding.rgIndicatorType.setOnCheckedChangeListener { _, i ->
            if (i == binding.rbLine.id)
                binding.moonyNavBar.indicatorType = IndicatorType.LINE
            else
                binding.moonyNavBar.indicatorType = IndicatorType.POINT
        }
        binding.btnSetDuration.setOnClickListener {
            binding.moonyNavBar.indicatorDuration = binding.edtDuration.text.toString().toLong()

        }
        binding.btnShowOrHide.setOnClickListener {
            if (binding.moonyNavBar.isShow)
                binding.moonyNavBar.hide()
            else
                binding.moonyNavBar.show()
        }

    }


}