package nl.sjtek.client.android.cards

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import nl.sjtek.client.android.R
import nl.sjtek.control.data.responses.ResponseCollection

class NightModeCard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseCard(context, attrs, defStyleAttr) {

    override fun onShouldInflate(context: Context) {
        val horizontal = context.resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
        val vertical = context.resources.getDimensionPixelSize(R.dimen.activity_vertical_margin)
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER)
        layoutParams.leftMargin = horizontal
        layoutParams.rightMargin = horizontal
        layoutParams.topMargin = vertical
        layoutParams.bottomMargin = vertical

        val textView = TextView(context)
        textView.layoutParams = layoutParams
        textView.setText(R.string.night_mode_enabled)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(R.style.TextAppearance_AppCompat_Body1)
        } else {
            textView.setTextAppearance(context, R.style.TextAppearance_AppCompat_Body1)
        }

        addView(textView)
    }

    override fun onUpdate(update: ResponseCollection) {
        super.onUpdate(update)
        visibility = if (update.nightMode.isEnabled) View.VISIBLE else View.GONE
    }
}
