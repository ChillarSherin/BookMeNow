package com.chillarcards.bookmenow.ui.register

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentSignupBinding
import com.chillarcards.bookmenow.utills.Const


class LoginFragment : Fragment() {

    lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pInfo =
            activity?.let { activity?.packageManager!!.getPackageInfo(it.packageName, PackageManager.GET_ACTIVITIES) }
        val versionName = pInfo?.versionName //Version Name

        binding.version.text = "${getString(R.string.version)}" + Const.ver_title + versionName


        Const.enableButton(binding.signinBtn)
        Const.enableButton(binding.signupBtn)
        setUpObserver()

        binding.signinBtn.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToMobileFragment(
                )
            )
        }

        binding.signupBtn.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment(
                )
            )
        }



    }

    private fun setUpObserver() {

    }

    override fun onStop() {
        super.onStop()
        Log.d("abc_mob", "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("abc_mob", "onDestroy: ")
    }

   companion object {
        private const val TAG = "MobileFragment"
    }
}