package com.chillarcards.bookmenow.ui.interfaces

import com.chillarcards.bookmenow.utills.CommonDBaseModel

interface IAdapterViewUtills {

    fun getAdapterPosition(Position: Int, ValueArray: ArrayList<CommonDBaseModel>, Mode: String?)
}