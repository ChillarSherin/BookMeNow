package com.chillarcards.bookmenow.ui.booking

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentViewAllBinding
import com.chillarcards.bookmenow.ui.adapter.BookingAdapter
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


class BookingAllFragment : Fragment(), IAdapterViewUtills {

    lateinit var binding: FragmentViewAllBinding
    private val bookingViewModel by viewModel<BookingViewModel>()
    private lateinit var prefManager: PrefManager
    private val args: BookingAllFragmentArgs by navArgs()
    private var doctorName =""
    private var phoneNo =""
    private val PERMISSION_REQUEST_CALL_PHONE = 1


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

        bookingViewModel.run {
            doctorID.value = prefManager.getDoctorId().toString()
            date.value = args.date
            getBookingList()
        }
        setUpObserver()

        binding.nextDayTv.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    // Handle the selected date
                    val selectedDate = "$year-${(month + 1).toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
                    bookingViewModel.run {
                        doctorID.value = prefManager.getDoctorId().toString()
                        date.value = selectedDate
                        getBookingList()
                    }
                    setUpObserver()
                },
                currentYear,
                currentMonth,
                currentDay
            )

            // Set the minimum date to today
            datePickerDialog.datePicker.minDate = calendar.timeInMillis

            // Set the maximum date to one week from today
            calendar.add(Calendar.DAY_OF_MONTH, 7)
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis

            datePickerDialog.show()

        }

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
                                        binding.appointmentCard.visibility = View.VISIBLE
                                        doctorName = bookingData.data.doctorName

                                        val dateString = bookingData.data.appointmentDate;
                                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                        val date = dateFormat.parse(dateString)

                                        val calendar = Calendar.getInstance()
                                        calendar.time = date
                                        val dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
                                        val dateOfMonth = calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
                                        val year = calendar.get(Calendar.YEAR)

                                        binding.currentDate.text = "$dayOfWeek"
                                        binding.currentDay.text = dateOfMonth
                                        binding.currentYear.text = year.toString()

                                        binding.ttlApointTv.text = "Today "+bookingData.data.totalAppointments.toString()+" Appointments"
                                        binding.completedTv.text = "Completed  :"+bookingData.data.completedAppointments.toString()
                                        binding.cancelTv.text = "Pending  :"+bookingData.data.pendingAppointments.toString()

                                        if(bookingData.data.appointmentList.isNotEmpty()) {
                                            binding.nodata.visibility=View.GONE
                                            binding.tranRv.visibility=View.VISIBLE
                                            val bookingAdapter = BookingAdapter(
                                                bookingData.data.appointmentList,
                                                context,
                                                this@BookingAllFragment
                                            )
                                            binding.tranRv.adapter = bookingAdapter
                                            binding.tranRv.layoutManager = LinearLayoutManager(
                                                context,
                                                LinearLayoutManager.VERTICAL,
                                                false
                                            )
                                        }else{
                                            binding.nodata.visibility=View.VISIBLE
                                            binding.tranRv.visibility=View.GONE
                                        }
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

        val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_persistent, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetView)

        val actionButton: LinearLayout = bottomSheetView.findViewById(R.id.action_btn)
        val customerTV: TextView = bottomSheetView.findViewById(R.id.customerNameTextView)
        customerTV.text = "Patient Name : "+selectedData[0].itmName

        val completeButton: TextView = bottomSheetView.findViewById(R.id.completedButton)

        if (selectedData[0].positionVal?.equals(1) == true){
            actionButton.visibility=View.GONE
        }else{
            completeButton.setOnClickListener {
                findNavController().navigate(
                    BookingAllFragmentDirections.actionBookingFragmentToEstimateFragment(
                        selectedData[0].mastIDs,doctorName
                    )
                )
                bottomSheetDialog.dismiss()
            }
        }

        val callButton: TextView = bottomSheetView.findViewById(R.id.callButton)
        callButton.setOnClickListener {
            phoneNo = selectedData[0].mobile.toString()
            makePhoneCall(phoneNo)
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
    private fun makePhoneCall(phoneNumber: String) {
        if (phoneNumber.isNotEmpty()) {
            val permission = Manifest.permission.CALL_PHONE
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), PERMISSION_REQUEST_CALL_PHONE)
            } else {
                // Permission has already been granted

                val dialIntent = Intent(Intent.ACTION_CALL)
                dialIntent.data = Uri.parse("tel:$phoneNumber")

                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // Request the permission to make a phone call if not granted
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), PERMISSION_REQUEST_CALL_PHONE)
                }
                try {
                    startActivity(dialIntent)
                } catch (e: SecurityException) {
                    e.printStackTrace()
                    // Handle security exception
                }
            }
        } else {
            Log.e("PhoneCall", "Phone number is empty or null")
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CALL_PHONE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                makePhoneCall(phoneNo)
            } else {
                // Permission denied
                Const.shortToast(requireContext(), "Permission denied")
            }
        }
    }

}
