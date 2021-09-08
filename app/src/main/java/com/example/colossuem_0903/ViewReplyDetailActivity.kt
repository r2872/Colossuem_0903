package com.example.colossuem_0903

import android.os.Bundle
import android.widget.Toast
import com.example.colossuem_0903.datas.ReplyData
import com.example.colossuem_0903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_view_reply_detail.*
import org.json.JSONObject

class ViewReplyDetailActivity : BaseActivity() {

    lateinit var mReplyData: ReplyData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reply_detail)

        setupEvents()
        setValues()
    }

    override fun setValues() {

        mReplyData = intent.getSerializableExtra("replyData") as ReplyData

        sideAndNickname_Txt.text =
            "(${mReplyData.selectedSide.title}) ${mReplyData.writer.nickname}"
        replyContent_Txt.text = mReplyData.content
    }

    override fun setupEvents() {

        replyPost_Btn.setOnClickListener {
            replyToReply()
        }
    }

    private fun replyToReply() {

        if (reply_Edt.length() < 5) {
            Toast.makeText(mContext, "5글자 이상 입력 해 주세요", Toast.LENGTH_SHORT).show()
            return
        }

        val inputContent = reply_Edt.text.toString()

        ServerUtil.postRequestChildReply(
            mContext,
            inputContent,
            mReplyData.id,
            object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObj: JSONObject) {

                    runOnUiThread {
                        reply_Edt.text.clear()
                    }
                }
            })
    }
}