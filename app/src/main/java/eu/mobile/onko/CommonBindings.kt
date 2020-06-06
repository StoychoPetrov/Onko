package eu.mobile.onko

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

object CommonBindings {
    @BindingAdapter("goneUnless")
    @JvmStatic
    fun goneUnless(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @BindingAdapter("setStatusName")
    @JvmStatic
    fun setStatusName(textView: TextView, status: Int) {
        val name     = String.format("res_status_%1s", status)
        val flagResid  = textView.resources.getIdentifier(name, "string", textView.context.packageName)
        textView.text       = textView.context.getString(flagResid)

        when(status) {

            3 -> textView.setTextColor(ContextCompat.getColor(textView.context, R.color.dark_green))

            4 -> textView.setTextColor(ContextCompat.getColor(textView.context, R.color.dark_red))

            5 -> textView.setTextColor(ContextCompat.getColor(textView.context, R.color.dark_blue))
        }
    }
}