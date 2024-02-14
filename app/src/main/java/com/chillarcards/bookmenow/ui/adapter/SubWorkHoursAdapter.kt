package com.chillarcards.bookmenow.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.data.model.DaySchedule
import com.chillarcards.bookmenow.data.model.WorkSchedule
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SubWorkHoursAdapter(
    private val context: Context,
    private var dataList: List<WorkSchedule>
//    private var dataList: WorkSchedule
    ) :
    RecyclerView.Adapter<SubWorkHoursAdapter.MyView>() {

    class MyView(view: View) : RecyclerView.ViewHolder(view) {
        var serviceName: TextView = view.findViewById(R.id.service_name)
        var serviceFrom: TextView = view.findViewById(R.id.service_from)
        var serviceTo: TextView = view.findViewById(R.id.service_to)
        var serviceTime: ImageView = view.findViewById(R.id.service_shift)
        var switchButton: SwitchCompat = view.findViewById(R.id.service_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.sub_service_adapter,
                parent,
                false
            )
        return MyView(itemView)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        val currentSchedule  = dataList[position]

        holder.serviceTime.visibility=View.VISIBLE
        holder.serviceFrom.text = currentSchedule.startTime
        holder.serviceTo.text = currentSchedule.endTime
        if(currentSchedule.session.equals("Evening")){
            holder.serviceTime.setImageDrawable(context.getDrawable(R.drawable.ic_pm))
        }else if(currentSchedule.session.equals("Morning")){
            holder.serviceTime.setImageDrawable(context.getDrawable(R.drawable.ic_am))
        }else if(currentSchedule.session.equals("Afternoon")){
            holder.serviceTime.setImageDrawable(context.getDrawable(R.drawable.ic_pm))
        }
        holder.switchButton.isChecked = (currentSchedule.status == 1)

        if (currentSchedule.status == 0) {
            holder.switchButton.setTextColor(context.resources.getColor(R.color.onoff))
        }else {
            holder.switchButton.setTextColor(context.resources.getColor(R.color.theme_end))
        }

        holder.switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                holder.switchButton.setTextColor(context.resources.getColor(R.color.black))
            } else {
                holder.switchButton.setTextColor(context.resources.getColor(R.color.onoff))
            }
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}