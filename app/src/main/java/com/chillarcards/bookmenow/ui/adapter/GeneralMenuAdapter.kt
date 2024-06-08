package com.chillarcards.bookmenow.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.data.model.Category
import com.chillarcards.bookmenow.model.*
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills
import com.chillarcards.bookmenow.utills.CommonDBaseModel


class GeneralMenuAdapter (private val items: List<Category>,
                          context: Context?,
                          private val getAdapterUtil: IAdapterViewUtills
)
    : RecyclerView.Adapter<GeneralMenuAdapter.ViewHolder>() {
    private val colors = arrayOf("#FF738F", "#FFFFFFFF", "#FFFFFFFF", "#FED06B")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_gnrl_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.menuFrm.setCardBackgroundColor(Color.parseColor(colors[position % colors.size]))
        holder.bind(item)

    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val menuFrm: CardView = itemView.findViewById(R.id.menu_frm)
        private val itemName: TextView = itemView.findViewById(R.id.menu_title)
//        private val itemTitle: TextView = itemView.findViewById(R.id.item_title)

        fun bind(item: Category) {
            itemName.text = item.catName
            menuFrm.setOnClickListener {
                val commonDObj = CommonDBaseModel()
                commonDObj.mastIDs = item.catId.toString()
                commonDObj.itmName = item.catKey
                val sCommonDAry: ArrayList<CommonDBaseModel> = ArrayList()
                sCommonDAry.add(commonDObj)
                getAdapterUtil.getAdapterPosition(position, sCommonDAry, "MENU")
            }

            when (item.catKey) {
                "doc" -> itemName.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_doc, 0, 0)
                "clinic" -> itemName.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_hospital, 0, 0)
                "saloon" -> itemName.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_saloon, 0, 0)
                "lab" -> itemName.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_doc, 0, 0)
                else -> {
                    itemName.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_doc, 0, 0)
                itemName.text="New"
                }
            }
        }
    }


}
