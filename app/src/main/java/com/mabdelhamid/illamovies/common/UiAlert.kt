package com.mabdelhamid.illamovies.common

data class UiAlert(
    val type: Type = Type.NORMAL,
    val text: UiText
) {

    enum class Type { NORMAL, ERROR, SUCCESS }
}