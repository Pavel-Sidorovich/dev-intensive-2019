package ru.skillbranch.devintensive.ui.adapters

import android.content.Context
import android.graphics.*
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chat_single.*
import kotlinx.android.synthetic.main.item_user_list.*
import kotlinx.android.synthetic.main.item_user_list.sv_indicator
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.UserItem

class UserAdapter(val listener: (UserItem) -> Unit) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    var items: List<UserItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val convertView = inflater.inflate(R.layout.item_user_list, parent, false)
        return UserViewHolder(convertView)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) = holder.bind(items[position], listener)

    fun updateData(data : List<UserItem>) {
        val diffCallback = object : DiffUtil.Callback(){
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = items[oldItemPosition].id == data[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = items[oldItemPosition].hashCode() == data[newItemPosition].hashCode()

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size

        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = data
        diffResult.dispatchUpdatesTo(this)
    }

    inner class UserViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView), LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bind(user: UserItem, listener: (UserItem) -> Unit) {
            if (user.avatar != null) {
                Glide.with(itemView)
                    .load(user.avatar)
                    .into(iv_avatar_user)
            } else {
                Glide.with(itemView)
                    .clear(iv_avatar_user)
//                iv_avatar_user.setInitials(user.initials ?: "??")
                iv_avatar_user.setImageBitmap(drawInitials(user.initials ?: "??", iv_avatar_user.context))
            }

            sv_indicator.visibility = if(user.isOnline) View.VISIBLE else View.GONE
            tv_user_name.text = user.fullName
            tv_last_activity.text = user.lastActivity
            iv_selected.visibility = if(user.isSelected) View.VISIBLE else View.GONE
            itemView.setOnClickListener {
                listener.invoke(user)
            }
        }
    }

    private fun drawInitials(text : String, myContext : Context) : Bitmap {
//        val myContext = iv_avatar_single.context
        val size = myContext.resources.getDimension(R.dimen.avatar_item_size).toInt()
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val mPaint = Paint()
        val mTextBoundRect = Rect()


        val centerX = size / 2 //width / 2
        val centerY = size / 2 //height / 2

        val value = TypedValue()
        myContext.theme.resolveAttribute(R.attr.colorAccent, value, true)
        canvas.drawColor(value.data)

        Log.d("M_ProfileActivity", "smth")
        // Рисуем текст
        mPaint.color = Color.WHITE
        mPaint.textSize = myContext.resources.getDimension(R.dimen.avatar_initials_20)

        // Подсчитаем размер текста
        mPaint.getTextBounds(text, 0, text.length, mTextBoundRect)
        // Используем measureText для измерения ширины
        val mTextWidth = mPaint.measureText(text)
        val mTextHeight = mTextBoundRect.height()

        canvas.drawText(
            text,
            centerX - (mTextWidth / 2f),
            centerY + (mTextHeight / 2f),
            mPaint
        )

        return bitmap
    }
}