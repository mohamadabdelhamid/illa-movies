package com.mabdelhamid.illamovies.common

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes

sealed class UiText {
    data class String(val value: kotlin.String?) : UiText()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any?
    ) : UiText()

    fun asString(context: Context? = null): kotlin.String? {
        return when (this) {
            is String -> value
            is StringResource -> context?.getString(resId, *args)
        }
    }
}
