package ru.skillbranch.devintensive.ui.adapters

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatItem

class ChatTouchHelperCallback(
    archivedAdapter: ChatAdapter,
    archivedSwipeListener: (ChatItem) -> Unit
): ChatItemTouchHelperCallback(
    archivedAdapter,
    archivedSwipeListener
) {
    private val iconBounds = Rect()

    override fun drawIcon(canvas: Canvas, itemView: View, dX: Float) {
        val icon = itemView.resources.getDrawable(R.drawable.ic_archive_white_24dp, itemView.context.theme)
        val iconSize = itemView.resources.getDimensionPixelSize(R.dimen.icon_size)
        val space = itemView.resources.getDimensionPixelSize(R.dimen.spacing_normal_16)

        val margin = (itemView.bottom - itemView.top - iconSize) / 2
        with(iconBounds){
            left = itemView.right + dX.toInt() + space
            top = itemView.top + margin
            right = itemView.right + dX.toInt() + iconSize + space
            bottom = itemView.bottom - margin
        }

        icon.bounds = iconBounds
        icon.draw(canvas)
    }
}