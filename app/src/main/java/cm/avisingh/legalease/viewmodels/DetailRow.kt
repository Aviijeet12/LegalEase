package cm.avisingh.legalease.viewmodels

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import cm.avisingh.legalease.R

class DetailRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val tvLabel: TextView
    private val tvValue: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.item_detail_row, this, true)
        orientation = HORIZONTAL

        tvLabel = findViewById(R.id.tvLabel)
        tvValue = findViewById(R.id.tvValue)

        // Handle custom attributes
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.DetailRow)

            val label = typedArray.getString(R.styleable.DetailRow_label)
            val value = typedArray.getString(R.styleable.DetailRow_value)
            val icon = typedArray.getResourceId(R.styleable.DetailRow_icon, 0)

            setLabel(label ?: "")
            setValue(value ?: "")

            if (icon != 0) {
                setIcon(icon)
            }

            typedArray.recycle()
        }
    }

    fun setLabel(text: String) {
        tvLabel.text = text
    }

    fun setValue(text: String) {
        tvValue.text = text
    }

    fun setIcon(iconRes: Int) {
        tvLabel.setCompoundDrawablesWithIntrinsicBounds(iconRes, 0, 0, 0)
    }
}