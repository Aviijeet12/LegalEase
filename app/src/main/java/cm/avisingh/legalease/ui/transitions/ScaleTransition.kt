package cm.avisingh.legalease.ui.transitions

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.transition.Transition
import android.transition.TransitionValues
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class ScaleTransition : Transition {
    constructor() : super()
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private companion object {
        private const val PROP_SCALE_X = "legalease:scale:scaleX"
        private const val PROP_SCALE_Y = "legalease:scale:scaleY"
    }

    override fun getTransitionProperties(): Array<String> {
        return arrayOf(PROP_SCALE_X, PROP_SCALE_Y)
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    private fun captureValues(transitionValues: TransitionValues) {
        val view = transitionValues.view
        transitionValues.values[PROP_SCALE_X] = view.scaleX
        transitionValues.values[PROP_SCALE_Y] = view.scaleY
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        if (startValues == null || endValues == null) return null

        val view = endValues.view
        view.scaleX = 0.8f
        view.scaleY = 0.8f

        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.8f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.8f, 1f)

        return ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY).apply {
            duration = 300
        }
    }
}