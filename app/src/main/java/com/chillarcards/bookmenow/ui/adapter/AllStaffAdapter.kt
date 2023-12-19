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
import com.chillarcards.bookmenow.ui.Dummy
import com.chillarcards.bookmenow.ui.DummyStaff
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills
import com.chillarcards.bookmenow.utills.CommonDBaseModel

class AllStaffAdapter(private val items: List<DummyStaff>,
                      private val getAdapterUtil: IAdapterViewUtills,
                      private val context: Context?) : RecyclerView.Adapter<AllStaffAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_staff_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.StaffView.setOnClickListener {
            val commonDObj = CommonDBaseModel()
            commonDObj.mastIDs = item.id.toString()
            commonDObj.itmName = item.name
            val sCommonDAry: ArrayList<CommonDBaseModel> = ArrayList()
            sCommonDAry.add(commonDObj)
        }
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val StaffView: CardView = itemView.findViewById(R.id.order_frm)
        val StaffNameTextView: TextView = itemView.findViewById(R.id.staff_name)
        val StaffRate: TextView = itemView.findViewById(R.id.staff_rate)

        fun bind(item: DummyStaff) {
            StaffNameTextView.text = item.name
            StaffRate.text = context?.getResources()?.getString(R.string.currency)+" "+item.total

//            itemView.idTextView.text = "ID: ${item.id}"
//            itemView.imageView.setImageResource(item.imageResId)
        }

    }

}
