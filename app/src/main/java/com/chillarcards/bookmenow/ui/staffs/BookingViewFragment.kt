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
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills
import com.chillarcards.bookmenow.utills.CommonDBaseModel
import com.google.android.material.bottomsheet.BottomSheetDialog


class BookingViewFragment : Fragment(), IAdapterViewUtills {

    lateinit var binding: FragmentViewAllBinding

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
        val pInfo =
            activity?.let { activity?.packageManager!!.getPackageInfo(it.packageName, PackageManager.GET_ACTIVITIES) }

        setToolbar()
        binding.headTran.text = getString(R.string.book_head)

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

        val bookingAdapter = BookingAdapter(
            transItem, context,this@BookingViewFragment)

        binding.tranRv.adapter = bookingAdapter
        binding.tranRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    }

    private fun setToolbar() {
        binding.toolbar.toolbarTitle.text = getString(R.string.booking_head)
        binding.toolbar.toolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
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
