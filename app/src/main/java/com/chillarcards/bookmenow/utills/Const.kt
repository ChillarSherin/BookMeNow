package com.chillarcards.bookmenow.utills

import android.content.Context
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.viewmodel.RegisterViewModel

class Const {
    companion object {


        const val ver_title = ":  " //Client
        private lateinit var prefManager: PrefManager

        fun enableButton(button: Button) {
            button.isEnabled = true
            button.alpha = 1f
        }

        fun disableButton(button: Button) {
            button.isEnabled = false
            button.alpha = 0.55f
        }

        fun shortToast(context: Context, value: String) {
            //Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
            val builder = AlertDialog.Builder(context)
            //set message for alert dialog
            builder.setMessage(value)
            //performing negative action
            builder.setNegativeButton(context.getString(R.string.close)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setOnShowListener {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.resources.getColor(R.color.primary_red)
                )
            }
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        }

        fun getNewTokenAPI(
            context: Context,
            authViewModel: RegisterViewModel,
            viewLifecycleOwner: LifecycleOwner
        ) {
            prefManager = PrefManager(context)
            authViewModel.mob.value = prefManager.getMobileNo()
            authViewModel.getAuthToken()
            authViewModel.tokenData.observe(viewLifecycleOwner) {
                when (it!!.status) {
                    Status.SUCCESS -> {
                        it.data?.let { authData ->
                            when (authData.statusCode) {
                                "200" -> {
                                    prefManager.setRefresh("0")
                                    Log.d("abc_const", "getNewTokenAPI: ${authData.data.access_token}")
                                    prefManager.setToken(authData.data.access_token)
                                }
                                else -> {
                                    shortToast(context, authData.message)
                                }
                            }
                        }
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                    }
                }
            }
        }

    }
}