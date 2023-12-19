package com.chillarcards.bookmenow.utills

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.ui.home.HomeFragmentDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment(private val selectedData: ArrayList<CommonDBaseModel>) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_persistent, container, false)
        dialog?.setCanceledOnTouchOutside(true)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val customerTV: TextView = view.findViewById(R.id.customerNameTextView)
        customerTV.text = selectedData[0].valueStr1


        val completeButton: TextView = view.findViewById(R.id.completedButton)
        completeButton.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToEstimateFragment(
                )
            )
            dismiss()
        }

        val callButton: TextView = view.findViewById(R.id.callButton)
        callButton.setOnClickListener {
            val phoneNumber = "tel:" + selectedData[0].mastIDs
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse(phoneNumber)
            if (dialIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(dialIntent)
            }
            dismiss()
        }

    }
}