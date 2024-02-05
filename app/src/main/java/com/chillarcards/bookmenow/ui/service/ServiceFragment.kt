package com.chillarcards.bookmenow.ui.service


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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentModuleStaffsBinding
import com.chillarcards.bookmenow.ui.DummyService
import com.chillarcards.bookmenow.ui.adapter.AllServiceAdapter
import com.chillarcards.bookmenow.ui.home.HomeFragmentDirections
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills
import com.chillarcards.bookmenow.utills.CommonDBaseModel
import com.chillarcards.bookmenow.utills.Const
import com.google.android.material.bottomsheet.BottomSheetDialog


class ServiceFragment : Fragment(), IAdapterViewUtills {

    lateinit var binding: FragmentModuleStaffsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModuleStaffsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pInfo =
            activity?.let { activity?.packageManager!!.getPackageInfo(it.packageName, PackageManager.GET_ACTIVITIES) }

        setToolbar()
        binding.headTran.text = getString(R.string.service_view_msg)
        binding.headOne.text = "Service Name"
        binding.headTwo.text = "Rate"
        binding.headThree.text = "Time Required"
        binding.headFour.text = "Status"

        val transItem = listOf(
            DummyService(1,"Hair cut",  "500","20"),
            DummyService(2,"Hair massage",  "400","10"),
            DummyService(3,"Hair Wash",  "100","5"),
            DummyService(4,"Hair Colour",  "800","60"),
            DummyService(5,"Nail Art",  "400","20"),
            DummyService(6,"Full Spa",  "100","50"),
            DummyService(7,"Facial",  "220","20"),
            DummyService(8,"Pedicure",  "502","30")
        )

        val staffAdapter = AllServiceAdapter(
            transItem, this@ServiceFragment,context)
        binding.confirmBtn.setText(R.string.add_service_head)
        Const.enableButton(binding.confirmBtn)
        binding.staffRv.adapter = staffAdapter
        binding.staffRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.confirmBtn.setOnClickListener {
            setBottomSheet("")
        }
    }

    private fun setToolbar() {
        binding.toolbar.toolbarTitle.text = getString(R.string.service_view)
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
            setBottomSheet(staffId)
        }
    }

    private fun setBottomSheet(staffId: String) {

        val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_service, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetView)

        val actionButton: LinearLayout = bottomSheetView.findViewById(R.id.action_btn)
//        val customerTV: TextView = bottomSheetView.findViewById(R.id.customerNameTextView)
//        customerTV.text = "Patient Name : "

        val completeButton: TextView = bottomSheetView.findViewById(R.id.completedButton)
        completeButton.setOnClickListener {
//            findNavController().navigate(
//                HomeFragmentDirections.actionHomeFragmentToEstimateFragment(
//                    selectedData[0].mastIDs
//                )
//            )
            bottomSheetDialog.dismiss()
        }
        val callButton: TextView = bottomSheetView.findViewById(R.id.callButton)
        callButton.setOnClickListener {
          //  val phoneNumber = "tel:" + selectedData[0].mastIDs
            val phoneNumber = "tel:9777777777"
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse(phoneNumber)
            if (dialIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(dialIntent)
            }
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()

        }


}
