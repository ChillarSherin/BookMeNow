package com.chillarcards.bookmenow.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentHomeBinding
import com.chillarcards.bookmenow.ui.Dummy
import com.chillarcards.bookmenow.ui.adapter.BookingAdapter
import com.chillarcards.bookmenow.ui.adapter.HorizontalAdapter
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills
import com.chillarcards.bookmenow.ui.notification.NotificationViewModel
import com.chillarcards.bookmenow.utills.CommonDBaseModel
import com.chillarcards.bookmenow.utills.Const
import com.chillarcards.bookmenow.utills.PrefManager
import com.chillarcards.bookmenow.utills.Status
import com.chillarcards.bookmenow.viewmodel.BookingViewModel
import com.chillarcards.bookmenow.viewmodel.RegisterViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment(), IAdapterViewUtills {

    lateinit var binding: FragmentHomeBinding

    private lateinit var notificationViewModel: NotificationViewModel
    private val bookingViewModel by viewModel<BookingViewModel>()
    private lateinit var prefManager: PrefManager
    private var doctorName =""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager(requireContext())

        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)

        getCurrentDate()

        val currentDate = Calendar.getInstance().time
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate)

        bookingViewModel.run {
            doctorID.value = prefManager.getDoctorId().toString()
            date.value = formattedDate
            getBookingList()
        }

        setUpObserver()

        binding.menuIcon.setOnClickListener {
            openOptionsMenu(it)

        }
    }

    private fun getCurrentDate(){
        val currentDate = Calendar.getInstance()
        val dayOfWeek = currentDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH)
        val dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH)
        val year = currentDate.get(Calendar.YEAR)
        binding.daydate.text = "$dayOfWeek $dayOfMonth"
        binding.year.text = year.toString()
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
                                        binding.logoIcon.text= "Hi "+bookingData.data.doctorName
                                        binding.ttlApointTv.text = "Today "+bookingData.data.totalAppointments.toString()+" Appointments"
                                        binding.completedTv.text = "Completed  :"+bookingData.data.completedAppointments.toString()
                                        binding.cancelTv.text = "Pending  :"+bookingData.data.pendingAppointments.toString()

                                        doctorName = bookingData.data.doctorName

                                        val bookingAdapter = BookingAdapter(
                                            bookingData.data.appointmentList, context,this@HomeFragment)
                                        binding.tranRv.adapter = bookingAdapter
                                        binding.tranRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                                        binding.bookingViewAll.setOnClickListener {
                                            try {
                                                findNavController().navigate(
                                                    HomeFragmentDirections.actionHomeFragmentToBookingFragment(
                                                    )
                                                )
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
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

    override fun getAdapterPosition(
        Position: Int,
        ValueArray: ArrayList<CommonDBaseModel>,
        Mode: String?
    ) {
        if(Mode.equals("VIEW")) {
            setBottomSheet(ValueArray)
        }  else if(Mode.equals("BOOKVIEW")) {
            val bookingId: String = ValueArray[0].mastIDs.toString()
            findNavController().navigate(
                HomeFragmentDirections.actionHomeToStaffViewBookFragment(bookingId)
            )
        }
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
                    HomeFragmentDirections.actionHomeFragmentToEstimateFragment(
                        selectedData[0].mastIDs,doctorName
                    )
                )
                bottomSheetDialog.dismiss()
            }
        }

        val callButton: TextView = bottomSheetView.findViewById(R.id.callButton)
        callButton.setOnClickListener {
            val phoneNumber = "tel:" + selectedData[0].mobile
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse(phoneNumber)
            if (dialIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(dialIntent)
            }
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()

    }

    private fun openOptionsMenu(view: View) {

        val contextWrapper = ContextThemeWrapper(requireContext(), R.style.PopupMenuStyle)
        val popup = PopupMenu(contextWrapper, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_top, popup.menu)

        val notificationItem = popup.menu.findItem(R.id.menu_notification)
        val notificationCount = getNotificationCount()

        if (notificationCount > 0) {
            // Show red dot or notification count
            notificationItem.setIcon(R.drawable.ic_notification_red_dot)
        } else {
            // Hide red dot or notification count
            notificationItem.setIcon(R.drawable.ic_notification)
            notificationItem.actionView = null
        }

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_notification -> {
                    findNavController().navigate(R.id.action_homeFragment_to_NotificationFragment)
                    true
                }
                else -> false
            }
        }

        popup.show()
    }

    private fun getNotificationCount(): Int {
        // Retrieve the count from the notification repository or any other source
        return notificationViewModel.getNotifications().size
    }

}
