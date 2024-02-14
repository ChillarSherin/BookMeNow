package com.chillarcards.bookmenow.ui.register

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentOtpBinding
import com.chillarcards.bookmenow.utills.Const
import com.chillarcards.bookmenow.utills.GenericKeyEvent
import com.chillarcards.bookmenow.utills.GenericTextWatcher
import com.chillarcards.bookmenow.utills.PrefManager
import com.chillarcards.bookmenow.utills.SMSReceiver
import com.chillarcards.bookmenow.utills.Status
import com.chillarcards.bookmenow.viewmodel.RegisterViewModel
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


open class OTPFragment : Fragment() {

    lateinit var binding: FragmentOtpBinding

    private lateinit var prefManager: PrefManager
    private lateinit var timer: CountDownTimer
    private val digitRegex = "^\\d$".toRegex()
    private val args: OTPFragmentArgs by navArgs()
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var callbacks: OnVerificationStateChangedCallbacks
    private lateinit var smsBroadcastReceiver: BroadcastReceiver
    private val mobileViewModel by viewModel<RegisterViewModel>()
    private var mVerificationId = ""
    private var mResendToken: ForceResendingToken? = null

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

        FirebaseApp.initializeApp(this.requireContext())
        firebaseAuth = FirebaseAuth.getInstance()
        prefManager = PrefManager(requireContext())

        if (binding.timer.text == "00:60")
            startTimer()

        otpViewActions()
        otpObserver()

        val maskedPhoneNumber = maskPhoneNumber(args.mobile.toString())
        binding.otpHeadMsg.text="We have send a 6 digit OTP to $maskedPhoneNumber"

        binding.resendText.visibility = View.GONE

