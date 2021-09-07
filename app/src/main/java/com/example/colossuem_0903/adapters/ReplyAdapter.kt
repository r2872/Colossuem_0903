package com.example.colossuem_0903.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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

        likeCountTxt.tag = true
        disLikeCountTxt.tag = false

        val ocl = object : View.OnClickListener {
            override fun onClick(view: View?) {

                val clickedLike = view!!.tag.toString().toBoolean()

                ServerUtil.postRequestReplyLike(
                    mContext,
                    data.id,
                    clickedLike,
                    object : ServerUtil.JsonResponseHandler {
                        override fun onResponse(jsonObj: JSONObject) {


                        }
                    })
            }
        }

        likeCountTxt.setOnClickListener(ocl)
        disLikeCountTxt.setOnClickListener(ocl)
//        해당 댓글에 좋아요 / 싫어요 찍었다 : 서버에 전송.


//        API : POST - topic_reply_like
//        토큰값 / 댓글 id / true or false (좋아요 / 싫어요) 파라미터.

//        도전과제 : 두개의 텍스트뷰가 눌리면 할일이 거의 동일.
//        차이점 - true 냐 false 냐

//        tag 속성 이용, 하나의 코드에서 두개 대응.

        return row
    }
}