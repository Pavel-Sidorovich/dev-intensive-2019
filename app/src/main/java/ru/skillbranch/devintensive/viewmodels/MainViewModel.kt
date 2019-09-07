package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.ChatItem

class MainViewModel : BaseViewModel() {

    private val chats = Transformations.map(chatItems) { chats ->
        val result = chats.filter { !it.isArchived }
            .map { it.toChatItem() }
            .sortedBy { it.id.toInt() }.toMutableList()

        val archive = chats.filter { it.isArchived }

        if (archive.isNotEmpty()) {
            result.add(0 , Chat.toChatArchive(archive))
        }

        return@map result
    }

    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryStr = query.value!!
            val chatItem = chats.value!!

            result.value = if (queryStr.isEmpty()) {
                chatItem
            } else {
                chatItem.filter { it.title.contains(queryStr, true) }
            }
        }

        result.addSource(chats) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }

        return result
    }
}