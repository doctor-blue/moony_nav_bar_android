package com.doctorblue.nonamebottombar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.doctorblue.noname_library.IndicatorPosition
import com.doctorblue.noname_library.IndicatorType
import com.doctorblue.noname_library.OnItemReselectedListener
import com.doctorblue.noname_library.OnItemSelectedListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testProperties()
        //testWithFragment()

        //default item selected
        no_name_bottombar.itemActiveIndex = 0
    }

   /* private fun testWithFragment() {
        no_name_bottombar.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelect(id: Int) {
                when (id) {
                    R.id.first_fragment -> {
                        val firstFragment = FirstFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_layout, firstFragment).commit()
                    }
                    R.id.second_fragment -> {
                        val secondFragment = SecondFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_layout, secondFragment).commit()
                    }
                    R.id.third_fragment -> {
                        val thirdFragment = ThirdFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_layout, thirdFragment).commit()
                    }
                    R.id.fourthFragment -> {
                        val fourthFragment = FourthFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_layout, fourthFragment).commit()
                    }
                }
            }

        }
        no_name_bottombar.onItemReselectedListener = object : OnItemReselectedListener {
            override fun onItemReselect(id: Int) {
                when (id) {
                    R.id.first_fragment -> {
                        //Code here
                    }
                    R.id.second_fragment -> {
                        //Code here
                    }
                    R.id.third_fragment -> {
                        //Code here
                    }
                    R.id.fourthFragment -> {
                        //Code here
                    }
                }
            }

        }
    }*/

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