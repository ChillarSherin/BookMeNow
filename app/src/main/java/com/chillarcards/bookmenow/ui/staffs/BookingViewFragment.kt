package com.chillarcards.bookmenow.ui.staffs

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentViewAllBinding
import com.chillarcards.bookmenow.ui.Booking
import com.chillarcards.bookmenow.ui.Dummy
import com.chillarcards.bookmenow.ui.adapter.BookingAdapter
import com.chillarcards.bookmenow.ui.booking.BookingAllFragmentDirections
import com.chillarcards.bookmenow.ui.home.HomeFragmentDirections
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills
import com.chillarcards.bookmenow.utills.CommonDBaseModel
import com.chillarcards.bookmenow.utills.Const
import com.chillarcards.bookmenow.utills.PrefManager
import com.chillarcards.bookmenow.utills.Status
import com.chillarcards.bookmenow.viewmodel.BookingViewModel
import com.chillarcards.bookmenow.viewmodel.RegisterViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class BookingViewFragment : Fragment(), IAdapterViewUtills {

    lateinit var binding: FragmentViewAllBinding
    private val bookingViewModel by viewModel<BookingViewModel>()
    private lateinit var prefManager: PrefManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewAllBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager(requireContext())

        setToolbar()
        binding.headTran.text = getString(R.string.book_head)

        val currentDate = Calendar.getInstance().time
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate)

        bookingViewModel.run {
            doctorID.value = prefManager.getDoctorId().toString()
            date.value = formattedDate
            getBookingList()
        }

        setUpObserver()

    }

    private fun setToolbar() {
        binding.toolbar.toolbarTitle.text = getString(R.string.booking_head)
        binding.toolbar.toolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun setUpObserver() {
        try {
            bookingViewModel.bookingData.observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            hideProgress()
                            it.data?.let { bookingData ->
                                when (bookingData.statusCode) {
                                    200 -> {

                                        val bookingAdapter = BookingAdapter(
                                            bookingData.data.appointmentList, context,this@BookingViewFragment)
                                        binding.tranRv.adapter = bookingAdapter
                                        binding.tranRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


                                    }
                                    403 -> {
                                        prefManager.setRefresh("1")
                                        val authViewModel by viewModel<RegisterViewModel>()
                                        Const.getNewTokenAPI(
                                            requireContext(),
                                            authViewModel,
                                            viewLifecycleOwner
                                        )

                                    }
                                    else -> Const.shortToast(requireContext(), bookingData.message)

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
    private fun showProgress() {
        binding.otpProgress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.otpProgress.visibility = View.GONE
    }
    private fun setBottomSheet(selectedData: ArrayList<CommonDBaseModel>) {
//        val bottomSheetFragment = BottomSheetFragment(selectedData)
//        bottomSheetFragment.show((context as AppCompatActivity).supportFragmentManager, bottomSheetFragment.tag)

        val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_persistent, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetView)

        val customerTV: TextView = bottomSheetView.findViewById(R.id.customerNameTextView)
        customerTV.text = selectedData[0].valueStr1

//        val closeButton: ImageView = bottomSheetView.findViewById(R.id.closeButton)
//        closeButton.setOnClickListener {
//            bottomSheetDialog.dismiss()
//        }

        val completeButton: TextView = bottomSheetView.findViewById(R.id.completedButton)
        completeButton.setOnClickListener {
            findNavController().navigate(
                BookingAllFragmentDirections.actionBookingFragmentToEstimateFragment(
                )
            )
            bottomSheetDialog.dismiss()
        }

        val callButton: TextView = bottomSheetView.findViewById(R.id.callButton)
        callButton.setOnClickListener {
            val phoneNumber = "tel:" + selectedData[0].mastIDs
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse(phoneNumber)
            if (dialIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(dialIntent)
            }
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()

    }

    override fun onStop() {
        super.onStop()
        Log.d("abc_mob", "onStop: ")
       // mobileViewModel.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("abc_mob", "onDestroy: ")
    }

    override fun getAdapterPosition(
        Position: Int,
        ValueArray: ArrayList<CommonDBaseModel>,
        Mode: String?
    ) {
        if(Mode.equals("VIEW")){
            setBottomSheet(ValueArray)
        }
    }

}
