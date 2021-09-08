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
import com.example.colossuem_0903.datas.NotificationData
import com.example.colossuem_0903.datas.TopicData
import java.text.SimpleDateFormat

class NotificationAdapter(
    private val mContext: Context,
    resId: Int,
    private val mList: List<NotificationData>
) : ArrayAdapter<NotificationData>(mContext, resId, mList) {

    private val mInflater = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        if (row == null) {
            row = mInflater.inflate(R.layout.notification_list_item, null)
        }
        row!!

        val data = mList[position]

        val notificationTitle = row.findViewById<TextView>(R.id.notificationTitle_Txt)
        val notificationCreateAt = row.findViewById<TextView>(R.id.notiCreateAt_Txt)

        notificationTitle.text = data.title

//        Calendar -> String 으로 가공 (SimpleDateFormat - format) 활용
        val sdf = SimpleDateFormat("yyyy년 M월 d일 a h:mm")
        notificationCreateAt.text = sdf.format(data.createdAt.time)

        return row
    }
}