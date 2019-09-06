package ru.skillbranch.devintensive.models.data

import androidx.annotation.VisibleForTesting
import ru.skillbranch.devintensive.extensions.DAY
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.ImageMessage
import ru.skillbranch.devintensive.models.TextMessage
import ru.skillbranch.devintensive.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

data class Chat(
    val id: String,
    val title: String,
    val members: List<User> = listOf(),
    var messages: MutableList<BaseMessage> = mutableListOf(),
    var isArchived: Boolean = false
) {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun unreadableMessageCount(): Int {
        var count = 0
        for (message in messages) {
            if (!message.isReaded) {
                count++
            } else {
                return count
            }
        }
        return count
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageDate(): Date? {
        return if (messages.isEmpty()) {
            null//Date()
        } else {
            messages.last().date
        }


    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageShort(): Pair<String?, String?> {//= when(val lastMessage = messages.lastOrNull()){
        val lastMessage = messages.lastOrNull() ?: return "Сообщений еще нет" to ""
        if (lastMessage is TextMessage) {
            return if (lastMessage.text?.length ?: 0 > 128) {
                lastMessage.text!!.substring(0, 127) to (lastMessage.from.firstName ?: "")
            } else {
                lastMessage.text to (lastMessage.from.firstName ?: "")
            }
        }
        return "Сообщений еще нет" to ""
    }

    private fun isSingle(): Boolean = members.size == 1

    fun toChatItem(): ChatItem {

//        if (isArchived) {
//            ChatItem(
//                id,
//                null,
//                "",
//                "Archive",
//                lastMessageShort().first,
//                unreadableMessageCount(),
//                "",
//                false,
//                ChatType.ARCHIVE,
//                lastMessageShort().second
//            )
//        } else
        return if (isSingle()) {
            val user = members.first()
            ChatItem(
                id,
                user.avatar,
                Utils.toInitials(user.firstName, user.lastName) ?: "??",
                "${user.firstName ?: ""} ${user.lastName ?: ""}",
                lastMessageShort().first,
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat() ?: "",
                user.isOnline
            )
        } else {
            val user = members.first()
            ChatItem(
                id,
                null,
                Utils.toInitials(user.firstName, user.lastName) ?: "??",
                title,
                lastMessageShort().first,
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat() ?: "",
                false,
                ChatType.GROUP,
                lastMessageShort().second,
                messages.isEmpty()
            )
        }
    }
}

enum class ChatType {
    SINGLE,
    GROUP,
    ARCHIVE
}



