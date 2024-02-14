package com.chillarcards.bookmenow.utills

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import java.util.regex.Pattern

class SMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
            val extras = intent.extras
            if (extras != null) {
                val smsBundle = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as Bundle
                val smsMessage = smsBundle.getString(SmsRetriever.EXTRA_SMS_MESSAGE)
                if (smsMessage != null) {
                    // Extract the OTP code from the SMS message
                    val otpCode = extractOTP(smsMessage)
                    if (otpCode != null) {
                        // Process the OTP code as per your application's requirements
                        Log.d(TAG, "Received OTP: $otpCode")
                    }
                }
            }
        }
    }

    private fun extractOTP(message: String): String? {
        val pattern = Pattern.compile("(\\d{6})")
        val matcher = pattern.matcher(message)
        return if (matcher.find()) {
            matcher.group(0)
        } else {
            null
        }
    }

    companion object {
        private const val TAG = "SMSReceiver"
    }
}
