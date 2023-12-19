package com.chillarcards.bookmenow.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.ui.StaffService

class SelectedAdapter(private val mContext: Context?, private val itemList: MutableList<StaffService>) :
    RecyclerView.Adapter<SelectedAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceName: TextView = itemView.findViewById(R.id.service_name)
        val serviceRate: TextView = itemView.findViewById(R.id.service_rate)

        fun bind(item: StaffService) {
            serviceName.text = item.name
          //  checkBox.isChecked = item.isSelected
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.serviceName.text = item.name
       // holder.serviceRate.text = getResources().getString(R.string.currency) +""+ item.total
        holder.serviceRate.text = (mContext?.getResources()?.getString(R.string.currency) ?: "₹") +""+ item.total
        holder.bind(itemList[position])

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}