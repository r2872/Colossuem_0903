package com.example.colossuem_0903

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
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

    @SuppressLint("SetTextI18n")
    override fun setValues() {

        getMainDataFromServer()

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

//                        도전 코드 (구글링) : 키보드 숨김처리
                        closeKeyboard()
                        Toast.makeText(mContext, "등록완료.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    fun closeKeyboard() {
        val view = this.currentFocus

        if (view != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun getMainDataFromServer() {
        ServerUtil.getRequestChildReply(mContext, mReplyData.id, object: ServerUtil.JsonResponseHandler {
            override fun onResponse(jsonObj: JSONObject) {

            }
        })
    }

}