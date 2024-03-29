package com.example.playlistmarket.root

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.playlistmarket.App.Companion.appContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import java.time.LocalDateTime

fun <T> debounce(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    action: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (debounceJob?.isActive == false || debounceJob == null) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action.invoke(param)
            }
        }
    }
}

fun hideKeyboard(fragment: Fragment) {
    val activity = fragment.requireActivity() as AppCompatActivity
    activity.currentFocus?.let { view ->
        val inputMethodManager =
            appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun currentTimeTag(): Int =
    with(LocalDateTime.now()) {
        StringBuilder()
            .append(year - 2023)
            .append(dayOfYear)
            .append(second + minute * 60 + hour * 3600)
            .toString()
            .toInt()
    }
