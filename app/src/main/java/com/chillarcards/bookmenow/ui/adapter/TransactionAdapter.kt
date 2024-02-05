package com.chillarcards.bookmenow.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.utills.CommonDBaseModel
import com.chillarcards.bookmenow.ui.Booking
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills

class TransactionAdapter(private val items: List<Booking>,
                         private val context: Context?,
                         private val getAdapterUtil: IAdapterViewUtills)
    : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.BookingView.setOnClickListener {
            val commonDObj = CommonDBaseModel()
            commonDObj.mastIDs = item.id.toString()
            commonDObj.itmName = item.name
            commonDObj.valueStr1 = item.custname
            commonDObj.positionVal = item.status
            val sCommonDAry: ArrayList<CommonDBaseModel> = ArrayList()
            sCommonDAry.add(commonDObj)
            getAdapterUtil.getAdapterPosition(position, sCommonDAry, "VIEW")
        }

        holder.tranDateTextView.text = item.name
        holder.CustomNameTextView.text = item.custname
        if(item.status == 1) {
            holder.BookStatus.text = "Done"
            context?.let { holder.BookStatus.setTextColor(it.getColor(R.color.primary_green)) }
        } else {
            holder.BookStatus.text = "Pending"
            context?.let { holder.BookStatus.setTextColor(it.getColor(R.color.white)) }
        }

    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val BookingView: CardView = itemView.findViewById(R.id.book_frm)
        val tranDateTextView: TextView = itemView.findViewById(R.id.tran_date)
        val CustomNameTextView: TextView = itemView.findViewById(R.id.tran_cust_name)
        val BookStatus: TextView = itemView.findViewById(R.id.tran_status)

    }

}
