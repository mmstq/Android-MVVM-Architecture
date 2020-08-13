package com.mmstq.mduarchive.utility

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.View
import com.andrognito.flashbar.Flashbar
import com.andrognito.flashbar.anim.FlashAnim
import com.mmstq.mduarchive.R

class Util {
    companion object{
        fun getSnackBar(activity: Activity, context: Context): Flashbar.Builder{
            return Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.TOP)
                .title("Fetching latest archives")
                .backgroundColorRes(R.color.darkGreen)
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

        fun pressAnimation(view: View){

            view.animate().setDuration(100).scaleX(0.85f).scaleY(0.85f).withEndAction {
                view.animate().setDuration(100).scaleX(1f).scaleY(1f)

            }.start()


        }

    }

}