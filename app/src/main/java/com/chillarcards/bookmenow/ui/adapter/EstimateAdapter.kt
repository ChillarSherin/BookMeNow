package com.chillarcards.bookmenow.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.ui.DummyStaff
import com.chillarcards.bookmenow.ui.StaffService

class EstimateAdapter(private val itemList: List<DummyStaff>) :
    RecyclerView.Adapter<EstimateAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slno: TextView = itemView.findViewById(R.id.slno)
        val serviceName: TextView = itemView.findViewById(R.id.service_name)
        val serviceRate: TextView = itemView.findViewById(R.id.service_rate)
      //  val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = itemList[position]
//                    clickedItem.isSelected = !clickedItem.isSelected
                    notifyItemChanged(position)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.slno.text = item.id.toString()
        holder.serviceName.text = item.name
        holder.serviceRate.text = item.total

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}