package com.example.colossuem_0903

import android.os.Bundle
import com.example.colossuem_0903.datas.ReplyData
import kotlinx.android.synthetic.main.activity_view_reply_detail.*

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

    }
}