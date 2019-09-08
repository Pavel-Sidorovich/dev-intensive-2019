package ru.skillbranch.devintensive

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import ru.skillbranch.devintensive.repositories.PreferencesRepository

class App: Application() {
    companion object{
        private var instance:App? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    init {
        instance = this
    }

    override fun onCreate() {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate()
//            PreferencesRepository.getAppTheme().also {
//                AppCompatDelegate.setDefaultNightMode(it)
//            }
        //call once when app created
    }
}