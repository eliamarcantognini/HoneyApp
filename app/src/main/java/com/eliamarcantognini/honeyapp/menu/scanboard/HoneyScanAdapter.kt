package com.eliamarcantognini.honeyapp.menu.scanboard

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eliamarcantognini.honeyapp.R
import com.eliamarcantognini.honeyapp.firestore.Scan

class HoneyScanAdapter (onScanListener: OnScanListener) : RecyclerView.Adapter<HoneyScanHolder>() {


    private val honeyScansList = ArrayList<Scan>()
    private val onScanListener: OnScanListener = onScanListener
//
//    constructor(onScanListener: OnScanListener){
//        this.onScanListener = onScanListener
//    }
//    private val honeyScansFullList = ArrayList<HoneyScan>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoneyScanHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.honey_list_card, parent, false)
        return HoneyScanHolder(view, onScanListener)
    }

    override fun onBindViewHolder(holder: HoneyScanHolder, position: Int) {
        Log.d("SCANLISTHOLDER", "1 ++++" +honeyScansList.toString())
        Log.d("SCANLISTHOLDER", "2 ++++" +honeyScansList[position].firm)
        holder.honey = honeyScansList[position]
        holder.description.text = honeyScansList[position].desc
        holder.firm.text = honeyScansList[position].firm
        holder.name.text = honeyScansList[position].type
        holder.image.setImageResource(R.drawable.honey)

    }

    override fun getItemCount(): Int {
        return honeyScansList.size
    }

    fun setData(newData: ArrayList<Scan>) {
        this.honeyScansList.clear()
        this.honeyScansList.addAll(newData)
        notifyDataSetChanged()
    }

}