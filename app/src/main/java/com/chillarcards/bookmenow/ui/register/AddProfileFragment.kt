package com.chillarcards.bookmenow.ui.register

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentAddProfileBinding
import com.chillarcards.bookmenow.databinding.FragmentAddStaffBinding
import com.chillarcards.bookmenow.ui.Dummy
import com.chillarcards.bookmenow.ui.adapter.BookingAdapter
import com.chillarcards.bookmenow.utills.Const
import com.chillarcards.bookmenow.utills.PrefManager
import com.chillarcards.bookmenow.utills.Status
import com.chillarcards.bookmenow.viewmodel.ProfileViewModel
import com.chillarcards.bookmenow.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddProfileFragment : Fragment() {

    lateinit var binding: FragmentAddProfileBinding
    private lateinit var prefManager: PrefManager
    private val profileViewModel by viewModel<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager(requireContext())

        profileViewModel.mob.value = prefManager.getMobileNo()
        profileViewModel.getProfile()

        setUpObserver()
        setToolbar()
        Const.enableButton(binding.confirmBtn)

        binding.confirmBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showProgress() {
        binding.confirmBtn.visibility = View.INVISIBLE
        binding.otpProgress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.confirmBtn.visibility = View.VISIBLE
        binding.otpProgress.visibility = View.GONE
    }

    private fun setUpObserver() {
        try {
            profileViewModel.profileData.observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            hideProgress()
                            it.data?.let { profileData ->
                                when (profileData.statusCode) {
                                    200 -> {
                                        // TODO: check if response from verifying otp or sending otp
                                        binding.pName.setText(profileData.data.doctor_name)
                                        binding.pQualifi.setText(profileData.data.qualification)
                                        binding.pDesi.setText(profileData.data.designation)
                                        binding.pDetail.setText(profileData.data.description)
                                        val consultationChargeText = profileData.data.consultation_charge.toString()
                                        binding.pFee.setText("₹"+consultationChargeText)
                                        Glide.with(requireActivity())
                                            //.load(profileData.data.profileImageUrl)
                                            .load("https://www.chillarpayments.com/Demo/Direct-Book/images/Wc2xbVeJl0d6cyfWGCxlvcsxxYogVqsJElJy5tvN.jpeg")
                                            .centerCrop()
                                            .transform(CircleCrop())
                                            .into(binding.imProfile)
                                    }
                                    403 -> {
                                        prefManager.setRefresh("1")
                                        val authViewModel by viewModel<RegisterViewModel>()
                                        Const.getNewTokenAPI(
                                            requireContext(),
                                            authViewModel,
                                            viewLifecycleOwner
                                        )

                                        profileViewModel.run {
                                            mob.value = prefManager.getMobileNo()
                                            getProfile()
                                        }
                                        setUpObserver()
                                    }
                                    else -> Const.shortToast(requireContext(),"dfgdfgdfgd"+ profileData.message)

                                }
                            }
                        }
                        Status.LOADING -> {
                            showProgress()
                        }
                        Status.ERROR -> {
                            hideProgress()
                            Const.shortToast(requireContext(), it.message.toString())
                            Const.shortToast(requireContext(),"403 profile")
                            prefManager.setRefresh("1")
                            val authViewModel by viewModel<RegisterViewModel>()
                            Const.getNewTokenAPI(
                                requireContext(),
                                authViewModel,
                                viewLifecycleOwner
                            )

//                            profileViewModel.run {
//                                mob.value = prefManager.getMobileNo()
//                                getProfile()
//                            }
//                            setUpObserver()
                        }
                    }
                }
            }


        } catch (e: Exception) {
            Log.e("abc_otp", "setUpObserver: ", e)
        }
    }

    private fun setToolbar() {
        binding.toolbar.toolbarTitle.text = getString(R.string.profile_details)
        binding.toolbar.toolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("abc_mob", "onStop: ")
       // profileViewModel.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("abc_mob", "onDestroy: ")
    }
}