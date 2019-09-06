package ru.skillbranch.devintensive.viewmodels

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.repositories.PreferencesRepository

open class BaseThemeViewModel : ViewModel() {
    companion object {
        private val repository: PreferencesRepository = PreferencesRepository
        private val appTheme = MutableLiveData<Int>()
    }


    init {
//        profileDate.value = repository.getProfile()
        appTheme.value = repository.getAppTheme()
    }

    fun switchTheme() {
        if(appTheme.value == AppCompatDelegate.MODE_NIGHT_YES){
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        } else {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }
        repository.saveAppTheme(appTheme.value!!)
    }

    fun getTheme(): LiveData<Int> = appTheme
}
