package com.chillarcards.bookmenow.ui.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentGeneralBinding
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills
import com.chillarcards.bookmenow.utills.CommonDBaseModel

class GeneralFragment : Fragment(), IAdapterViewUtills {

    lateinit var binding: FragmentGeneralBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_general, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()

        binding.intervalFrm.setEndIconOnClickListener {
            binding.interval.setText("")
        }
        binding.profile.setOnClickListener{
            findNavController().navigate(
                GeneralFragmentDirections.actionGeneralFragmentToRegisterFragment(
                )
            )
        }
        binding.workingFrm.setOnClickListener{
            findNavController().navigate(
                GeneralFragmentDirections.actionGeneralFragmentToTimeFragment(
                )
            )
        }
        binding.addStaff.setOnClickListener{
            findNavController().navigate(
                GeneralFragmentDirections.actionGeneralFragmentToModuleStaffFragment(
                )
            )
        }
        binding.addService.setOnClickListener{
            findNavController().navigate(
                GeneralFragmentDirections.actionGeneralFragmentToModuleServiceFragment(
                )
            )
        }
    }

    private fun setToolbar() {
        binding.toolbar.toolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.toolbarTitle.text = getString(R.string.general)
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