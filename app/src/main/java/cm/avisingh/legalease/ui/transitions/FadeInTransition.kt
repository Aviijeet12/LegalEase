package cm.avisingh.legalease.ui.transitions

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.transition.Transition
import android.transition.TransitionValues
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat

class FadeInTransition : Transition {
    constructor() : super()
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private companion object {
        private const val PROP_ALPHA = "legalease:fadeIn:alpha"
    }

    override fun getTransitionProperties(): Array<String> {
        return arrayOf(PROP_ALPHA)
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    private fun captureValues(transitionValues: TransitionValues) {
        val view = transitionValues.view
        transitionValues.values[PROP_ALPHA] = ViewCompat.getAlpha(view)
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        if (startValues == null || endValues == null) return null

        val view = endValues.view
        view.alpha = 0f
        return ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
            duration = 300
        }
    }
}