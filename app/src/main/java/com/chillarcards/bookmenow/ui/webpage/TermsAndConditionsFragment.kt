package com.chillarcards.bookmenow.ui.webpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentAboutBinding
import com.chillarcards.bookmenow.databinding.FragmentTermsAndConditionsBinding


class TermsAndConditionsFragment : Fragment(R.layout.fragment_terms_and_conditions) {
    private lateinit var binding: FragmentTermsAndConditionsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentTermsAndConditionsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.getString("termsURL")
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.loadUrl(url ?: "")
    }


}