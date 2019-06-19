package com.example.valueanimatorwithcircularreveal

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_bar.*
import kotlinx.android.synthetic.main.search_bar_toolbar.*
import kotlinx.android.synthetic.main.toolbar_search.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivSearch.setOnClickListener {
            circularRevealAnimation(false)
        }

        vSearchToolbar.setNavigationOnClickListener {
            circularRevealAnimation(true)
        }
    }

    /**
     *  Performs circular reveal Animation for search bar
     *  @param { isReverse = Boolean } if its false
     *  then it will open with circular reveal else it will close with circular reveal
     */
    private fun circularRevealAnimation(isReverse : Boolean) {
        val cx = (ivSearch.right + ivSearch.left) / 2
        val cy = ivSearch.height
        val finalRadius = Math.hypot(vSearchBar.width.toDouble(), vSearchBar.height.toDouble())
        val circularRevealAnim = if (!isReverse) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ViewAnimationUtils.createCircularReveal(vSearchBar, cx, cy, 0.0f, finalRadius.toFloat() )
            } else {
                null
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ViewAnimationUtils.createCircularReveal(vSearchBar, cx, cy, finalRadius.toFloat(),  0.0f)
            } else {
                null
            }
        }
        circularRevealAnim?.let {
            circularRevealAnim.duration = 300
            if (!isReverse) {
                vSearchBar.visibility = View.VISIBLE
                vToolbar.visibility = View.INVISIBLE
            } else {
                vToolbar.visibility = View.VISIBLE
            }
            circularRevealAnim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    if (isReverse) {
                        vSearchBar.visibility = View.INVISIBLE
                    }
                }
            })
            circularRevealAnim.start()
        }
    }

