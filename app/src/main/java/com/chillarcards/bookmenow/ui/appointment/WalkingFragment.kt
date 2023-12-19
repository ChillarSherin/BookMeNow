package com.chillarcards.bookmenow.ui.appointment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentWalkingBinding
import com.chillarcards.bookmenow.ui.Staff
import com.chillarcards.bookmenow.ui.StaffService
import com.chillarcards.bookmenow.ui.adapter.SelectedAdapter
import com.chillarcards.bookmenow.ui.adapter.ServiceAdapter
import com.chillarcards.bookmenow.utills.Const


class WalkingFragment : Fragment() {

    lateinit var binding: FragmentWalkingBinding
    private val selectedItems = mutableListOf<StaffService>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalkingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()

        binding.generateBillBtn.visibility = View.VISIBLE

        val staffList = listOf(
            Staff(-1, "select staff"),
            Staff(0, "staff 1"),
            Staff(1, "staff 2"),
            Staff(2, "staff 3"),
            Staff(3, "staff 4"),
            Staff(4, "staff 5")
        )


        val items = listOf(
            StaffService(1, "hair cut", "800"),
            StaffService(2, "full hair pack", "800"),
            StaffService(3, "nail art", "800"),
            StaffService(4, "hair color", "9900"),
            StaffService(5, "blow dry", "400"),
            StaffService(6, "treatment", "800"),
            StaffService(7, "bridal pckg", "1800"),
            StaffService(8, "coloring  ", "600"),
            StaffService(9, "hair dressing  ", "600"),
            StaffService(10, "spa  ", "800")
        )

        val adapterService = ServiceAdapter(items) { clickedItem ->
            if (clickedItem.isSelected) {
                selectedItems.add(clickedItem)
                setEstimateItem()
            } else {
                selectedItems.remove(clickedItem)
                setEstimateItem()
            }
        }

        binding.serviceRv.layoutManager = GridLayoutManager(context,3)
        binding.serviceRv.adapter = adapterService

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, staffList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = spinnerAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedStaff = staffList[position]
                val selectedItem = selectedStaff.name
                val selectedItemId = selectedStaff.id
                if(selectedItemId== -1){
                    binding.servicesFrm.visibility=View.GONE
                }else{
                    binding.servicesFrm.visibility = View.VISIBLE
                    binding.totalFrm.visibility = View.GONE
                    Const.enableButton(binding.generateBillBtn)
                }
                Toast.makeText(requireContext(), "Selected Item: $selectedItem, ID: $selectedItemId", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when nothing is selected
            }
        }

        binding.generateBillBtn.setOnClickListener{
            findNavController().navigate(
                WalkingFragmentDirections.actionWalkingFragmentToEstimateFragment(
                )
            )
        }


    }

    private fun setEstimateItem(){
        binding.totalFrm.visibility = View.VISIBLE

        val adapter = SelectedAdapter(context,selectedItems)
        binding.estimateRv.layoutManager = LinearLayoutManager(context)
        binding.estimateRv.adapter = adapter

        val selectedItems = selectedItems.filter { it.isSelected }
        val sum = selectedItems.sumBy { it.total.toInt() }
        binding.totalSum.text = context?.getResources()?.getString(R.string.currency)+""+"$sum"
        if ( binding.totalSum.text.equals("₹0")){
            binding.totalSum.visibility = View.GONE
        }
    }

    private fun setToolbar() {
        binding.toolbar.toolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.toolbarTitle.text = getString(R.string.book_appointment)
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

}