package com.eliamarcantognini.honeyapp.menu.scanboard

import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eliamarcantognini.honeyapp.R
import com.eliamarcantognini.honeyapp.firestore.Scan

class HoneyScanHolder(view: View, listener: OnScanListener) : RecyclerView.ViewHolder(view),
    OnClickListener {
    var name: TextView = view.findViewById(R.id.honeyListName)
    var firm: TextView = view.findViewById(R.id.honeyListFirm)
    var description: TextView = view.findViewById(R.id.honeyListDesc)
    var starNumber: TextView = view.findViewById(R.id.starNumberTxt)
    var image: ImageView = view.findViewById(R.id.honeyListImg)
    var starImage: ImageView = view.findViewById(R.id.starImg)
    var scan: Scan = Scan("Demo", "Demo", "Demo", "Demo", "Demo", "Demo", "", "", 0, "", R.drawable.honey)
    private var onScanListener: OnScanListener = listener

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        onScanListener.onScanClick(adapterPosition)
    }
}