package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository

class ArchiveViewModel : BaseViewModel() {
    private val archiveChat = Transformations.map(chatItems) { chats ->
        return@map chats.filter { it.isArchived }
            .map { it.toChatItem() }
            .sortedBy { it.id.toInt() }
    }

    fun getArchiveChat(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryStr = query.value!!
            val chatItem = archiveChat.value!!

            result.value = if (queryStr.isEmpty()) {
                chatItem
            } else {
                chatItem.filter { it.title.contains(queryStr, true) }
            }
        }

        result.addSource(archiveChat) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }

        return result
    }
}