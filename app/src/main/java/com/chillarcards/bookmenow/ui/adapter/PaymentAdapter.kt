package com.chillarcards.bookmenow.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.data.model.BookingReportItem

class PaymentAdapter(private val items: List<BookingReportItem>,
                     private val context: Context?)
    : RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.payment_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val CustomNameTextView: TextView = itemView.findViewById(R.id.tran_cust_name)
        private val PayStatus: ImageView = itemView.findViewById(R.id.report_status)
        private val orderId: TextView = itemView.findViewById(R.id.ord_id)

        fun bind(item: BookingReportItem) {
            CustomNameTextView.text = item.customerName
            orderId.text = item.orderId

            //0= vist pending 1 = vist completed  2 cancelled
//            when(item.bookingStatus){
//                2{}
//            }
            if(item.bookingStatus==2){
                PayStatus.setImageDrawable(context?.getDrawable(R.drawable.ic_down))
            }  else  if(item.bookingStatus==0){
                PayStatus.setImageDrawable(context?.getDrawable(R.drawable.ic_pending))
            } else  if(item.bookingStatus==1){
                PayStatus.setImageDrawable(context?.getDrawable(R.drawable.ic_top))
            }
        }

    }

}
