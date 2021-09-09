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
        val likeCountTxt = row.findViewById<TextView>(R.id.likeCount_Txt)
        val dislikeCountTxt = row.findViewById<TextView>(R.id.disLikeCount_Txt)

        writerNickNameTxt.text = "(${data.selectedSide.title}) ${data.writer.nickname}"
        contentTxt.text = data.content
        likeCountTxt.text = "좋아요 ${data.likeCount}개"
        dislikeCountTxt.text = "싫어요 ${data.disLikeCount}개"

        if (data.myLike) {
            likeCountTxt.setBackgroundResource(R.drawable.red_border_rect)
            likeCountTxt.setTextColor(ContextCompat.getColor(mContext, R.color.like_red))
        } else {
            likeCountTxt.setBackgroundResource(R.drawable.black_border_rect)
            likeCountTxt.setTextColor(ContextCompat.getColor(mContext, R.color.gray))
        }
        if (data.myDisLike) {
            dislikeCountTxt.setBackgroundResource(R.drawable.blue_border_rect)
            dislikeCountTxt.setTextColor(ContextCompat.getColor(mContext, R.color.dislike_blue))
        } else {
            dislikeCountTxt.setBackgroundResource(R.drawable.black_border_rect)
            dislikeCountTxt.setTextColor(ContextCompat.getColor(mContext, R.color.gray))
        }

        likeCountTxt.tag = true
        dislikeCountTxt.tag = false

        val ocl = View.OnClickListener { view ->
            val isLike = view!!.tag.toString().toBoolean()

            ServerUtil.postRequestReplyLikeOrDislike(
                mContext,
                data.id,
                isLike,
                object : ServerUtil.JsonResponseHandler {
                    override fun onResponse(jsonObj: JSONObject) {

                        (mContext as ViewReplyDetailActivity).getMainDataFromServer()
                    }
                })
        }

        likeCountTxt.setOnClickListener(ocl)
        dislikeCountTxt.setOnClickListener(ocl)


        return row
    }
}