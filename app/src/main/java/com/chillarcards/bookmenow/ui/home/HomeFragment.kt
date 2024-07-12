package com.chillarcards.bookmenow.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.data.model.EntityDetail
import com.chillarcards.bookmenow.databinding.FragmentHomeBinding
import com.chillarcards.bookmenow.ui.adapter.BookingAdapter
import com.chillarcards.bookmenow.ui.adapter.ClinicAdapter
import com.chillarcards.bookmenow.ui.booking.EstimateFragmentDirections
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
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment(), IAdapterViewUtills {

    lateinit var binding: FragmentHomeBinding
    private val args: HomeFragmentArgs by navArgs()
    private lateinit var notificationViewModel: NotificationViewModel
    private val bookingViewModel by viewModel<BookingViewModel>()
    private lateinit var prefManager: PrefManager
    private var doctorName =""
    private var phoneNo =""
    private val PERMISSION_REQUEST_CALL_PHONE = 1
    private var formattedDate = ""
    private var shareLink = ""
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
        formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate)

        bookingViewModel.run {
            doctorID.value = prefManager.getDoctorId().toString()
            date.value = formattedDate
            entityId.value = if (prefManager.getEntityId() == "-1") "" else prefManager.getEntityId()
            getBookingList()
        }

        bookingViewModel.run {
            getShareLink()
        }

        setUpObserver()
        getUpObserver()

        binding.menuIcon.setOnClickListener {
            openOptionsMenu(it)
        }

        binding.share.setOnClickListener {
            if(shareLink!="") {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareLink)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            }else{
                getUpObserver()
                Const.shortToast(requireContext(),"Something went wrong! Try Again")
            }
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
                                        binding.ttlApointTv.text = "Today "+bookingData.data.totalBooking.toString()+" Appointments"
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

                                        if(bookingData.data.entityDetails.isNotEmpty()) {
                                            if(bookingData.data.entityDetails.size>1){
                                                binding.topStaffFrame.visibility=View.VISIBLE
                                                val entityDataMastCols: List<EntityDetail>
                                                val entityMastTemp  = bookingData.data.entityDetails.toMutableList()
                                                entityMastTemp.add(0, EntityDetail( -1,"View all","",0,1))
                                                entityDataMastCols  = entityMastTemp

                                                val salesTopPicAdapter = ClinicAdapter(
                                                    entityDataMastCols,
                                                    requireContext(),
                                                    this@HomeFragment
                                                )

                                                binding.topPicRv.adapter = salesTopPicAdapter
                                                binding.topPicRv.layoutManager = LinearLayoutManager(
                                                    context,
                                                    LinearLayoutManager.HORIZONTAL,
                                                    false
                                                )
                                            }else{
                                                binding.topStaffFrame.visibility=View.GONE
                                            }
                                        }else{
                                            binding.topStaffFrame.visibility=View.GONE
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
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("abc_otp", "setUpObserver: ", e)
        }
    }
   private fun getUpObserver() {
        try {
            bookingViewModel.bookLinkData.observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            hideProgress()
                            it.data?.let { booklnkData ->
                                when (booklnkData.statusCode) {
                                    200 -> {
                                        shareLink = booklnkData.data
                                    }
                                    else -> Const.shortToast(requireContext(), booklnkData.message)
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
        }
        else if(Mode.equals("BOOKVIEW")) {
            val bookingId: String = ValueArray[0].mastIDs.toString()
            findNavController().navigate(
                HomeFragmentDirections.actionHomeToStaffViewBookFragment(bookingId)
            )
        }
        else if(Mode.equals("VIEWBOOKING")) {
            if(ValueArray[0].mastIDs=="-1"){
                prefManager.setEntityId(ValueArray[0].mastIDs.toString())

                bookingViewModel.run {
                    doctorID.value = prefManager.getDoctorId().toString()
                    date.value = formattedDate
                    entityId.value = ""
                    getBookingList()
                }
            }else{
                prefManager.setEntityId(ValueArray[0].mastIDs.toString())
                bookingViewModel.run {
                    doctorID.value = prefManager.getDoctorId().toString()
                    date.value = formattedDate
                    entityId.value = prefManager.getEntityId()
                    getBookingList()
                }
            }
            setUpObserver()
        }
    }

    private fun setBottomSheet(selectedData: ArrayList<CommonDBaseModel>) {
        var mediaPlayer: MediaPlayer?

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
                // Initialize MediaPlayer in onCreate or another appropriate method
                mediaPlayer = MediaPlayer.create(context, R.raw.bell_audio)
                if (!mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.start()
                }
                bookingViewModel.run {
                    bookingId.value = selectedData[0].mastIDs
                    getConfirmBooking()
                }
                viewUpObserver()
//                findNavController().navigate(
//                    HomeFragmentDirections.actionHomeFragmentToEstimateFragment(
//                        selectedData[0].mastIDs,doctorName
//                    )
//                )
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
    private fun viewUpObserver() {
        try {
            bookingViewModel.bookStatusData.observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            it.data?.let { bookStatusData ->
                                when (bookStatusData.statusCode) {
                                    200 -> {

                                        findNavController().navigate(
                                            HomeFragmentDirections.actionEstimateFragmentToSuccessFragment(
                                            )
                                        )

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
                                    else -> Const.shortToast(requireContext(), bookStatusData.message)

                                }
                            }
                        }
                        Status.LOADING -> {
                        }
                        Status.ERROR -> {
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

    private fun openOptionsMenu(view: View) {

        val contextWrapper = ContextThemeWrapper(requireContext(), R.style.PopupMenuStyle)
        val popup = PopupMenu(contextWrapper, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_top, popup.menu)

        val version = popup.menu.findItem(R.id.version)
        val pInfo =
            activity?.let { activity?.packageManager!!.getPackageInfo(it.packageName, PackageManager.GET_ACTIVITIES) }
        val versionName = pInfo?.versionName //Version Name
        version.title = "Version $versionName"

//        val notificationItem = popup.menu.findItem(R.id.menu_notification)
//        val notificationCount = getNotificationCount()
//
//        if (notificationCount > 0) {
//            // Show red dot or notification count
//            notificationItem.setIcon(R.drawable.ic_notification_red_dot)
//        } else {
//            // Hide red dot or notification count
//            notificationItem.setIcon(R.drawable.ic_notification)
//            notificationItem.actionView = null
//        }

//        popup.setOnMenuItemClickListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.menu_notification -> {
//                    findNavController().navigate(R.id.action_homeFragment_to_NotificationFragment)
//                    true
//                }
//                else -> false
//            }
//        }

        popup.show()
    }

    private fun getNotificationCount(): Int {
        // Retrieve the count from the notification repository or any other source
        return notificationViewModel.getNotifications().size
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
