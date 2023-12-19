package com.chillarcards.bookmenow.ui.register


import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentOtpBinding
import com.chillarcards.bookmenow.utills.Const
import com.chillarcards.bookmenow.utills.GenericKeyEvent
import com.chillarcards.bookmenow.utills.GenericTextWatcher
import com.chillarcards.bookmenow.utills.PrefManager


class OTPFragment : Fragment() {

    lateinit var binding: FragmentOtpBinding

    private lateinit var prefManager: PrefManager
    private lateinit var timer: CountDownTimer
    private val digitRegex = "^\\d$".toRegex()

//    private val otpViewModel by viewModel<OTPViewModel>()

    private var aOk = false
    private var bOk = false
    private var cOk = false
    private var dOk = false
    private var eOk = false
    private var fOk = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
                alertMsg(requireContext())
            }
        })
    }

    fun alertMsg(context: Context) {
        try {
            PrefManager(context)
            val builder = AlertDialog.Builder(context)

            builder.setTitle(R.string.alert_heading)
            builder.setMessage(R.string.pop_alert_message)
            builder.setIcon(R.mipmap.ic_launcher)
            builder.setCancelable(false)

            //performing positive action
            builder.setPositiveButton(context.getString(R.string.ok)) { _, _ ->
                findNavController().popBackStack()
            }
            builder.setNegativeButton(context.getString(R.string.cancel)) { _, _ ->
//                alertDialog.dismiss()
            }
            val alertDialog: AlertDialog = builder.create()

            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        } catch (e: Exception) {
            //e.printstackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_otp, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (binding.timer.text == "00:30")
            startTimer()
        prefManager = PrefManager(requireContext())
        otpObserver()

        binding.confirmBtn.setOnClickListener {
            if (binding.otpA.text.isNullOrEmpty() || binding.otpB.text.isNullOrEmpty() || binding.otpC.text.isNullOrEmpty() || binding.otpD.text.isNullOrEmpty() || binding.otpE.text.isNullOrEmpty() || binding.otpF.text.isNullOrEmpty()) {
                Const.shortToast(requireContext(), "please enter the 6 digit OTP code")
            } else {
                val otp =
                    "${binding.otpA.text.toString()}${binding.otpB.text.toString()}${binding.otpC.text.toString()}${binding.otpD.text.toString()}${binding.otpE.text.toString()}${binding.otpF.text.toString()}"
                Log.d("abc_otp", "onViewCreated: $otp")
                if (otp.isNotEmpty())
                    callVerificationAPI(otp)//verifyCode(otp)
                else
                    Const.shortToast(requireContext(), getString(R.string.enter_otp))
            }
        }

        binding.resendText.setOnClickListener {
            // Const.shortToast(requireContext(), "Resending OTP. Please wait")
//            otpViewModel.mob.value = prefManager.getUserPhoneNumber()
//            otpViewModel.userId.value = prefManager.getUserId()
//            otpViewModel.getOTP()
            clearOTP()
        }

        try {
            val s = "didn't receive OTP? RESEND"
            val wordToSpan: Spannable = SpannableString(s)
            wordToSpan.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primary_peach
                    )
                ),
                s.indexOf("?") + 1, s.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            wordToSpan.setSpan(
                StyleSpan(Typeface.BOLD), s.indexOf("?") + 1, s.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.resendText.text = wordToSpan
        } catch (e: Exception) {
            //e.printstackTrace()
        }

        otpViewActions()

    }





    private fun clearOTP() {
        binding.otpA.setText("")
        binding.otpB.setText("")
        binding.otpC.setText("")
        binding.otpD.setText("")
        binding.otpE.setText("")
        binding.otpF.setText("")
        binding.otpA.requestFocus()
    }

    private fun callVerificationAPI(otp: String) {
//        otpViewModel.mob.value = prefManager.getUserPhoneNumber()
//        otpViewModel.otp.value = otp
//        otpViewModel.userId.value = prefManager.getUserId()
//        otpViewModel.token.value = Const.getToken(requireContext())
//        otpViewModel.verifyOTP()
//        otpObserver()

        try {
            //Make data save and redirect to home fragment directly
            context?.let { it1 -> PrefManager(it1).setIsLoggedIn(true) }

            findNavController().navigate(
                OTPFragmentDirections.actionOTPFragmentToHomeFragment(

                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun otpObserver() {

    }

    private fun otpViewActions() {
//      GenericTextWatcher here works only for moving to next EditText when a number is entered
//      first parameter is the current EditText and second parameter is next EditText
        binding.otpA.addTextChangedListener(GenericTextWatcher(binding.otpA, binding.otpB))
        binding.otpA.addTextChangedListener {
            aOk = it != null && it.matches(digitRegex)
            checkValidationStatus()
        }
        binding.otpB.addTextChangedListener(GenericTextWatcher(binding.otpB, binding.otpC))
        binding.otpB.addTextChangedListener {
            bOk = it != null && it.matches(digitRegex)
            checkValidationStatus()
        }
        binding.otpC.addTextChangedListener(GenericTextWatcher(binding.otpC, binding.otpD))
        binding.otpC.addTextChangedListener {
            cOk = it != null && it.matches(digitRegex)
            checkValidationStatus()
        }
        binding.otpD.addTextChangedListener(GenericTextWatcher(binding.otpD, binding.otpE))
        binding.otpD.addTextChangedListener {
            dOk = it != null && it.matches(digitRegex)
            checkValidationStatus()
        }
        binding.otpE.addTextChangedListener(GenericTextWatcher(binding.otpE, binding.otpF))
        binding.otpE.addTextChangedListener {
            eOk = it != null && it.matches(digitRegex)
            checkValidationStatus()
        }
        binding.otpF.addTextChangedListener(GenericTextWatcher(binding.otpF, null))
        binding.otpF.addTextChangedListener {
            fOk = it != null && it.matches(digitRegex)
            checkValidationStatus()
        }

//      GenericKeyEvent here works for deleting the element and to switch back to previous EditText
//      first parameter is the current EditText and second parameter is previous EditText
        binding.otpA.setOnKeyListener(GenericKeyEvent(binding.otpA, null))
        binding.otpB.setOnKeyListener(GenericKeyEvent(binding.otpB, binding.otpA))
        binding.otpC.setOnKeyListener(GenericKeyEvent(binding.otpC, binding.otpB))
        binding.otpD.setOnKeyListener(GenericKeyEvent(binding.otpD, binding.otpC))
        binding.otpE.setOnKeyListener(GenericKeyEvent(binding.otpE, binding.otpD))
        binding.otpF.setOnKeyListener(GenericKeyEvent(binding.otpF, binding.otpE))
    }

    private fun showProgress() {
        binding.confirmBtn.visibility = View.INVISIBLE
        binding.otpProgress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.confirmBtn.visibility = View.VISIBLE
        binding.otpProgress.visibility = View.GONE
    }

    private fun setTimer() {
//        binding.resendText.visibility = View.VISIBLE
        binding.timerGrp.visibility = View.VISIBLE
        startTimer()
    }

    private fun startTimer() {
        if (this@OTPFragment::timer.isInitialized)
            timer.cancel()
        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val s = "00:${(millisUntilFinished / 1000)}"
                binding.timer.text = s
                binding.resendText.visibility = View.GONE

            }

            override fun onFinish() {
                Handler(Looper.getMainLooper()).postDelayed({
                    try {
                        binding.sec.visibility = View.GONE
                        binding.timer.text = getString(R.string.otp_expired)
                        binding.resendText.visibility = View.VISIBLE

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, 1000)
            }
        }
        timer.start()
    }


    private fun checkValidationStatus() {
        if (aOk && bOk && cOk && dOk && eOk && fOk)
            Const.enableButton(binding.confirmBtn)
        else
            Const.disableButton(binding.confirmBtn)
    }

}
