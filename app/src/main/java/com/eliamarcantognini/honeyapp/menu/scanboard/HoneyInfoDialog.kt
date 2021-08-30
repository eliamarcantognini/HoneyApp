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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
            Log.d("AAAAAAAA", viewModel.scan.value?.type!!)
            honeyNameTxt.text = viewModel.scan.value?.type
            honeyDescTxt.text = viewModel.scan.value?.desc
            firmTxt.text = viewModel.scan.value?.firm
            firmAddrTxt.text =
                viewModel.scan.value?.addr
            val cityAndCap = viewModel.scan.value?.city + ", " + viewModel.scan.value?.cap
            firmCityCapTxt.text = cityAndCap

            loadStarsLogic()

            // If the site is given, it renders the button to open the browser with the url given
            viewModel.scan.value?.site?.let {
                siteBtn.visibility = View.VISIBLE
                siteBtn.setOnClickListener { _ ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https:$it")))
                }
            }
            // Dial intent which opens the dialer with the given telephone number
            callBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:" + viewModel.scan.value?.num)
                }
                startActivity(intent)
            }
            // Location intent which opens a map app with the address given
            locationBtn.setOnClickListener {
                val geo = Uri.parse(
                    "geo:0,0?q=" + Uri.encode(viewModel.scan.value?.addr) + Uri.encode(", ") + Uri.encode(
                        viewModel.scan.value?.city
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

    private fun loadStarsLogic()
    {
        binding.apply {
            when (viewModel.scan.value?.stars) {
                1 -> fillOneStar()
                2 -> fillTwoStar()
                3 -> fillThreeStar()
                4 -> fillFourStar()
                5 -> fillFiveStar()
            }
            val db = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val userId = auth.currentUser!!.uid
            val scan = viewModel.scan.value!!
            val token = scan.token!!
            val scansRef = db.collection("scans").document(userId).collection("data").document(token)
            starInfo.starImg1.setOnClickListener {
                fillOneStar()
                scansRef.get().addOnSuccessListener { scansRef.update("stars", 1) }
                viewModel.scan.value!!.stars = 1
            }
            starInfo.starImg2.setOnClickListener {
                fillTwoStar()
                scansRef.get().addOnSuccessListener { scansRef.update("stars", 2) }
                viewModel.scan.value!!.stars = 2
            }
            starInfo.starImg3.setOnClickListener {
                fillThreeStar()
                scansRef.get().addOnSuccessListener { scansRef.update("stars", 3) }
                viewModel.scan.value!!.stars = 3
            }
            starInfo.starImg4.setOnClickListener {
                fillFourStar()
                scansRef.get().addOnSuccessListener { scansRef.update("stars", 4) }
                viewModel.scan.value!!.stars = 4
            }
            starInfo.starImg5.setOnClickListener {
                fillFiveStar()
                scansRef.get().addOnSuccessListener { scansRef.update("stars", 5) }
                viewModel.scan.value!!.stars = 5
            }
        }
    }

    private fun fillOneStar()
    {
        binding.apply {
            starInfo.starImg1.setImageResource(R.drawable.ic_star_primarydark_24)
            starInfo.starImg2.setImageResource(R.drawable.star_outline_primarydark_24)
            starInfo.starImg3.setImageResource(R.drawable.star_outline_primarydark_24)
            starInfo.starImg4.setImageResource(R.drawable.star_outline_primarydark_24)
            starInfo.starImg5.setImageResource(R.drawable.star_outline_primarydark_24)
        }
    }

    private fun fillTwoStar()
    {
        binding.apply {
            starInfo.starImg1.setImageResource(R.drawable.ic_star_primarydark_24)
            starInfo.starImg2.setImageResource(R.drawable.ic_star_primarydark_24)
            starInfo.starImg3.setImageResource(R.drawable.star_outline_primarydark_24)
            starInfo.starImg4.setImageResource(R.drawable.star_outline_primarydark_24)
            starInfo.starImg5.setImageResource(R.drawable.star_outline_primarydark_24)
        }
    }

    private fun fillThreeStar()
    {
        binding.apply {
            starInfo.starImg1.setImageResource(R.drawable.ic_star_primarydark_24)
            starInfo.starImg2.setImageResource(R.drawable.ic_star_primarydark_24)
            starInfo.starImg3.setImageResource(R.drawable.ic_star_primarydark_24)
            starInfo.starImg4.setImageResource(R.drawable.star_outline_primarydark_24)
            starInfo.starImg5.setImageResource(R.drawable.star_outline_primarydark_24)
        }
    }

    private fun fillFourStar()
    {
        binding.apply {
            starInfo.starImg1.setImageResource(R.drawable.ic_star_primarydark_24)
            starInfo.starImg2.setImageResource(R.drawable.ic_star_primarydark_24)
            starInfo.starImg3.setImageResource(R.drawable.ic_star_primarydark_24)
            starInfo.starImg4.setImageResource(R.drawable.ic_star_primarydark_24)
            starInfo.starImg5.setImageResource(R.drawable.star_outline_primarydark_24)
        }
    }

    private fun fillFiveStar()
    {
        binding.apply {
            starInfo.starImg1.setImageResource(R.drawable.ic_star_primarydark_24)
            starInfo.starImg2.setImageResource(R.drawable.ic_star_primarydark_24)
            starInfo.starImg3.setImageResource(R.drawable.ic_star_primarydark_24)
            starInfo.starImg4.setImageResource(R.drawable.ic_star_primarydark_24)
            starInfo.starImg5.setImageResource(R.drawable.ic_star_primarydark_24)
        }
    }

}