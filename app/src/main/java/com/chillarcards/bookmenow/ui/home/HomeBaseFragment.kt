package com.chillarcards.bookmenow.ui.home

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.chillarcards.bookmenow.MainActivity
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentHomeBaseBinding
import com.chillarcards.bookmenow.utills.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Calendar
import java.util.Locale

class HomeBaseFragment : Fragment() {

    lateinit var binding: FragmentHomeBaseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_base, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.inner_host_nav) as NavHostFragment
        val navController = navHostFragment.navController

        binding.report.setOnClickListener {
            navController.navigate(R.id.reportFragment)
        }
        binding.setting.setOnClickListener {
            navController.navigate(R.id.generalFragment)
        }
        binding.addProfile.setOnClickListener {
            navController.navigate(R.id.profileFragment)
        }

        binding.walkBook.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    // Handle the selected date
                    val selectedDate = "$year-${(month + 1).toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
                    // TODO: Do something with the selected date (e.g., display it)
                   //   navController.navigate(R.id.BookingFragment)

                    // Navigate to BookingFragment with selected date using Safe Args
                    val action = HomeFragmentDirections.actionHomeFragmentToBookingFragment(selectedDate,)
                    navController.navigate(action)

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

        binding.logout.setOnClickListener {
            setBottomSheet()
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.BookingFragment, R.id.StaffBookFragment, R.id.estimateFragment,
                R.id.successFragment,  R.id.walk_book_Fragment , R.id.reportFragment ,
                R.id.generalFragment, R.id.profileFragment, R.id.TimeFragment ,
                R.id.RegisterFragment, R.id.StaffFragment , R.id.AddStaffFragment,
                R.id.BankFragment , R.id.StaffModuleFragment, R.id.ServiceModuleFragment  -> {
                    binding.bottomMenu.visibility = View.GONE
                }
                else -> {
                    binding.bottomMenu.visibility = View.VISIBLE
                }
            }
        }


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

//        val preferences = getSharedPreferences("user_data", MODE_PRIVATE)
//        val editor = preferences.edit()
//        editor.clear()
//        editor.apply()

        val prefManager = PrefManager(requireContext())
        prefManager.clearAll()
    }

}