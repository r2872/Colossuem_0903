package com.example.colossuem_0903.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.colossuem_0903.R
import com.example.colossuem_0903.datas.ReplyData
import com.example.colossuem_0903.utils.ServerUtil
import org.json.JSONObject
import java.text.SimpleDateFormat

class ReplyAdapter(
    private val mContext: Context,
    resId: Int,
    private val mList: List<ReplyData>
) : ArrayAdapter<ReplyData>(mContext, resId, mList) {

    private val mInflater = LayoutInflater.from(mContext)

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        if (row == null) {
            row = mInflater.inflate(R.layout.reply_list_item, null)
        }
        row!!

        val data = mList[position]

        val selectedSideTxt = row.findViewById<TextView>(R.id.selectedSide_Txt)
        val writerNickNameTxt = row.findViewById<TextView>(R.id.writerNickName_Txt)
        val createdAtTxt = row.findViewById<TextView>(R.id.createdAt_txt)
        val contentTxt = row.findViewById<TextView>(R.id.content_Txt)
        val replyCountTxt = row.findViewById<TextView>(R.id.replyCount_Txt)
        val likeCountTxt = row.findViewById<TextView>(R.id.likeCount_Txt)
        val disLikeCountTxt = row.findViewById<TextView>(R.id.disLikeCount_Txt)

        contentTxt.text = data.content
        replyCountTxt.text = "답글 ${data.replyCount}개"
        likeCountTxt.text = "좋아요 ${data.likeCount}개"
        disLikeCountTxt.text = "싫어요 ${data.disLikeCount}개"

        selectedSideTxt.text = "(${data.selectedSide.title})"

        writerNickNameTxt.text = data.writer.nickname

        createdAtTxt.text = data.getFormattedTimeAgo()

        return row
    }
}