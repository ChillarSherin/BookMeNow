package com.chillarcards.bookmenow.utills

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.ActivityCompat
import com.chillarcards.bookmenow.MainActivity
import java.util.Timer
import java.util.TimerTask


class BackgroundService : Service() {
    override fun onCreate() {
        super.onCreate()
        // Start a timer or schedule a task to check for user inactivity
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                checkUserInactivity()
            }
        }, 0, 60000) // Check every minute
    }

    private fun checkUserInactivity() {
        val currentTime = System.currentTimeMillis()
        val lastActivityTimestamp = getLastActivityTimestampFromSharedPreferences()

        // Calculate time difference
        val timeDifference = currentTime - lastActivityTimestamp

        // Check if the time difference exceeds 24 hours (24 * 60 * 60 * 1000 milliseconds)
        if (timeDifference > 24 * 60 * 60 * 1000) {
            // Logout the user
            logoutUsers()
        }
    }

    private fun getLastActivityTimestampFromSharedPreferences(): Long {
        // Retrieve last activity timestamp from SharedPreferences
//         Return the timestamp
        return getLastActivityTimestampFromSharedPreferences()
    }

    private fun logoutUsers() {
        // Perform logout operation
        // For example, clear user session, navigate to login screen, etc.
        MainActivity.justLoggedIn
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}