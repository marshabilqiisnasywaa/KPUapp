package com.example.kpuapp
import android.content.Context
import android.content.SharedPreferences

class UserPreferenceManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFERENCE_FILE_NAME = "UserPreferencesManager"
        private const val KEY_USER_LOGGED_IN = "userLoggedIn"
        private const val KEY_USER_NAME = "userName"
        private const val KEY_USER_PASSWORD = "userPassword"

        @Volatile
        private var instance: UserPreferenceManager? = null

        fun getInstance(context: Context): UserPreferenceManager {
            return instance ?: synchronized(this) {
                instance ?: UserPreferenceManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    fun setUserLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_USER_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_USER_LOGGED_IN, false)
    }

    fun saveUserName(userName: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USER_NAME, userName)
        editor.apply()
    }

    fun saveUserPassword(userPassword: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USER_PASSWORD, userPassword)
        editor.apply()
    }

    fun getUserName(): String {
        return sharedPreferences.getString(KEY_USER_NAME, "") ?: ""
    }

    fun getUserPassword(): String {
        return sharedPreferences.getString(KEY_USER_PASSWORD, "") ?: ""
    }

    fun clearPreferences() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
