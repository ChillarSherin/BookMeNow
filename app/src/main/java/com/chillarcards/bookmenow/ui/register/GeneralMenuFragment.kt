package com.chillarcards.bookmenow.ui.register


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentMenuBinding
import com.chillarcards.bookmenow.ui.adapter.GeneralMenuAdapter
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills
import com.chillarcards.bookmenow.utills.CommonDBaseModel
import com.chillarcards.bookmenow.utills.Const
import com.chillarcards.bookmenow.utills.PrefManager
import com.chillarcards.bookmenow.utills.Status
import com.chillarcards.bookmenow.viewmodel.GeneralViewModel
import com.chillarcards.bookmenow.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class GeneralMenuFragment : Fragment(),IAdapterViewUtills {

    lateinit var binding: FragmentMenuBinding
    private val generalViewModel by viewModel<GeneralViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
                alertMsg(requireContext())
            }
        })
    }
    fun alertMsg(context: Context) {
        try {
            PrefManager(context)
            val builder = AlertDialog.Builder(context)
            // Create the AlertDialog

            //set title for alert dialog
            builder.setTitle(R.string.alert_heading)
            //set message for alert dialog
            builder.setMessage(R.string.pop_alert_message)
            builder.setIcon(R.mipmap.ic_launcher)
            builder.setCancelable(false)

            //performing positive action
            builder.setPositiveButton(context.getString(R.string.ok)) { _, _ ->
                findNavController().popBackStack()
            }
            builder.setNegativeButton(context.getString(R.string.cancel)) { _, _ ->
//                alertDialog.dismiss()
            }
            val alertDialog: AlertDialog = builder.create()

            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        } catch (e: Exception) {
            //e.printstackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        generalViewModel.run { getCategory() }
        setUpObserver()
      


    }
    private fun setUpObserver() {
        try {
            generalViewModel.categoryData.observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            hideProgress()
                            it.data?.let { menuData ->
                                when (menuData.statusCode) {
                                    200 -> {
                                        val menuAdapter = GeneralMenuAdapter(
                                            menuData.data.categoryList, requireContext(),this@GeneralMenuFragment
                                        )
                                        binding.menuRv.adapter = menuAdapter
                                        binding.menuRv.layoutManager =
                                            GridLayoutManager(context, 2)
                                    }
                                    else -> Const.shortToast(requireContext(), menuData.message)

                                }
                            }
                        }
                        Status.LOADING -> {
                            showProgress()
                        }
                        Status.ERROR -> {
                            hideProgress()

                        }
                    }
                }
            }
            generalViewModel.statusData.observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            hideProgress()
                            it.data?.let { menuData ->
                                when (menuData.statusCode) {
                                    200 -> {
                                        generalViewModel.run {
                                            getGeneralSetting()
                                        }
                                    }
                                    else -> Const.shortToast(requireContext(), menuData.message)

                                }
                            }
                        }
                        Status.LOADING -> {
                            showProgress()
                        }
                        Status.ERROR -> {
                            hideProgress()
                        }
                    }
                }
            }

        } catch (e: Exception) {
            Log.e("abc_otp", "setUpObserver: ", e)
        }
    }

    private fun showProgress() {
        binding.loginProgress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.loginProgress.visibility = View.GONE
    }

    override fun getAdapterPosition(
        Position: Int,
        ValueArray: ArrayList<CommonDBaseModel>,
        Mode: String?
    ) {
        TODO("Not yet implemented")
    }


}
