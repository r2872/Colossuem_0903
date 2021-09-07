package com.example.colossuem_0903

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.colossuem_0903.datas.SideData
import com.example.colossuem_0903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_reply_edit.*
import org.json.JSONObject

class ReplyEditActivity : BaseActivity() {

    lateinit var mSelectedSide: SideData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply_edit)

        setValues()
        setupEvents()
    }

    override fun setValues() {

        mSelectedSide = intent.getSerializableExtra("selectedSide") as SideData
    }

    override fun setupEvents() {

        postReply_Btn.setOnClickListener {

//            1. 입력한 내용 받아오기
            val inputContent = content_Edt.text.toString()

//            2. 내용이 10글자 미만이라면, "10자 이상 입력해주세요." 강제 종료
            if (content_Edt.length() < 10) {
                Toast.makeText(mContext, "10자 이상 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//            3. 검사를 통과했다면, 서버에 게시글 등록. (AlertDialog 로 정말 등록하시겠습니까.?)
            val alert = AlertDialog.Builder(mContext)
                .setMessage("정말 등록하시겠습니까.?")
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                    ServerUtil.postRequestTopicReply(
                        mContext,
                        mSelectedSide.id,
                        inputContent,
                        object : ServerUtil.JsonResponseHandler {
                            override fun onResponse(jsonObj: JSONObject) {

                                runOnUiThread {
                                    Toast.makeText(mContext, "등록완료.", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                            }
                        })
                })
                .setNegativeButton("취소", null)
            alert.show()

        }
    }
}