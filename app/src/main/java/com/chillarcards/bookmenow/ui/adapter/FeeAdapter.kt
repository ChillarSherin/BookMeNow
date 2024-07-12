package com.chillarcards.bookmenow.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.data.model.AdditionalDetail
import com.chillarcards.bookmenow.data.model.EntityDetail
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills
import com.chillarcards.bookmenow.utills.CommonDBaseModel
import com.chillarcards.bookmenow.utills.PrefManager

class FeeAdapter(private val items: List<AdditionalDetail>,
                 val context: Context,
) : RecyclerView.Adapter<FeeAdapter.ViewHolder>() {
    private var filteredItems: List<AdditionalDetail> = items.filter { it.entityStatus != 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_fee, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = items[position]
        val item = filteredItems[position]
        holder.bind(item)
    }

    override fun getItemCount() = filteredItems.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val serviceEntity: TextView = itemView.findViewById(R.id.clinic_title)
        private val serviceName: TextView = itemView.findViewById(R.id.service_name)
        private val serviceFee: TextView = itemView.findViewById(R.id.service_fee)

        fun bind(item: AdditionalDetail) {
//            serviceName.text = "Consultation Fee ₹ "+item.consultationCharge.toString()
//            serviceFee.text = "Consultation Duration :"+item.consultationTime.toString()
            serviceEntity.visibility=View.GONE
            //serviceEntity.text = item.entityName
            serviceName.text = item.consultationCharge.toString()
            serviceFee.text = item.consultationTime.toString()
        }
    }

    fun getFirstLetterAfterSpace(inputText: String): String {
        val words = inputText.split(" ")
        val result = StringBuilder()

        for (word in words) {
            if (word.isNotEmpty()) {
                val firstChar = word[0]
                result.append(firstChar)
            }
        }

        return result.toString()
    }

}
