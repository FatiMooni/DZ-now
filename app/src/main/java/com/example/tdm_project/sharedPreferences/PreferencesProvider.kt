package com.example.tdm_project.sharedPreferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.tdm_project.data.Topic
import com.example.tdm_project.data.getTopics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class PreferencesProvider (context: Context){
//**language**/
    val PREFERENCE_NAME = "SharedPreferenceExample"
    val PREFERENCE_LANGUAGE = "Language"


    internal var mySharedPref : SharedPreferences = context.getSharedPreferences("filename",Context.MODE_PRIVATE)
    lateinit var editor : SharedPreferences.Editor
    lateinit var gson: Gson
    var topics = ArrayList<Topic>()



    //Create keys to manage the shared preferences
    companion object {
        const val DEVELOP_MODE = false
        private const val DEVICE_TOKEN = "data.source.prefs.DEVICE_TOKEN"
    }

    /**
     * For teh dark mode preferences
     */
   //Save the Dark mode
   @SuppressLint("CommitPrefEdits")
   fun setDarkModeState(state : String){
       editor = mySharedPref.edit()
       editor.putString("current_theme",state)
       editor.commit()

   }

    //Get the dark Mode State
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun load(): String {
        return mySharedPref.getString("current_theme","light")
    }

    /**
     * For the list of Topics
     */

   //save the list of topics
    @SuppressLint("CommitPrefEdits")
    fun setTopicsList(topics : ArrayList<Topic>){
        gson = Gson()
        val list = gson.toJson(topics)
        editor = mySharedPref.edit()
        editor.putString("current_topics",list)
        editor.apply()
    }

    //get the list of topics
    fun loadTopicsList(): ArrayList<Topic> {
        val gson = Gson()
        val json = mySharedPref.getString("current_topics", null)
        val type = object : TypeToken<ArrayList<Topic>>() {
        }.type


        return if(json !=null)
            {gson.fromJson(json, type)}

        else getTopics()
    }

    //create a variable to access to the shared preferences
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)



    //enable write/read from my preference provider
    var deviceToken = preferences.getString(DEVICE_TOKEN, "")
        set(value) = preferences.edit().putString(DEVICE_TOKEN,     value).apply()


    //About Language


    val preference = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)

    fun getLoginCount() : String{
        return preference.getString(PREFERENCE_LANGUAGE,"fr")
    }

    fun setLoginCount(Language:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_LANGUAGE,Language)
        editor.apply()
    }

}
