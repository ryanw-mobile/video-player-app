package uk.ryanwong.dazn.codechallenge.util

import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import timber.log.Timber
import uk.ryanwong.dazn.codechallenge.util.extensions.fadeIn
import uk.ryanwong.dazn.codechallenge.util.extensions.fadeOut
import java.util.*


/**
 * Use this binding adapter to show and hide the views using boolean variables
 */
@BindingAdapter("fadeVisible")
fun setFadeVisible(view: View, visible: Boolean? = true) {
    if (view.tag == null) {
        view.tag = true
        view.visibility = if (visible == true) View.VISIBLE else View.GONE
    } else {
        view.animate().cancel()
        if (visible == true) {
            if (view.visibility == View.GONE)
                view.fadeIn()
        } else {
            if (view.visibility == View.VISIBLE)
                view.fadeOut()
        }
    }
}

@BindingAdapter("thumbnail")
fun fetchImage(view: ImageView, src: String?) {
    src?.let {
        Timber.v("thumbnail: src = $src")
        val uri = src.toUri().buildUpon().scheme("https").build()
        Glide.with(view)
            .load(uri)
//            .placeholder(R.drawable.ic_baseline_error_outline_24)
//            .error(R.drawable.ic_baseline_error_outline_24)
//            .fallback(R.drawable.ic_baseline_error_outline_24)
            .into(view)
    } ?: run {
        // src is null, we still have to show a placeholder
//        view.setImageResource(R.drawable.ic_baseline_error_outline_24)
    }
}

@BindingAdapter("niceDateString")
fun toNiceString(view: TextView, src: Date?) {
    src?.let {
        Timber.v("toNiceString: date = ${it.time}")
        // Data formatting similar but not exactly the same as the mockups.
        // This is done without using any 3rd party library
        view.text = DateUtils.getRelativeDateTimeString(
            view.context,
            it.time,
            DateUtils.DAY_IN_MILLIS,
            DateUtils.DAY_IN_MILLIS * 3, 0
        ).toString()
    }
}
