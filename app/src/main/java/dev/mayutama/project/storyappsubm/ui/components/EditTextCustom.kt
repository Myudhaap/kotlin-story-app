package dev.mayutama.project.storyappsubm.ui.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import dev.mayutama.project.storyappsubm.R

class EditTextCustom @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): AppCompatEditText(context, attrs), View.OnTouchListener {

    private var originInputType = inputType
    private var validationRules: List<String> = emptyList()

    private var buttonImage: Drawable = if (inputType and InputType.TYPE_TEXT_VARIATION_PASSWORD == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
        ContextCompat.getDrawable(context, R.drawable.ic_eye_hide)!!.apply {
            setBounds(0,0,24,18)
        } as Drawable
    } else {
        ContextCompat.getDrawable(context, R.drawable.ic_cross)!!.apply {
            setBounds(0,0,24,18)
        } as Drawable
    }

    init {
        background = ContextCompat.getDrawable(context, R.drawable.bg_input_outline)
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EditTextCustom,
            0,
            0
        ).apply {
            try {
                val validateAttr = getString(R.styleable.EditTextCustom_validate)
                validationRules = validateAttr?.split("|")?.map { it.trim() } ?: emptyList()
            } finally {
                recycle()
            }
        }

        setOnTouchListener(this)

        addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
                if (p0.toString().isNotEmpty()) showButton() else hideButton()
                validate()
            }
        })
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            if (layoutDirection == LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (buttonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            }else {
                clearButtonStart = (width - paddingEnd - buttonImage.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }

            if (isClearButtonClicked) {
                if (originInputType and InputType.TYPE_TEXT_VARIATION_PASSWORD == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    when (event.action) {
                        MotionEvent.ACTION_UP -> {
                            if (inputType and InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                                buttonImage = ContextCompat.getDrawable(context, R.drawable.ic_eye_hide) as Drawable
                                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                                setSelection(text?.length ?: 0)
                                hideButton()
                                showButton()
                            } else {
                                buttonImage = ContextCompat.getDrawable(context, R.drawable.ic_eye) as Drawable
                                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                                setSelection(text?.length ?: 0)
                                hideButton()
                                showButton()
                            }
                            return true
                        }
                        else -> return false
                    }
                } else {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            buttonImage = ContextCompat.getDrawable(context, R.drawable.ic_cross) as Drawable
                            showButton()
                            return true
                        }
                        MotionEvent.ACTION_UP -> {
                            buttonImage = ContextCompat.getDrawable(context, R.drawable.ic_cross) as Drawable
                            when {
                                text != null -> {
                                    text?.clear()
                                    validate()
                                }
                            }
                            hideButton()
                            return true
                        }
                        else -> return false
                    }
                }
            } else return false
        }
        return false
    }

    private fun showButton() {
        setButtonDrawables(endOfTheText = buttonImage)
    }

    private fun hideButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null,
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    fun validate(): Boolean {
        val textValue = text?.toString()?.trim() ?: ""

        for (rule in validationRules) {
            when {
                rule.equals("required", ignoreCase = true) -> {
                    if (textValue.isEmpty()) {
                        error = String.format(ContextCompat.getString(context, R.string.validate_required))
                        return false
                    }
                }
                rule.startsWith("min_", ignoreCase = true) -> {
                    val min = rule.substringAfter("min_").toIntOrNull() ?: 0
                    if (textValue.length < min) {
                        error = String.format(ContextCompat.getString(context, R.string.validate_min_length), min)
                        return false
                    }
                }
            }
        }

        error = null
        return true
    }
}