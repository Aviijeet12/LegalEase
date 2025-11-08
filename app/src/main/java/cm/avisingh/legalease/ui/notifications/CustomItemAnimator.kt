package cm.avisingh.legalease.ui.notifications

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class CustomItemAnimator : DefaultItemAnimator() {

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.alpha = 0f
        holder.itemView.translationY = holder.itemView.height.toFloat() / 4
        
        val animatorSet = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(holder.itemView, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(
                    holder.itemView,
                    View.TRANSLATION_Y,
                    holder.itemView.height.toFloat() / 4,
                    0f
                )
            )
            duration = addDuration
        }
        
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                dispatchAddStarting(holder)
            }
            override fun onAnimationEnd(animator: Animator) {
                dispatchAddFinished(holder)
            }
            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        
        animatorSet.start()
        return true
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        val animatorSet = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(holder.itemView, View.ALPHA, 1f, 0f),
                ObjectAnimator.ofFloat(
                    holder.itemView,
                    View.TRANSLATION_X,
                    0f,
                    -holder.itemView.width.toFloat()
                )
            )
            duration = removeDuration
        }
        
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                dispatchRemoveStarting(holder)
            }
            override fun onAnimationEnd(animator: Animator) {
                dispatchRemoveFinished(holder)
            }
            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        
        animatorSet.start()
        return true
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        val fadeOut = ObjectAnimator.ofFloat(oldHolder.itemView, View.ALPHA, 1f, 0f)
        fadeOut.duration = changeDuration / 2

        val fadeIn = ObjectAnimator.ofFloat(newHolder.itemView, View.ALPHA, 0f, 1f)
        fadeIn.duration = changeDuration / 2
        fadeIn.startDelay = changeDuration / 2

        val animatorSet = AnimatorSet().apply {
            playTogether(fadeOut, fadeIn)
        }
        
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                dispatchChangeStarting(oldHolder, true)
                dispatchChangeStarting(newHolder, false)
            }
            override fun onAnimationEnd(animator: Animator) {
                dispatchChangeFinished(oldHolder, true)
                dispatchChangeFinished(newHolder, false)
            }
            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        
        animatorSet.start()
        return true
    }
}