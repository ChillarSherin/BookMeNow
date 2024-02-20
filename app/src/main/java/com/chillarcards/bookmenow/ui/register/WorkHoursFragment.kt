package com.chillarcards.bookmenow.ui.register


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentWorkHoursBinding
import com.chillarcards.bookmenow.ui.adapter.WorkHoursAdapter
import com.chillarcards.bookmenow.utills.Const
import com.chillarcards.bookmenow.utills.PrefManager
import com.chillarcards.bookmenow.utills.Status
import com.chillarcards.bookmenow.viewmodel.RegisterViewModel
import com.chillarcards.bookmenow.viewmodel.WorkViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class WorkHoursFragment : Fragment() {

    lateinit var binding: FragmentWorkHoursBinding
    private val workViewModel by viewModel<WorkViewModel>()
    private lateinit var prefManager: PrefManager
    lateinit var workHoursAdapter: WorkHoursAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_work_hours, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Const.enableButton(binding.confirmBtn)
        setToolbar()

        workViewModel.run {
            doctorID.value = "1"
            getWork()
        }

        setUpObserver()
        binding.confirmBtn.setOnClickListener {
//            findNavController().navigate(
//                WorkHoursFragmentDirections.actionTimeFragmentToHomeFragment()
//            )
            findNavController().popBackStack()

        }
    }

    private fun setToolbar() {
        binding.toolbar.toolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.toolbarTitle.text = getString(R.string.time_head)
    }

    private fun setUpObserver() {
        try {
            workViewModel.workData.observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            hideProgress()
                            it.data?.let { workData ->
                                when (workData.statusCode) {
                                    200 -> {
                                        if (workData.data.result.isNotEmpty()) {
                                            workHoursAdapter = WorkHoursAdapter(requireContext(), workData.data.result)
                                            binding.recycler.adapter = workHoursAdapter
                                            binding.recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                                        }
                                        else {
                                            // Handle the case when workData or its properties are null or empty
                                            Const.shortToast(requireContext(),"Please wait")
                                        }
                                    }
                                    403 -> {
                                        prefManager.setRefresh("1")
                                        val authViewModel by viewModel<RegisterViewModel>()
                                        Const.getNewTokenAPI(
                                            requireContext(),
                                            authViewModel,
                                            viewLifecycleOwner
                                        )

                                    }
                                    else -> Const.shortToast(requireContext(), workData.message)
                                }
                            }
                        }
                        Status.LOADING -> {
                            showProgress()
                        }
                        Status.ERROR -> {
                            hideProgress()
                            prefManager.setRefresh("1")
                            val authViewModel by viewModel<RegisterViewModel>()
                            Const.getNewTokenAPI(
                                requireContext(),
                                authViewModel,
                                viewLifecycleOwner
                            )

//                            profileViewModel.run {
//                                mob.value = prefManager.getMobileNo()
//                                getProfile()
//                            }
//                            setUpObserver()
                        }
                    }
                }
            }


        } catch (e: Exception) {
            Log.e("abc_otp", "setUpObserver: ", e)
        }
    }
    private fun showProgress() {
        binding.confirmBtn.visibility = View.INVISIBLE
        binding.otpProgress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.confirmBtn.visibility = View.VISIBLE
        binding.otpProgress.visibility = View.GONE
    }
}
