package org.protein.project.ui.locale

import java.util.Locale

actual fun setAppLocale(language: String) {
    Locale.setDefault(Locale(language))
}
