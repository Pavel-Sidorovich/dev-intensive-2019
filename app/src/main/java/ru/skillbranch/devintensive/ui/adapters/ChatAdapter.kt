package ru.skillbranch.devintensive.ui.adapters

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.item_chat_archive.*
import kotlinx.android.synthetic.main.item_chat_group.*
import kotlinx.android.synthetic.main.item_chat_single.*
import kotlinx.android.synthetic.main.item_chat_single.tv_message_single
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.utils.Utils.getColorFromAttr

class ChatAdapter(val listener: (ChatItem)-> Unit) : RecyclerView.Adapter<ChatAdapter.ChatItemViewHolder>() {
    companion object {
        private const val ARCHIVE_TYPE = 0
        private const val SINGLE_TYPE = 1
        private const val GROUP_TYPE = 2
    }
    var items: List<ChatItem> = listOf()

    override fun getItemViewType(position: Int): Int = when(items[position].chatType){
        ChatType.ARCHIVE -> ARCHIVE_TYPE
        ChatType.SINGLE -> SINGLE_TYPE
        ChatType.GROUP -> GROUP_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            SINGLE_TYPE -> SingleViewHolder(inflater.inflate(R.layout.item_chat_single, parent, false))
            GROUP_TYPE -> GroupViewHolder(inflater.inflate(R.layout.item_chat_group, parent, false))
            else -> ArchiveViewHolder(inflater.inflate(R.layout.item_chat_archive, parent, false))
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    fun updateData(data : List<ChatItem>){

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

    abstract inner class ChatItemViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView), LayoutContainer{
        override val containerView: View?
            get() = itemView

        abstract fun bind(item: ChatItem, listener: (ChatItem) -> Unit)
    }

    inner class SingleViewHolder(convertView: View) : ChatItemViewHolder(convertView), ItemTouchViewHolder {
        override fun onItemSelected() {
            itemView.setBackgroundColor(getColorFromAttr(R.attr.colorSelected, itemView.context.theme))
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(getColorFromAttr(R.attr.colorBackground, itemView.context.theme))
        }

        override fun bind(item: ChatItem, listener : (ChatItem) -> Unit) {
            if(item.avatar == null){
               Glide.with(itemView)
                   .clear(iv_avatar_single)
                iv_avatar_single.setImageBitmap(drawInitials(item.initials, iv_avatar_single.context))
            } else {
                Glide.with(itemView)
                    .load(item.avatar)
                    .into(iv_avatar_single)
            }

            sv_indicator.visibility = if(item.isOnline) View.VISIBLE else View.GONE

            with(tv_date_single){
                visibility = if(item.lastMessageDate != null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }

            with(tv_counter_single){
                visibility = if(item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }

            tv_title_single.text = item.title
            tv_message_single.text = item.shortDescription
            itemView.setOnClickListener{
                listener.invoke(item)
            }
        }
    }

    inner class GroupViewHolder(convertView: View) : ChatItemViewHolder(convertView), ItemTouchViewHolder {
        override fun onItemSelected() {
            itemView.setBackgroundColor(getColorFromAttr(R.attr.colorSelected, itemView.context.theme))
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(getColorFromAttr(R.attr.colorBackground, itemView.context.theme))
        }

        override fun bind(item: ChatItem, listener : (ChatItem) -> Unit) {
            iv_avatar_group.setImageBitmap(drawInitials(item.initials, iv_avatar_group.context))

            with(tv_date_group){
                visibility = if(item.lastMessageDate != null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }

            with(tv_counter_group){
                visibility = if(item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }

            tv_title_group.text = item.title
            tv_message_group.text = item.shortDescription
            with(tv_message_author){
                visibility = if(!item.isEmpty) View.VISIBLE else View.GONE
                text = "@${item.author}"
            }
            itemView.setOnClickListener{
                listener.invoke(item)
            }
        }
    }

    inner class ArchiveViewHolder(convertView: View) : ChatItemViewHolder(convertView) {

        override fun bind(item: ChatItem, listener : (ChatItem) -> Unit) {

            with(tv_date_archive){
                visibility = if(item.lastMessageDate != null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }

            with(tv_counter_archive){
                visibility = if(item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }
            tv_message_author_archive.text = "@${item.author}"
            tv_title_archive.text = item.title
            tv_message_archive.text = item.shortDescription
            itemView.setOnClickListener{
                listener.invoke(item)
            }
        }
    }

    private fun drawInitials(text : String, myContext : Context) : Bitmap {
        val size = myContext.resources.getDimension(R.dimen.avatar_item_size).toInt()
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val mPaint = Paint()
        val mTextBoundRect = Rect()


        val centerX = size / 2
        val centerY = size / 2

//        val value = TypedValue()
//        myContext.theme.resolveAttribute(R.attr.colorAccent, value, true)
//        canvas.drawColor(value.data)

        canvas.drawColor(Utils.getColorRandom(text))
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