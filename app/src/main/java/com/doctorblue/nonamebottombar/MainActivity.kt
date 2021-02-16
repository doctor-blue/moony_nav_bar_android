package com.doctorblue.nonamebottombar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.doctorblue.noname_library.IndicatorPosition
import com.doctorblue.noname_library.IndicatorType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
         rg_indicator_position.setOnCheckedChangeListener { _, i ->
             if (i == rb_bottom.id) {
                 no_name_bottombar.indicatorPosition = IndicatorPosition.BOTTOM
             } else {
                 no_name_bottombar.indicatorPosition = IndicatorPosition.TOP

             }
         }

         rg_indicator_type.setOnCheckedChangeListener { _, i ->
             if (i == rb_line.id)
                 no_name_bottombar.indicatorType = IndicatorType.LINE
             else
                 no_name_bottombar.indicatorType = IndicatorType.POINT
         }
         btn_set_duration.setOnClickListener {
             no_name_bottombar.indicatorDuration=edt_duration.text.toString().toLong()

         }
         btn_show_or_hide.setOnClickListener {
             if (no_name_bottombar.isShow)
                 no_name_bottombar.hide()
             else
                 no_name_bottombar.show()
         }

     }


}