package com.chillarcards.bookmenow.ui.home

import android.content.Intent
import android.content.pm.PackageManager
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
import com.chillarcards.bookmenow.ui.Booking
import com.chillarcards.bookmenow.utills.CommonDBaseModel
import com.chillarcards.bookmenow.ui.Dummy
import com.chillarcards.bookmenow.ui.adapter.HorizontalAdapter
import com.chillarcards.bookmenow.ui.adapter.TransactionAdapter
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills
import com.chillarcards.bookmenow.ui.notification.NotificationViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class HomeFragment : Fragment(), IAdapterViewUtills {

    lateinit var binding: FragmentHomeBinding

    private lateinit var notificationViewModel: NotificationViewModel

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

        val pInfo =
            activity?.let { activity?.packageManager!!.getPackageInfo(it.packageName, PackageManager.GET_ACTIVITIES) }
        //  val versionName = pInfo?.versionName //Version Name

        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)

        val transItem = listOf(
            Booking("8.00 am", 1, "Muhammad Hussain",1),
            Booking("8.15 am", 2, "Vihaan Mehta ",1),
            Booking("8:30 am ", 3, "Williams Johnson",0),
            Booking("8:45 am", 4, "Daniel Jones ",1),
            Booking("9:00 am", 5, "Reyansh K",1),
            Booking("9:15 am", 6, "Aditya Joshi ",1),
            Booking("9:30 am", 7, "Layla Akhtar",0),
            Booking("9:45 am", 8, "Aara Farooq ",0),
            Booking("10:00 am", 9, "Amina Muhammad",0),
            Booking("10:15 am", 10, "Ibrahim MK ",0),
            Booking("10:30 am", 11, "Christopher Thomas ",0)
        )

        val dummyItem = listOf(
            Dummy("sajith", 1, "customer name a"),
            Dummy("sujith", 2, "customer name b"),
            Dummy("rony jak", 3, "customer name c"),
            Dummy("smith", 3, "customer name d"),
            Dummy("sam paul", 3, "customer name d")
        )

        val salesTopPicAdapter = HorizontalAdapter(
            dummyItem, context,this@HomeFragment)
        binding.topPicRv.adapter = salesTopPicAdapter
        binding.topPicRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val transactionAdapter = TransactionAdapter(
            transItem, context,this@HomeFragment)
        binding.tranRv.adapter = transactionAdapter
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
        binding.staffAll.setOnClickListener {
            try {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToStaffBookFragment(
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding.menuIcon.setOnClickListener {
            openOptionsMenu(it)

        }
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
        if(Mode.equals("VIEW")) {
            setBottomSheet(ValueArray)
        }  else if(Mode.equals("STAFFVIEW")) {
            val staffId: String = ValueArray[0].mastIDs.toString()

            findNavController().navigate(
                HomeFragmentDirections.actionHomeToStaffViewBookFragment(staffId)
            )
        }
    }

    private fun setBottomSheet(selectedData: ArrayList<CommonDBaseModel>) {

        val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_persistent, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetView)

        val actionButton: LinearLayout = bottomSheetView.findViewById(R.id.action_btn)
        val customerTV: TextView = bottomSheetView.findViewById(R.id.customerNameTextView)
        customerTV.text = "Patient Name : "+selectedData[0].valueStr1

        val completeButton: TextView = bottomSheetView.findViewById(R.id.completedButton)

        if (selectedData[0].positionVal?.equals(1) == true){
            actionButton.visibility=View.GONE
        }else{
            completeButton.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToEstimateFragment(
                        selectedData[0].mastIDs
                    )
                )
                bottomSheetDialog.dismiss()
            }
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
