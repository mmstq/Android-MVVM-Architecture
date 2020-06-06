package com.mmstq.mduarchive.utility

import android.app.Activity
import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andrognito.flashbar.Flashbar
import com.andrognito.flashbar.anim.FlashAnim
import com.mmstq.mduarchive.R

class Util{
    companion object{
        fun getSnackBar(activity: Activity, context: Context): Flashbar.Builder{
            return Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.TOP)
                .title("Fetching latest archives")
                .backgroundColorRes(R.color.colorAccent)
                .enterAnimation(
                    FlashAnim.with(context)
                        .animateBar()
                        .accelerateDecelerate()
                        .duration(200)
                        .alpha()
                        .overshoot())
                .exitAnimation(
                    FlashAnim.with(context)
                    .animateBar()
                    .duration(400)
                    .accelerateDecelerate())
                .showProgress(Flashbar.ProgressPosition.LEFT)


        }
    }
}