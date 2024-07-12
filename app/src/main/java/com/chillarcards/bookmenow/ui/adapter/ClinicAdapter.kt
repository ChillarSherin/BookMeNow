package com.chillarcards.bookmenow.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.data.model.EntityDetail
import com.chillarcards.bookmenow.ui.interfaces.IAdapterViewUtills
import com.chillarcards.bookmenow.utills.CommonDBaseModel
import com.chillarcards.bookmenow.utills.PrefManager

class ClinicAdapter(private val items: List<EntityDetail>,
                    private val context: Context,
                    private val getAdapterUtil: IAdapterViewUtills
) : RecyclerView.Adapter<ClinicAdapter.ViewHolder>() {
   // private var filteredItems: List<EntityDetail> = items.filter { it.entityStatus != 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_top_staff, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = filteredItems[position]
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val staffView: LinearLayout = itemView.findViewById(R.id.staff_frm)
        private val staffNameTextView: TextView = itemView.findViewById(R.id.staff_name)
        private val ShopNameTextView: TextView = itemView.findViewById(R.id.banner_shop)

        fun bind(item: EntityDetail) {
            if (context != null) {
                val prefManager = PrefManager(context)
                val entityId = prefManager.getEntityId().trim()
                val itemEntityId = item.entityId.toString().trim()

                Log.d("Debug", "PrefManager Entity ID: $entityId")
                Log.d("Debug", "Item Entity ID: $itemEntityId")

                //SELECTED
                if (entityId == itemEntityId) {
                    ShopNameTextView.background = context.getDrawable(R.drawable.round_circle_colour)
                    Log.d("Debug", "Set background to round_circle")
                } else {
                    ShopNameTextView.background = context.getDrawable(R.drawable.round_circle)
                    Log.d("Debug", "Set background to round_circle_colour")
                }
            } else {
                Log.e("Error", "Context is null")
            }

            staffNameTextView.text = item.entityName
            ShopNameTextView.isAllCaps = true
            if (item.entityId==-1){
                ShopNameTextView.text = "All"
            }else{
                ShopNameTextView.text = getFirstLetterAfterSpace(item.entityName)
            }
            staffView.setOnClickListener {
                val commonDObj = CommonDBaseModel()
                commonDObj.mastIDs = item.entityId.toString()
                commonDObj.itmName = item.entityName
                commonDObj.valueStr1 = item.entityType.toString()
                val sCommonDAry: ArrayList<CommonDBaseModel> = ArrayList()
                sCommonDAry.add(commonDObj)
                getAdapterUtil.getAdapterPosition(position, sCommonDAry, "VIEWBOOKING")
            }
            ShopNameTextView.setOnClickListener {
                val commonDObj = CommonDBaseModel()
                commonDObj.mastIDs = item.entityId.toString()
                commonDObj.itmName = item.entityName
                commonDObj.valueStr1 = item.entityType.toString()
                val sCommonDAry: ArrayList<CommonDBaseModel> = ArrayList()
                sCommonDAry.add(commonDObj)
                getAdapterUtil.getAdapterPosition(position, sCommonDAry, "VIEWBOOKING")
            }
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
