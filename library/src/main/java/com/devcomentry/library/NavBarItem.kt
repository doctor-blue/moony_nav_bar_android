package com.devcomentry.library

import android.graphics.RectF
import android.graphics.drawable.Drawable
/**
 * Create by Nguyen Van Tan 7/2020
 * */
data class NavBarItem (
    var id:Int,
    var title: String,
    val icon: Drawable,
    var rect: RectF = RectF(),
    var alpha: Int
)