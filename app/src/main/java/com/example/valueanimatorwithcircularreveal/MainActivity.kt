package com.example.valueanimatorwithcircularreveal

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import kotlin.math.hypot
import android.opengl.ETC1.getWidth
import android.opengl.Visibility
import android.text.TextUtils
import android.util.DisplayMetrics
import androidx.appcompat.widget.Toolbar
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginStart
import kotlinx.android.synthetic.main.gmail_search_box.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etSearch2.visibility = View.INVISIBLE
        ivSearch.setOnClickListener {
//            circularRevealAnimation(false)
            animateSearchAndBack(false)
        }

        vSearchToolbar.setNavigationOnClickListener {
            circularRevealAnimation(true)
        }

        vGmailSearch.setOnClickListener {
            animateGmailSearch()
        }
    }

    /**
     *  Animate search like Gmail
     */
    private fun animateGmailSearch() {
        ivGmailBack.visibility = View.VISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            vGmailSearch.background = null
        }
        var hamBurgerRotate = ObjectAnimator.ofFloat(ivMenu, "rotation", 0f, 180.toFloat())
        hamBurgerRotate.duration = 300
        var hamBurgerFadeOut = ObjectAnimator.ofFloat(ivMenu, "alpha", 1.0f, 0f)
        hamBurgerFadeOut.duration = 200

        var backRotate = ObjectAnimator.ofFloat(ivGmailBack, "rotation", -90f, 0f)
        backRotate.duration = 300
        var backFadeIn = ObjectAnimator.ofFloat(ivGmailBack, "alpha", 0f, 1f)
        backFadeIn.duration = 300

        var genericXAnimate = ObjectAnimator.ofFloat(ivGmailBack, "translationX", ivMenu.x, 0.toFloat())

        val animatorSetSearch = AnimatorSet()
        animatorSetSearch.play(hamBurgerFadeOut).with(hamBurgerRotate)
        animatorSetSearch.play(backFadeIn).with(backRotate)
        animatorSetSearch.play(backRotate).with(genericXAnimate)
        animatorSetSearch.addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                /*ivGmailBack.visibility = View.VISIBLE
                ivMenu.visibility = View.GONE
                val animatorSetBack = AnimatorSet()
                animatorSetBack*//*.play(backFadeIn).with(backRotate)*//*.play(backRotate)
                animatorSetBack.start()*/
            }
        })
        animatorSetSearch.start()
    }

    /**
     *  Performs circular reveal Animation for search bar
     *  @param { isReverse = Boolean } if its false
     *  then it will open with circular reveal else it will close with circular reveal
     */
    private fun circularRevealAnimation(isReverse : Boolean) {
        val cx = (ivSearch.right + ivSearch.left) / 2
        val cy = ivSearch.height
        val finalRadius = hypot(vSearchBar.width.toDouble(), vSearchBar.height.toDouble())
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

    /**
     * Search and back
     */
    private fun animateSearchAndBack(isReverse: Boolean) {
        var animatorSet = AnimatorSet()
        var animDuration = 1000
        val displayMetrics = resources.displayMetrics
        val modifierX = -(displayMetrics.widthPixels - etSearch2.x - ivSearch.marginRight)
        etSearch2.visibility = View.VISIBLE

        val vNavIcon = vSearchToolbar.navigationIconView
        vNavIcon?.visibility = View.GONE

        var vNavViewFadeOut = ObjectAnimator.ofFloat(ivSearch, "alpha", 1f, 0f)
        vNavViewFadeOut.duration = animDuration.toLong()

        var vSearchTranslationX = ObjectAnimator.ofFloat(ivSearch, "translationX", modifierX)
        vSearchTranslationX.duration = animDuration.toLong()

        var etSearchFadeInAnim = ObjectAnimator.ofFloat(ivSearch, "alpha", 0f, 1f)
        etSearchFadeInAnim.duration = animDuration.toLong()

        animatorSet.play(vSearchTranslationX).with(etSearchFadeInAnim)
        animatorSet.play(vSearchTranslationX).with(vNavViewFadeOut)


//        animatorSet.play(vSearchFadeOut).with()

        animatorSet.start()

    }

    val Toolbar.navigationIconView: View?
        get() {
            //check if contentDescription previously was set
            val hadContentDescription = !TextUtils.isEmpty(navigationContentDescription)
            val contentDescription = if (hadContentDescription) navigationContentDescription else "navigationIcon"
            navigationContentDescription = contentDescription
            val potentialViews = arrayListOf<View>()
            //find the view based on it's content description, set programatically or with android:contentDescription
            findViewsWithText(potentialViews, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION)
            //Clear content description if not previously present
            if (!hadContentDescription) {
                navigationContentDescription = null
            }
            //Nav icon is always instantiated at this point because calling setNavigationContentDescription ensures its existence
            return potentialViews.firstOrNull()
        }
}
