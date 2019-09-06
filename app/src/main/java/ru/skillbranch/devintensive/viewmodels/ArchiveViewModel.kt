package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import ru.skillbranch.devintensive.models.data.ChatItem

class ArchiveViewModel : BaseViewModel() {
    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()


        val filterF = {
            val queryStr = query.value!!
            val chats = chatItems.value//?.map { it.toChatItem() }

            result.value = if (queryStr.isEmpty()) {
                chats
                    ?.filter { it.isArchived }
                    ?.map { it.toChatItem() }
                    ?.sortedBy { it.id.toInt() }
            } else {
                chats
                    ?.filter { it.isArchived }
                    ?.sortedBy { it.id.toInt() }
                    ?.filter {
                        for (member in it.members) {
                            if (member.firstName?.contains(queryStr, true) == true
                                || member.lastName?.contains(queryStr, true) == true
                            ) {
                                return@filter true
                            }
                        }
                        return@filter false
                    }
                    ?.map { it.toChatItem() }
            }
        }

        result.addSource(chatItems) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }

        return result //chats
    }
}