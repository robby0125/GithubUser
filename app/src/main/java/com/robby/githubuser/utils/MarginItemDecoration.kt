package com.robby.githubuser.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val verticalMargin: Int, private val horizontalMargin: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) top = verticalMargin

            bottom = verticalMargin
            left = horizontalMargin
            right = horizontalMargin
        }
    }
}