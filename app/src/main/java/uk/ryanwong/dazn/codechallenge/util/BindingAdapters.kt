package uk.ryanwong.dazn.codechallenge.util

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import timber.log.Timber


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
