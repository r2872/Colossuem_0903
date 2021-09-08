package com.example.colossuem_0903.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.colossuem_0903.R
import com.example.colossuem_0903.ViewReplyDetailActivity
import com.example.colossuem_0903.ViewTopicDetailActivity
import com.example.colossuem_0903.datas.ReplyData
import com.example.colossuem_0903.utils.ServerUtil
import org.json.JSONObject

class ChildReplyAdapter(
    private val mContext: Context,
    resId: Int,
    private val mList: List<ReplyData>
) : ArrayAdapter<ReplyData>(mContext, resId, mList) {

    private val mInflater = LayoutInflater.from(mContext)

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        if (row == null) {
            row = mInflater.inflate(R.layout.child_reply_list_item, null)
        }
        row!!

        val data = mList[position]

        val writerNickNameTxt = row.findViewById<TextView>(R.id.writerNickName_Txt)
        val contentTxt = row.findViewById<TextView>(R.id.content_Txt)

        writerNickNameTxt.text = "(${data.selectedSide.title}) ${data.writer.nickname}"
        contentTxt.text = data.content

        return row
    }
}