        binding.confirmBtn.setOnClickListener {
            if (binding.otpA.text.isNullOrEmpty() || binding.otpB.text.isNullOrEmpty() || binding.otpC.text.isNullOrEmpty() || binding.otpD.text.isNullOrEmpty() || binding.otpE.text.isNullOrEmpty() || binding.otpF.text.isNullOrEmpty()) {
                Const.shortToast(requireContext(), "please enter the 6 digit OTP code")
            } else {
                val otp =
                    "${binding.otpA.text.toString()}${binding.otpB.text.toString()}${binding.otpC.text.toString()}${binding.otpD.text.toString()}${binding.otpE.text.toString()}${binding.otpF.text.toString()}"
                Log.d("abc_otp", "onViewCreated: $otp")
                if (otp.isNotEmpty()){
                    verifyPhoneNumberWithCode(otp)
//                    mobileVerify()
                }
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
           // resendVerificationCode(args.mobile.toString(), mResendToken!!)
            resendVerificationCode(args.mobile.toString())
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
        }
        catch (e: Exception) {
            //e.printstackTrace()
        }

        callbacks = object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)
                binding.textinputError.visibility=View.VISIBLE
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        // Invalid request
                        binding.textinputError.text = e.message
                    }
                    is FirebaseTooManyRequestsException -> {
                        // The SMS quota for the project has been exceeded
                        binding.textinputError.text = e.message
                    }
                    is FirebaseAuthMissingActivityForRecaptchaException -> {
                        // reCAPTCHA verification attempted with null Activity
                        binding.textinputError.text = e.message
                    }
                    else -> {
                        // Handle other types of Firebase exceptions
                        binding.textinputError.text = "An error occurred: ${e.message}"
                    }
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number,
                // we now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                mVerificationId = verificationId
                mResendToken = token
                Log.d(TAG, "onCodeSent:$verificationId")

                if (binding.timer.text == "00:60")
                    startTimer()

                Const.shortToast(requireContext(),"OTP Shared")
            }
        }

    //    startPhoneNumberVerification(args.mobile.toString())
        startSMSListener()
    }

    private fun maskPhoneNumber(phoneNumber: String): String {
        if (phoneNumber.length < 5) {
            return phoneNumber
        }
        val maskedLength = phoneNumber.length - 5
        val maskedString =
            "*".repeat(maskedLength)
        return maskedString + phoneNumber.substring(phoneNumber.length - 5)
    }


    private fun clearOTP() {
        binding.textinputError.visibility=View.GONE
        binding.otpA.setText("")
        binding.otpB.setText("")
        binding.otpC.setText("")
        binding.otpD.setText("")
        binding.otpE.setText("")
        binding.otpF.setText("")
        binding.otpA.requestFocus()
    }

    private fun otpViewActions() {
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
        binding.resendText.visibility = View.GONE
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
                        Const.disableButton(binding.confirmBtn)
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
        val textName = binding.timer.text

        if (aOk && bOk && cOk && dOk && eOk && fOk && !textName.equals(getString(R.string.otp_expired)))
            Const.enableButton(binding.confirmBtn)
        else
            Const.disableButton(binding.confirmBtn)
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {

        Log.w(TAG, "onVerificationMOb +91$phoneNumber")

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber("+91$phoneNumber")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun verifyPhoneNumberWithCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(args.verificationID.toString(), code)
        signInWithPhoneAuthCredential(credential)
    }

    //private fun resendVerificationCode(phoneNumber: String, mResendToken: ForceResendingToken) {
    private fun resendVerificationCode(phoneNumber: String) {
        setTimer()
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber("+91$phoneNumber")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
          //  .setForceResendingToken(mResendToken) // ForceResendingToken from callbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = task.result?.user
                    mobileVerify()
                } else {
                    // Sign in failed
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        binding.textinputError.text="wrong"
                    }
                }
            }
    }

    private fun startSMSListener() {
        smsBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
                    val extras = intent.extras
                    if (extras != null) {
                        val sms = extras.getString(SmsRetriever.EXTRA_SMS_MESSAGE)
                        // Process the SMS message and extract the OTP code
                        processSMSMessage(sms)
                    }
                }
            }
        }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        requireActivity().registerReceiver(smsBroadcastReceiver, intentFilter)
    }


    private fun processSMSMessage(otp: String?) {
        // Extract the OTP code from the message
        // and fill it in the OTP fields
        Log.d(TAG, "Received SMS: $otp")
//        if (otp != null && otp.length == 6) {
//            binding.otpA.setText(otp[0])
//            binding.otpB.setText(otp.charAt(1))
//            binding.otpC.setText(otp.charAt(2))
//            binding.otpD.setText(otp.charAt(3))
//            binding.otpE.setText(otp.charAt(4))
//            binding.otpF.setText(otp.charAt(5))
//            binding.confirmBtn.performClick()
//        }
    }

    companion object {
        private const val TAG = "OTPFragment"
    }


    private fun mobileVerify() {
        mobileViewModel.run {
            mob.value = args.mobile
            verifyMobile()
        }
    }

    private fun otpObserver() {
        try {
            mobileViewModel.regData.observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            hideProgress()
                            it.data?.let { mobileData ->
                                when (mobileData.statusCode) {
                                    "200" -> {
                                        // TODO: check if response from verifying otp or sending otp
                                        prefManager.setMobileNo(mobileData.data.phone)
                                        prefManager.setRefToken(mobileData.data.refresh_token.trim())
                                        prefManager.setToken(mobileData.data.access_token.trim())
                                        prefManager.setStatus(mobileData.data.profile_completed)
                                        prefManager.setIsLoggedIn(true)
                                        prefManager.setRefresh("0")

                                        if(mobileData.data.profile_completed == 0){
                                            gotoGeneralHome()
                                        }else{
                                            gotoHomePage()
                                        }

                                    }
                                    "401" -> {

                                    }
                                    "400" -> {
                                        if(mobileData.message.contentEquals("Invalid OTP.")){
                                            binding.otpA.setText("")
                                            binding.otpB.setText("")
                                            binding.otpC.setText("")
                                            binding.otpD.setText("")
                                            binding.otpE.setText("")
                                            binding.otpF.setText("")
                                            binding.otpA.requestFocus()
                                            Const.shortToast(requireContext(), mobileData.message)
                                        }else{
                                            Const.shortToast(requireContext(), mobileData.message)
                                        }
                                    }
                                    else -> Const.shortToast(requireContext(), mobileData.message)
                                }
                            }
                        }
                        Status.LOADING -> {
                            showProgress()
                        }
                        Status.ERROR -> {
                            hideProgress()
                            Const.shortToast(requireContext(), it.message.toString())
                        }
                    }
                }
            }


        } catch (e: Exception) {
            Log.e("abc_otp", "setUpObserver: ", e)
        }
    }

    private fun gotoHomePage() {
        try {
            findNavController().navigate(OTPFragmentDirections.actionOTPFragmentToHomeFragment())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun gotoGeneralHome() {
        try {
            findNavController().navigate(OTPFragmentDirections.actionOTPFragmentToRegFragment())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
