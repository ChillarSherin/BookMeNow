package com.chillarcards.bookmenow.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentReportBinding
import com.chillarcards.bookmenow.ui.Dummy
import com.chillarcards.bookmenow.ui.adapter.HorizontalAdapter
import com.chillarcards.bookmenow.ui.adapter.PaymentAdapter
import com.chillarcards.bookmenow.ui.adapter.StaffReportAdapter
import com.chillarcards.bookmenow.ui.adapter.TransactionAdapter
import com.chillarcards.bookmenow.ui.home.HomeFragmentDirections
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills
import com.chillarcards.bookmenow.utills.CommonDBaseModel

class ReportFragment : Fragment(), IAdapterViewUtills {

    lateinit var binding: FragmentReportBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()


        val transItem = listOf(
            Dummy("8.00 am", 5, "Muhammad Hussain"),
            Dummy("8.15 am", 2, "Vihaan Mehta "),
            Dummy("8:45 am", 8, "Daniel Jones "),
            Dummy("9:00 am", 8, "Reyansh K"),
            Dummy("9:15 am", 8, "Aditya Joshi "),

        )
        val transactionAdapter = PaymentAdapter(
            transItem, context,this@ReportFragment)

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
     }

    private fun setToolbar() {
        binding.toolbar.toolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.toolbarTitle.text = getString(R.string.sales_report)
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
}