package com.chillarcards.bookmenow.ui.staffs


import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentViewAllBinding
import com.chillarcards.bookmenow.ui.DummyStaff
import com.chillarcards.bookmenow.ui.adapter.StaffAdapter
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills
import com.chillarcards.bookmenow.utills.CommonDBaseModel


class StaffBookFragment : Fragment(), IAdapterViewUtills {

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
        binding.headTran.text = getString(R.string.staff_head)

        val transItem = listOf(
            DummyStaff(1,"Sajith",  "10"),
            DummyStaff(2,"Sujith",  "4"),
            DummyStaff(3,"Ram Kumar",  "1"),
            DummyStaff(4,"Shilpa",  "8"),
            DummyStaff(5,"Ramu",  "4"),
            DummyStaff(6,"John",  "10"),
            DummyStaff(7,"Amith Khan",  "22"),
            DummyStaff(8,"Damaro",  "12")
        )

        val staffAdapter = StaffAdapter(
            transItem, this@StaffBookFragment,context)

        binding.tranRv.adapter = staffAdapter
        binding.tranRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    }

    private fun setToolbar() {
        binding.toolbar.toolbarTitle.text = getString(R.string.staff_head)
        binding.toolbar.toolbarBack.setOnClickListener {
            findNavController().popBackStack()
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
        if(Mode.equals("VIEW")){
            val staffId: String = ValueArray[0].mastIDs.toString()

            findNavController().navigate(
                StaffBookFragmentDirections.actionStaffFragmentToBookingFragment(staffId)
            )
        }
    }
}
