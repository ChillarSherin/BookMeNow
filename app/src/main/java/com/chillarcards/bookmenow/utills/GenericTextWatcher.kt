package com.chillarcards.bookmenow.utills

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.chillarcards.bookmenow.R


/**
 * Created by Sherin on 02-11-2023.
 */


class GenericKeyEvent internal constructor(
    private val currentView: AppCompatEditText,
    private val previousView: AppCompatEditText?
) : View.OnKeyListener {
    override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.otp_a && currentView.text != null && currentView.text!!.isEmpty()) {
            //If current is empty then previous EditText's number will also be deleted
            previousView!!.text = null
            previousView.requestFocus()
            return true
        }
        return false
    }


}

class GenericTextWatcher internal constructor(
    private val currentView: View,
    private val nextView: View?
) :
    TextWatcher {
    override fun afterTextChanged(editable: Editable) {
        val text = editable.toString()
        when (currentView.id) {
            R.id.otp_a -> if (text.length == 1) nextView!!.requestFocus()
            R.id.otp_b -> if (text.length == 1) nextView!!.requestFocus()
            R.id.otp_c -> if (text.length == 1) nextView!!.requestFocus()
            R.id.otp_d -> if (text.length == 1) nextView!!.requestFocus()
            R.id.otp_e -> if (text.length == 1) nextView!!.requestFocus()
//            R.id.otp_f -> hideKeyboard()
        }
    }

    override fun beforeTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) {
    }

    override fun onTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) {
    }

}