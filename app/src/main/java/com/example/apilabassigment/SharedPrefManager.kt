package com.example.apilabassigment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences


class SharedPrefManager(context: Context) {
    private val SHARED_PREF_NAME = "volleyregisterlogin"

    private val KEY_EMAIL = "keyemail"
    private val KEY_PASSWORD = "keypassword"
    private val KEY_ID = "keyid"
    private var ctx: Context? = context


    fun userLogin(user: User) {
        val sharedPreferences: SharedPreferences =
            ctx!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_EMAIL, user.email)
        editor.putString(KEY_PASSWORD, user.password)
        editor.apply()
    }

    open fun isLoggedIn(): Boolean {
        val sharedPreferences: SharedPreferences =
            ctx!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_EMAIL, null) != null
    }

    fun getUser(): User {
        val sharedPreferences: SharedPreferences =
            ctx!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return User(

            sharedPreferences.getString(KEY_EMAIL, null)!!,
            sharedPreferences.getString(KEY_PASSWORD, null)!!
        )
    }


    companion object {

        private var mInstance: SharedPrefManager? = null

        @Synchronized

        fun getInstance(ctx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(ctx)
            }
            return mInstance as SharedPrefManager
        }

    }

}