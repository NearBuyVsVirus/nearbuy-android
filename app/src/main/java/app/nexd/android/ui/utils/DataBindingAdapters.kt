package app.nexd.android.ui.utils

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter

object DataBindingAdapters {

    @BindingAdapter("android:error")
    @JvmStatic
    fun textViewBindError(view: TextView, @StringRes error: Int) {
        view.error = if (error != 0) view.context.getString(error) else null
    }
}