package com.chillarcards.bookmenow.utills

import android.content.Context
import android.content.SharedPreferences

class PrefManager(_context: Context) {

    companion object {
        // Shared preferences file name
        private const val PREF_NAME = "Servicexpert"

        private const val IS_LOGGED_IN = "IS_LOGGED_IN"
        private const val REFRESH = "REFRESH"
        private const val REFRESHTOKEN = "REFRESHTOKEN"
        private const val TOKEN = "Token"
        private const val STATUS = "STATUS"
        private const val MOBILENO = "MOBILENO"
        private const val DOCTORID = "DOCTORID"

        // shared pref mode
        private const val PRIVATE_MODE = Context.MODE_PRIVATE
    }

    private val pref: SharedPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    private val editor: SharedPreferences.Editor = pref.edit()

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGGED_IN, false)
    }

    fun setIsLoggedIn(value: Boolean) {
        editor.putBoolean(IS_LOGGED_IN, value)
        editor.commit()
    }

    fun getToken(): String {
        return pref.getString(TOKEN, "") ?: ""
    }
    fun setToken(token: String) {
        editor.putString(TOKEN, token)
        editor.commit()
    }
    fun getRefToken(): String {
        return pref.getString(REFRESHTOKEN, "") ?: ""
    }
    fun setRefToken(token: String) {
        editor.putString(REFRESHTOKEN, token)
        editor.commit()
    }
    fun getRefresh(): String {
        return pref.getString(REFRESH, "") ?: ""
    }
    fun setRefresh(token: String) {
        editor.putString(REFRESH, token)
        editor.commit()
    }
    fun getMobileNo(): String {
        return pref.getString(MOBILENO, "") ?: ""
    }
    fun setMobileNo(value: String) {
        editor.putString(MOBILENO, value)
        editor.commit()
    }
    fun getDoctorId(): Int {
        return pref.getInt(DOCTORID, 0)
    }
    fun setDoctorId(value: Int) {
        editor.putInt(DOCTORID, value)
        editor.commit()
    }
    fun getStatus(): Int {
        return pref.getInt(STATUS, 0)
    }
    fun setStatus(value: Int) {
        editor.putInt(STATUS, value)
        editor.commit()
    }

    fun clearAll() {
        editor.clear()
        editor.commit()
    }

    fun clearField(keyName: String) {
        editor.remove(keyName).apply()
    }
}
