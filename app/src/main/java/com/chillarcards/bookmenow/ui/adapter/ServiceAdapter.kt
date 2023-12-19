package com.chillarcards.bookmenow.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.ui.StaffService

class ServiceAdapter(private val itemList: List<StaffService>,
    private val onItemClickListener: (StaffService) -> Unit
) :
    RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceName: TextView = itemView.findViewById(R.id.service_name)
        val serviceRate: TextView = itemView.findViewById(R.id.service_rate)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = itemList[position]
                    clickedItem.isSelected = !clickedItem.isSelected
                    notifyItemChanged(position)
                    onItemClickListener.invoke(clickedItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.griditem_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.serviceName.text = currentItem.name.trim()
        holder.serviceRate.text = currentItem.total.trim()

        if (currentItem.isSelected) {
            holder.itemView.setBackgroundResource(R.drawable.rectangle_selected)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.rectangle_border)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}