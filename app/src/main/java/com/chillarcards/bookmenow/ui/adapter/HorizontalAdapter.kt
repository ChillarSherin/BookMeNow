package com.chillarcards.bookmenow.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.ui.Dummy
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills
import com.chillarcards.bookmenow.utills.CommonDBaseModel

class HorizontalAdapter(private val items: List<Dummy>,
                        context: Context?,
                        private val getAdapterUtil: IAdapterViewUtills
) : RecyclerView.Adapter<HorizontalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_top_staff, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.staffView.setOnClickListener {
            val commonDObj = CommonDBaseModel()
            commonDObj.mastIDs = item.id.toString()
            commonDObj.itmName = item.name
            commonDObj.valueStr1 = item.custname
            val sCommonDAry: ArrayList<CommonDBaseModel> = ArrayList()
            sCommonDAry.add(commonDObj)
            getAdapterUtil.getAdapterPosition(position, sCommonDAry, "STAFFVIEW")
        }
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val staffView: LinearLayout = itemView.findViewById(R.id.staff_frm)
        private val staffNameTextView: TextView = itemView.findViewById(R.id.staff_name)
        private val ShopNameTextView: TextView = itemView.findViewById(R.id.banner_shop)

        fun bind(item: Dummy) {
            staffNameTextView.text = item.name
            ShopNameTextView.isAllCaps = true
            ShopNameTextView.text = getFirstLetterAfterSpace(item.name)+ ""+item.id
//            itemView.idTextView.text = "ID: ${item.id}"
//            itemView.imageView.setImageResource(item.imageResId)
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
