package ru.skillbranch.devintensive.viewmodels

import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.repositories.ChatRepository

open class BaseViewModel : BaseThemeViewModel() {
    protected val query = mutableLiveData("")
    private val chatRepository = ChatRepository
    protected val chatItems = chatRepository.loadChats()

    protected var archivedCount = 0

    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        archivedCount ++
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        archivedCount --
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }
}