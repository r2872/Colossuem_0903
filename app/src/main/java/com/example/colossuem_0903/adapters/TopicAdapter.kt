package com.example.colossuem_0903.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.colossuem_0903.R
import com.example.colossuem_0903.datas.TopicData

class TopicAdapter(
    private val mContext: Context,
    resId: Int,
    private val mList: List<TopicData>
) : ArrayAdapter<TopicData>(mContext, resId, mList) {

    private val mInflater = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        if (row == null) {
            row = mInflater.inflate(R.layout.topic_list_item, null)
        }
        row!!

        val data = mList[position]

        val topicImg = row.findViewById<ImageView>(R.id.topic_Img)
        val topicTitleTxt = row.findViewById<TextView>(R.id.topicTitle_Txt)

        topicTitleTxt.text = data.title

        Glide.with(mContext)
            .load(data.imgURL)
            .into(topicImg)

        return row
    }
}