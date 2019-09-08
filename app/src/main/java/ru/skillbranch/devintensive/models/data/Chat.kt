package ru.skillbranch.devintensive.models.data

import androidx.annotation.VisibleForTesting
import ru.skillbranch.devintensive.extensions.DAY
import ru.skillbranch.devintensive.extensions.short128
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
    companion object {
    fun toChatArchive(archiveChats: List<Chat>) : ChatItem {
        archiveChats.sortedBy { it.lastMessageDate() }

        var counter = 0
        for(chat in archiveChats) {
            counter += chat.unreadableMessageCount()
        }
        return ChatItem( "archiveChats" ,
            "" ,
            "" ,
            "Архив чатов" ,
            archiveChats.last().lastMessageShort().first ,
            counter ,
            archiveChats.last().lastMessageDate()?.shortFormat(),
            false,
            ChatType.ARCHIVE ,
            archiveChats.last().lastMessageShort().second)
    }
}
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun unreadableMessageCount(): Int = messages.map { !it.isReaded }.size

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageDate(): Date? = messages.lastOrNull()?.date

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageShort(): Pair<String?, String?> =
        when (val lastMessage = messages.lastOrNull()) {
            is TextMessage -> (lastMessage.text?.short128() ?: "Сообщений еще нет") to ("@" + lastMessage.from.firstName ?: "")
            is ImageMessage -> "Отправил фото" to ("@" + lastMessage.from.firstName ?: "")
            else -> "Сообщений еще нет" to ""
        }

    private fun isSingle(): Boolean = members.size == 1

    fun toChatItem(): ChatItem {
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



