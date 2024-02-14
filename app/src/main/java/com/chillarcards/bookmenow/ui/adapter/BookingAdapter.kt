package com.chillarcards.bookmenow.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.data.model.Appointment
import com.chillarcards.bookmenow.utills.CommonDBaseModel
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills

class BookingAdapter(private val dataList: List<Appointment>,
                     private val context: Context?,
                     private val getAdapterUtil: IAdapterViewUtills)
    : RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.booking_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
        if(item.bookingStatus == 1) {
            holder.paymentStatus.text = "Done"
            context?.let { holder.paymentStatus.setTextColor(it.getColor(R.color.primary_green)) }
        }  else if(item.bookingStatus == 2) {
            holder.paymentStatus.text = "Cancelled"
            context?.let { holder.paymentStatus.setTextColor(it.getColor(R.color.primary_red)) }
        } else {
            holder.paymentStatus.text = "Pending"
            context?.let { holder.paymentStatus.setTextColor(it.getColor(R.color.white)) }
        }

        holder.BookingView.setOnClickListener {
            val commonDObj = CommonDBaseModel()
            commonDObj.mastIDs = item.bookingId.toString()
            commonDObj.itmName = item.customerName
            commonDObj.mobile = item.customerPhone
            commonDObj.positionVal = item.bookingStatus
            val sCommonDAry: ArrayList<CommonDBaseModel> = ArrayList()
            sCommonDAry.add(commonDObj)
            getAdapterUtil.getAdapterPosition(position, sCommonDAry, "VIEW")
        }
    }

    override fun getItemCount() = dataList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val BookingView: CardView = itemView.findViewById(R.id.book_frm)
        private val CustomNameTextView: TextView = itemView.findViewById(R.id.tran_cust_name)
        private val TimeTextView: TextView = itemView.findViewById(R.id.tran_cust_date)
        val paymentStatus: TextView = itemView.findViewById(R.id.tran_sales_name)

        fun bind(item: Appointment) {
            TimeTextView.text = item.timeSlot
            CustomNameTextView.text = item.customerName

        }

    }

}
