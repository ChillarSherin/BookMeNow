package com.chillarcards.bookmenow.ui.webpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.databinding.FragmentAboutBinding

class AboutFragment : Fragment(R.layout.fragment_about) {

  private lateinit var binding:FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.getString("aboutURL")

        binding.webview.settings.javaScriptEnabled = true
        binding.webview.loadUrl(url ?: "")
    }


}