package com.chillarcards.bookmenow.ui.webpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentRefundPolicyBinding

class RefundPolicyFragment : Fragment(R.layout.fragment_refund_policy) {

private lateinit var binding:FragmentRefundPolicyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       binding=FragmentRefundPolicyBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.getString("refundURL")
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.loadUrl(url ?: "")
    }


}