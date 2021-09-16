package com.eliamarcantognini.honeyapp.menu.scanboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eliamarcantognini.honeyapp.R
import com.eliamarcantognini.honeyapp.firestore.Scan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HoneyScanAdapter(private val onScanListener: OnScanListener) :
    RecyclerView.Adapter<HoneyScanHolder>() {

    private val scansList = ArrayList<Scan>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoneyScanHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.honey_list_card, parent, false)
        return HoneyScanHolder(view, onScanListener)
    }

    override fun onBindViewHolder(holder: HoneyScanHolder, position: Int) {
        holder.scan = scansList[position]
        holder.description.text = scansList[position].desc
        holder.firm.text = scansList[position].firm
        holder.name.text = scansList[position].type
        holder.image.setImageResource(scansList[position].resID!!)
//        holder.image.scaleType = ImageView.ScaleType.FIT_CENTER
        holder.starNumber.text = scansList[position].stars.toString()

        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        val token = holder.scan.token
        val scansRef = db.collection("scans").document(userId).collection("data").document(token!!)
        scansRef.get().addOnSuccessListener {
            val stars = it.toObject(Scan::class.java)!!.stars!!.toInt()
            holder.starNumber.text = stars.toString()
            if (stars > 0) {
                holder.starImage.setImageResource(R.drawable.ic_star_24)
            }
        }

    }

    override fun getItemCount(): Int {
        return scansList.size
    }

    fun setData(newData: ArrayList<Scan>) {
        this.scansList.clear()
        this.scansList.addAll(newData)
        notifyDataSetChanged()
    }

}