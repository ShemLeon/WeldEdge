package com.leoevg.weldedge.presentation.utils

import android.content.Context
import android.graphics.drawable.Drawable

// контекст - класс который хранит в себе все данные о приложении: локаль, тема,
// только через него можно получить доступ к шаредпрефс - он нужен чтобы запускать все 4 компонента андлроид
// (активити, броадкаст ресивер, сервис, контентпровайдер). он отвечает за ресурсы - и мы его тут переиспользуем.
internal fun Context.getStringResourceById(id: String?): String {
    val name = id?.removePrefix("R.string.")
    val packageName: String? = getPackageName()
    val resId: Int = resources.getIdentifier(name, "string", packageName)
    return getString(resId)
}

internal fun Context.getDrawableResourceById(id: String?): Drawable? {
    val name = id?.removePrefix("R.drawable.")
    val packageName: String? = getPackageName()
    val resId: Int = resources.getIdentifier(name, "drawable", packageName)
    return if (resId != 0) getDrawable(resId) else null
}

internal const val baseAssetPath = "file:///android_asset/"

