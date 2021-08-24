package com.eliamarcantognini.honeyapp.menu.scanboard

import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eliamarcantognini.honeyapp.R
import com.eliamarcantognini.honeyapp.firestore.Scan
import com.eliamarcantognini.honeyapp.menu.scanner.Honey
import com.eliamarcantognini.honeyapp.menu.scanner.OnScanListener

class HoneyScanHolder(view: View): RecyclerView.ViewHolder(view), OnClickListener {
    var name: TextView = view.findViewById(R.id.honeyListName)
    var firm: TextView = view.findViewById(R.id.honeyListFirm)
    var description: TextView = view.findViewById(R.id.honeyListDesc)
    var image: ImageView = view.findViewById(R.id.honeyListImg)
    var honey: Scan = Scan("Demo", "Demo", "Demo", "Demo", "Demo", "Demo", "")
    private lateinit var onScanListener: OnScanListener

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        onScanListener.onScanClick(adapterPosition)
    }
}