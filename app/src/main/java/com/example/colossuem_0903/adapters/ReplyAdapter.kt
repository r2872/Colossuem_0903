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

//        이 댓글은 내가 좋아요 / 싫어요 찍은 여부를 판단 할 수 있다.
//        data.mylike 등 변수를 활용

//        응용문제.
//        내가 좋아요 한 댓글 -> 버튼 테두리 빨간 사각형.
//        좋아요 안한 댓글 -> 버튼 테두리 검정 사각형.

//        싫어요 : 내가 했다면 파란 사각형, 아니면 검정 사각형.

//        응용문제 2.
//        위와 같은 상황에서 글씨 색상도, 빨간 or #DFDFDF 회색.
//        싫어요 글씨 파란색 or #DFDFDF 회색.
        if (data.myLike) {
            likeCountTxt.setBackgroundResource(R.drawable.red_border_rect)
            likeCountTxt.setTextColor(ContextCompat.getColor(mContext, R.color.like_red))
        } else {
            likeCountTxt.setBackgroundResource(R.drawable.black_border_rect)
            likeCountTxt.setTextColor(ContextCompat.getColor(mContext, R.color.black))
        }
        if (data.myDisLike) {
            disLikeCountTxt.setBackgroundResource(R.drawable.blue_border_rect)
            disLikeCountTxt.setTextColor(ContextCompat.getColor(mContext, R.color.dislike_blue))
        } else {
            disLikeCountTxt.setBackgroundResource(R.drawable.black_border_rect)
            disLikeCountTxt.setTextColor(ContextCompat.getColor(mContext, R.color.black))
        }




        likeCountTxt.tag = true
        disLikeCountTxt.tag = false

//        해당 댓글에 좋아요 / 싫어요 찍었다 : 서버에 전송.

//        API : POST - topic_reply_like
//        토큰값 / 댓글 id / true or false (좋아요 / 싫어요) 파라미터.

//        도전과제 : 두개의 텍스트뷰가 눌리면 할일이 거의 동일.
//        차이점 - true 냐 false 냐

        val ocl = object : View.OnClickListener {
            override fun onClick(view: View?) {

                val isLike = view!!.tag.toString().toBoolean()

                ServerUtil.postRequestReplyLikeOrDislike(
                    mContext,
                    data.id,
                    isLike,
                    object : ServerUtil.JsonResponseHandler {
                        override fun onResponse(jsonObj: JSONObject) {

//                            어댑터 안에서 -> ViewTopicDetailActivity (mContext 변수에 담겨있다!) 의 기능 실행.
                            (mContext as ViewTopicDetailActivity).getTopicDetailDataFromServer()
                        }
                    })
            }
        }

//        tag 속성 이용, 하나의 코드에서 두개 대응.
        likeCountTxt.setOnClickListener(ocl)
        disLikeCountTxt.setOnClickListener(ocl)

//        임시처리 : 답글 갯수도 늘리면 이벤트.
        replyCountTxt.setOnClickListener {

//            답글 목록 보는 화면으로 이동.
            val myIntent = Intent(mContext, ViewReplyDetailActivity::class.java)
            myIntent.putExtra("replyData", data)
            mContext.startActivity(myIntent)
        }

        return row
    }
}