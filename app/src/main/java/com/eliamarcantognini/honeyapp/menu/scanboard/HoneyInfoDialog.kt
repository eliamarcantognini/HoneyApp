package com.eliamarcantognini.honeyapp.menu.scanboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.eliamarcantognini.honeyapp.R
import com.eliamarcantognini.honeyapp.databinding.HoneyInfoDialogBinding

class HoneyInfoDialog : DialogFragment() {


    private lateinit var viewModel: ScanboardViewModel
    private var _binding: HoneyInfoDialogBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HoneyInfoDialogBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ScanboardViewModel::class.java)
        binding.apply {
            honeyImg.setImageResource(R.drawable.honey)
            Log.d("AAAAAAAA", viewModel.honey.value?.type!!)
            honeyNameTxt.text = viewModel.honey.value?.type
            honeyDescTxt.text = viewModel.honey.value?.desc
            firmTxt.text = viewModel.honey.value?.firm
            firmAddrTxt.text =
                viewModel.honey.value?.addr
            val cityAndCap = viewModel.honey.value?.city + ", " + viewModel.honey.value?.cap
            firmCityCapTxt.text = cityAndCap
            // If the site is given, it renders the button to open the browser with the url given
            viewModel.honey.value?.site?.let {
                siteBtn.visibility = View.VISIBLE
                siteBtn.setOnClickListener { _ ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https:$it")))
                }
            }
            // Dial intent which opens the dialer with the given telephone number
            callBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:" + viewModel.honey.value?.num)
                }
                startActivity(intent)
            }
            // Location intent which opens a map app with the address given
            locationBtn.setOnClickListener {
                val geo = Uri.parse(
                    "geo:0,0?q=" + Uri.encode(viewModel.honey.value?.addr) + Uri.encode(", ") + Uri.encode(
                        viewModel.honey.value?.city
                    )
                )
                startActivity(Intent(Intent.ACTION_VIEW, geo))
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.80).toInt()
//        val height = (resources.displayMetrics.heightPixels * 0.80).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}