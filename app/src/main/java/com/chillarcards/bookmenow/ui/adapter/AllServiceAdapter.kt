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
import com.chillarcards.bookmenow.ui.DummyService
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills
import com.chillarcards.bookmenow.utills.CommonDBaseModel

class AllServiceAdapter(private val items: List<DummyService>,
                        private val getAdapterUtil: IAdapterViewUtills,
                        private val context: Context?) : RecyclerView.Adapter<AllServiceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_service_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.serviceView.setOnClickListener {
            val commonDObj = CommonDBaseModel()
            commonDObj.mastIDs = item.id.toString()
            commonDObj.itmName = item.name

            val sCommonDAry: ArrayList<CommonDBaseModel> = ArrayList()
            sCommonDAry.add(commonDObj)
            getAdapterUtil.getAdapterPosition(position, sCommonDAry, "VIEW")

        }
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceView: CardView = itemView.findViewById(R.id.order_frm)
        val serviceNameTextView: TextView = itemView.findViewById(R.id.service_name)
        val serviceRate: TextView = itemView.findViewById(R.id.service_rate)
        val serviceMin: TextView = itemView.findViewById(R.id.service_min)

        fun bind(item: DummyService) {
            serviceNameTextView.text = item.name
            serviceRate.text = context?.getResources()?.getString(R.string.currency)+" "+item.rate
            serviceMin.text = item.min+" "+context?.getResources()?.getString(R.string.min)
        }

    }

}
