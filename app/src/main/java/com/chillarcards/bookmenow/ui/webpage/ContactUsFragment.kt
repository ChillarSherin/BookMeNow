package com.chillarcards.bookmenow.ui.webpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentContactUsBinding

class ContactUsFragment : Fragment(R.layout.fragment_contact_us) {
 private lateinit var binding:FragmentContactUsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentContactUsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.getString("contactURL")
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.loadUrl(url ?: "")
    }


}