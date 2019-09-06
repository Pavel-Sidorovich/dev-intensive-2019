package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import ru.skillbranch.devintensive.models.data.ChatItem

class MainViewModel : BaseViewModel() {
//    private val query = mutableLiveData("")
//    private val chatRepository = ChatRepository
//    private val chatItems = chatRepository.loadChats()
//
//    private var archivedCount = 0

//    private val chats = Transformations.map(chatItems){chats ->
//        return@map chats.filter { !it.isArchived }
//            .map { it.toChatItem() }
//            .sortedBy { it.id.toInt() }
//    }

    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()


        val filterF = {
            val queryStr = query.value!!
            val chats = chatItems.value//?.map { it.toChatItem() }

            result.value = if (queryStr.isEmpty()) {
                chats
                    ?.filter { !it.isArchived}
                    ?.map { it.toChatItem() }
                    ?.sortedBy { it.id.toInt() }
            } else {
                chats
                    ?.filter { !it.isArchived }
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

//    private fun loadChats(): List<ChatItem>{
//        val chats = chatRepository.loadChats()
//        return chats.map {
//            it.toChatItem()
//        }
//            .sortedBy { it.id.toInt() }
//    }

//    fun addItems() {
//        val newItems = DataGenerator.generateChatsWithOffset(chats.value!!.size, 5).map { it.toChatItem() }
//        val copy = chats.value!!.toMutableList()
//        copy.addAll(newItems)
//        chats.value = copy.sortedBy { it.id.toInt() }
//    }

//    fun addToArchive(chatId: String) {
//        val chat = chatRepository.find(chatId)
//        chat ?: return
//        archivedCount ++
//        chatRepository.update(chat.copy(isArchived = true))
//    }
//
//    fun restoreFromArchive(chatId: String) {
//        val chat = chatRepository.find(chatId)
//        chat ?: return
//        archivedCount --
//        chatRepository.update(chat.copy(isArchived = false))
//    }
//
//    fun handleSearchQuery(text: String?) {
//        query.value = text
//    }
}