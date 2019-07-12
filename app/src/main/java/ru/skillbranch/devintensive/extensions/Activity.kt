package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.ViewGroup



fun Activity.hideKeyboard() {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = this.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


fun Activity.isKeyboardOpen(): Boolean{
    val r = Rect()
    val view = (this
        .findViewById(android.R.id.content) as ViewGroup).getChildAt(0) as View
    view.getWindowVisibleDisplayFrame(r)
    val heightDiff = view.rootView.height - (r.bottom - r.top)
    if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
        Log.d("M_Activity", "Open")
        return true
    }
    Log.d("M_Activity", "Close")
    return false
}

fun Activity.isKeyboardClosed(): Boolean{
    val r = Rect()
    val view = (this
        .findViewById(android.R.id.content) as ViewGroup).getChildAt(0) as View
    view.getWindowVisibleDisplayFrame(r)
    val heightDiff = view.rootView.height - (r.bottom - r.top)
    if (heightDiff > 100) {
        return false
    }
    return true
}