package com.leoevg.weldedge.presentation.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.provider.Settings.Global.getString

// контекст - класс котоырй хранит в себе все данные о приложении: локаль, тема,
// только через него можно получить доступ к шаредпрефс - он нужен чтобы запускать все 4 компонента андлроид
// (активити, броадкаст ресивер, сервис, контентпровайдер). он отвечает за ресурсы - и мы его тут переиспользуем.
private fun Context.getStringResourceById(id: String?): String {
    val packageName: String? = getPackageName()
    val resId: Int = resources.getIdentifier(id, "string", packageName)
    return getString(resId)
}

private fun Context.getDrawableResourceById(id: String?): Drawable? {
    val packageName: String? = getPackageName()
    val resId: Int = resources.getIdentifier(id, "drawable", packageName)
    return getDrawable(resId)
}

