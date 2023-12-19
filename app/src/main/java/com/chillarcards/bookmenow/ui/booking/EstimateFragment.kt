package com.chillarcards.bookmenow.ui.booking

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentEstimateBinding
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills
import com.chillarcards.bookmenow.utills.BottomSheetFragment
import com.chillarcards.bookmenow.utills.CommonDBaseModel


class EstimateFragment : Fragment(), IAdapterViewUtills {

    lateinit var binding: FragmentEstimateBinding
    private var mediaPlayer: MediaPlayer? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEstimateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pInfo =
            activity?.let { activity?.packageManager!!.getPackageInfo(it.packageName, PackageManager.GET_ACTIVITIES) }

        setToolbar()

        binding.cashButton.setOnClickListener {
            // Initialize MediaPlayer in onCreate or another appropriate method
            mediaPlayer = MediaPlayer.create(context, R.raw.bell_audio)
            if (!mediaPlayer!!.isPlaying) {
                mediaPlayer!!.start()
            }
            findNavController().navigate(
                EstimateFragmentDirections.actionEstimateFragmentToSuccessFragment(
                )
            )
        }
        binding.upiButton.setOnClickListener {
            // Initialize MediaPlayer in onCreate or another appropriate method
            mediaPlayer = MediaPlayer.create(context, R.raw.bell_audio)
            if (!mediaPlayer!!.isPlaying) {
                mediaPlayer!!.start()
            }
            findNavController().navigate(
                EstimateFragmentDirections.actionEstimateFragmentToSuccessFragment(
                )
            )
        }
    }

    private fun setToolbar() {
        binding.toolbar.toolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setBottomSheet(selectedData: ArrayList<CommonDBaseModel>) {
//        binding.bottomView.visibility= View.VISIBLE
//        val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_persistent, null)
//        val bottomSheetDialog = BottomSheetDialog(requireContext())
//        bottomSheetDialog.setContentView(bottomSheetView)
//        bottomSheetDialog.show()

        val bottomSheetFragment = BottomSheetFragment(selectedData)
        bottomSheetFragment.show((context as AppCompatActivity).supportFragmentManager, bottomSheetFragment.tag)


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

    fun createLinear(){

//        for (item in transItem) {
//            val newLinearLayout = LinearLayout(context)
//            newLinearLayout.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            newLinearLayout.orientation = LinearLayout.HORIZONTAL
//            newLinearLayout.weightSum = 2f
//
//            val slnoTextView = TextView(context)
//            slnoTextView.layoutParams = LinearLayout.LayoutParams(
//                0,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                0.4f
//            )
//            slnoTextView.text = item.id.toString()
//            slnoTextView.gravity = Gravity.CENTER
//            slnoTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_black))
//            slnoTextView.setTypeface(null, Typeface.BOLD)
//
//            val serviceNameTextView = TextView(context)
//            serviceNameTextView.layoutParams = LinearLayout.LayoutParams(
//                0,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                1f
//            )
//            serviceNameTextView.text = item.name
//            serviceNameTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_black))
//
//            val serviceRateTextView = TextView(context)
//            serviceRateTextView.layoutParams = LinearLayout.LayoutParams(
//                0,
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                0.6f
//            )
//            serviceRateTextView.gravity = Gravity.CENTER
//            serviceRateTextView.text = item.total
//            serviceRateTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_black))
//
//            newLinearLayout.addView(slnoTextView)
//            newLinearLayout.addView(serviceNameTextView)
//            newLinearLayout.addView(serviceRateTextView)
//
//            binding.parentLayout.addView(newLinearLayout)
//        }

    }


}
