package com.example.colossuem_0903.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.colossuem_0903.R
import com.example.colossuem_0903.datas.ReplyData

class ReplyAdapter(
    private val mContext: Context,
    resId: Int,
    private val mList: List<ReplyData>
) : ArrayAdapter<ReplyData>(mContext, resId, mList) {

    private val mInflater = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        if (row == null) {
            row = mInflater.inflate(R.layout.reply_list_item, null)
        }
        row!!

        val data = mList[position]



        return row
    }
}