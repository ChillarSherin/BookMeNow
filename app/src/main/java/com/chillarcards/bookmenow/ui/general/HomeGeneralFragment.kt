package com.chillarcards.bookmenow.ui.general

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chillarcards.bookmenow.MainActivity
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentGeneralHomeBinding
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills
import com.chillarcards.bookmenow.utills.CommonDBaseModel
import com.chillarcards.bookmenow.utills.Const
import com.chillarcards.bookmenow.utills.PrefManager
import com.chillarcards.bookmenow.utills.Status
import com.chillarcards.bookmenow.viewmodel.GeneralViewModel
import com.chillarcards.bookmenow.viewmodel.RegisterViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeGeneralFragment : Fragment(), IAdapterViewUtills {

    lateinit var binding: FragmentGeneralHomeBinding
    private val generalViewModel by viewModel<GeneralViewModel>()
    private lateinit var prefManager: PrefManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_general_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager(requireContext())
        Const.enableButton(binding.confirmBtn)
        generalViewModel.run {
            getGeneralSetting()
        }
        setUpObserver()


        binding.intervalFrm.setEndIconOnClickListener {
            binding.interval.setText("")
        }
        binding.profile.setOnClickListener{
            findNavController().navigate(
                HomeGeneralFragmentDirections.actionGeneralFragmentToRegisterFragment(
                )
            )
        }
        binding.workingHrs.setOnClickListener{
            findNavController().navigate(
                HomeGeneralFragmentDirections.actionGeneralFragmentToTimeFragment(
                    generalViewModel.doctorID.value
                )
            )
        }
        binding.bankFrm.setOnClickListener{
            findNavController().navigate(
                HomeGeneralFragmentDirections.actionGeneralFragmentToBankFragment(
                )
            )
        }
        binding.confirmBtn.setOnClickListener{
            findNavController().navigate(
                HomeGeneralFragmentDirections.actionGeneralFragmentToHomeFragment(
                )
            )
        }

        binding.logout.setOnClickListener {
            setBottomSheet()
        }

//        binding.onoffShop.setOnClickListener{
//            if (generalViewModel.shopStatus.value==1) {
//                alertMsg(requireContext(),"Are you sure you want to OFF the booking link ?")
//            } else {
//                alertMsg(requireContext(),"Are you sure you want to ON the booking link ?")
//            }
//        }
        setCheckBoxListener()
    }

    private fun setCheckBoxListener(){
        binding.shopStatus.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                alertMsg(requireContext(),"Are you sure you want to OFF the booking link ?")
            } else {
                alertMsg(requireContext(),"Are you sure you want to ON the booking link ?")
            }
        }
    }
    private fun alertMsg(context: Context, message :String) {
        try {
            PrefManager(context)
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.alert_heading)
            builder.setMessage(message)
            builder.setIcon(R.mipmap.ic_launcher)
            builder.setCancelable(false)

            //performing positive action
            builder.setPositiveButton(context.getString(R.string.ok)) { _, _ ->
                generalViewModel.run {
                    getStatus()
                }
            }
            builder.setNegativeButton(context.getString(R.string.cancel)) { _, _ ->
                Const.shortToast(requireContext(),generalViewModel.shopStatus.value.toString())

                binding.shopStatus.setOnCheckedChangeListener( null )
                binding.shopStatus.isChecked = (generalViewModel.shopStatus.value == 1)
                setCheckBoxListener()

                findNavController().popBackStack()
            }
            val alertDialog: AlertDialog = builder.create()

            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        } catch (e: Exception) {
            //e.printstackTrace()
        }
    }

    private fun setUpObserver() {
        try {
            generalViewModel.settingData.observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            hideProgress()
                            it.data?.let { settingData ->
                                when (settingData.statusCode) {
                                    200 -> {
                                        binding.shopStatus.setOnCheckedChangeListener( null )
                                        binding.shopStatus.isChecked = (settingData.data.entityStatus == 1)
                                        setCheckBoxListener()
                                        if (settingData.data.entityStatus == 0) {
                                            binding.shopStatus.setTextColor(resources.getColor(R.color.theme_end))
                                        }else{
                                            binding.shopStatus.setTextColor(resources.getColor(R.color.onoff))
                                        }

                                        binding.interval.setText(settingData.data.consultationDuration.toString())
                                        generalViewModel.shopStatus.value = settingData.data.entityStatus
                                        generalViewModel.doctorID.value = settingData.data.doctor_id.toString()
                                        prefManager.setDoctorId(settingData.data.doctor_id)
                                    }
                                    else -> Const.shortToast(requireContext(), settingData.message)

                                }
                            }
                        }
                        Status.LOADING -> {
                            showProgress()
                        }
                        Status.ERROR -> {
                            hideProgress()
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
            generalViewModel.statusData.observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            hideProgress()
                            it.data?.let { settingData ->
                                when (settingData.statusCode) {
                                    200 -> {
                                        generalViewModel.run {
                                            getGeneralSetting()
                                        }
                                    }
                                    else -> Const.shortToast(requireContext(), settingData.message)

                                }
                            }
                        }
                        Status.LOADING -> {
                            showProgress()
                        }
                        Status.ERROR -> {
                            hideProgress()
                            prefManager.setRefresh("1")
                            val authViewModel by viewModel<RegisterViewModel>()
                            Const.getNewTokenAPI(
                                requireContext(),
                                authViewModel,
                                viewLifecycleOwner
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("abc_otp", "setUpObserver: ", e)
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


    override fun getAdapterPosition(
        Position: Int,
        ValueArray: ArrayList<CommonDBaseModel>,
        Mode: String?
    ) {
//        if(Mode.equals("VIEW")) {
//            val bottomSheetFragment = BottomSheetFragment(ValueArray)
//            bottomSheetFragment.show(
//                (context as AppCompatActivity).supportFragmentManager,
//                bottomSheetFragment.tag
//            )
//        }
    }

    private fun setBottomSheet() {

        val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.logout, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetView)

        val completeButton: TextView = bottomSheetView.findViewById(R.id.cancelButton)
        completeButton.setOnClickListener {

            bottomSheetDialog.dismiss()
        }

        val callButton: TextView = bottomSheetView.findViewById(R.id.okButton)
        callButton.setOnClickListener {
            performLogout()
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun performLogout() {

        // Clear any user session or authentication data
        clearUserSession()

        Log.d("abc_home", "showLogoutAlert: recreating activity.. all data cleared")
        val intent = Intent(requireContext(), MainActivity::class.java)
        ActivityCompat.finishAffinity(requireActivity())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }

    private fun clearUserSession() {

        val prefManager = PrefManager(requireContext())
        prefManager.clearAll()
    }

}