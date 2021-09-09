package com.example.colossuem_0903

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.colossuem_0903.adapters.ChildReplyAdapter
import com.example.colossuem_0903.datas.ReplyData
import com.example.colossuem_0903.utils.GlobalData
import com.example.colossuem_0903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_view_reply_detail.*
import org.json.JSONObject

class ViewReplyDetailActivity : BaseActivity() {

    lateinit var mReplyData: ReplyData
    private lateinit var mChildReplyAdapter: ChildReplyAdapter
    val mReplyList = ArrayList<ReplyData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reply_detail)

        setupEvents()
        setValues()
    }

    @SuppressLint("SetTextI18n")
    override fun setValues() {

        mReplyData = intent.getSerializableExtra("replyData") as ReplyData

        getMainDataFromServer()

        sideAndNickname_Txt.text =
            "(${mReplyData.selectedSide.title}) ${mReplyData.writer.nickname}"
        replyContent_Txt.text = mReplyData.content

        mChildReplyAdapter = ChildReplyAdapter(mContext, R.layout.child_reply_list_item, mReplyList)
        childReplyListView.adapter = mChildReplyAdapter
    }

    override fun setupEvents() {

        refreshLayout.setOnRefreshListener {

            getMainDataFromServer()

            refreshLayout.isRefreshing = false
        }

//        답글 삭제 이벤트 -> 리스트뷰의 이벤트 처리 (LongClick)
        childReplyListView.setOnItemLongClickListener { parent, view, position, id ->

//            vaildation => 내가 적은 답글이 아니라면, 함수 강제 종료.
//            길게 누른 답글의 작성자가 본인 인가?
//            답글.작성자.id(Int) == 로그인한사람.id(Int)

            Log.d("댓글상세-로그인한사람?", GlobalData.loginUser!!.nickname)

            val clickedReply = mReplyList[position]

            if (GlobalData.loginUser?.id != clickedReply.writer.id) {
                Toast.makeText(mContext, "자신이 적은 댓글만 삭제 가능합니다.", Toast.LENGTH_SHORT).show()
                return@setOnItemLongClickListener true
            }

//            경고창 -> 정말 해당 답글을 삭제하시겠습니까?
            val alert = AlertDialog.Builder(mContext)
                .setMessage("정말 해당 답글을 삭제하시겠습니까?")
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

//                    해당 답글 삭제 -> API 요청 + 새로고침
                    ServerUtil.deleteRequestReply(
                        mContext,
                        clickedReply.id,
                        object : ServerUtil.JsonResponseHandler {
                            override fun onResponse(jsonObj: JSONObject) {

                                runOnUiThread {
                                    Toast.makeText(mContext, "댓글을 삭제했습니다.", Toast.LENGTH_SHORT)
                                        .show()
                                }
                                getMainDataFromServer()
                            }
                        })

                })
                .setNegativeButton("취소", null)
                .show()


//            true -> 롱클릭 전용 이벤트, false -> 떼면 일반 클릭 이벤트도 실행
            return@setOnItemLongClickListener true
        }

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

//                        리스트뷰의 최 하단 (마지막 아이템) 으로 이동.
                        childReplyListView.smoothScrollToPosition(mReplyList.lastIndex)
                    }
                    getMainDataFromServer()
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

    fun getMainDataFromServer() {
        ServerUtil.getRequestChildReply(
            mContext,
            mReplyData.id,
            object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObj: JSONObject) {

                    mReplyList.clear()

                    val dataObj = jsonObj.getJSONObject("data")
                    val replyObj = dataObj.getJSONObject("reply")
                    val repliesArr = replyObj.getJSONArray("replies")

                    for (i in 0 until repliesArr.length()) {

                        val replyObj = repliesArr.getJSONObject(i)

                        val tempReplyData = ReplyData.getReplyDataFromJson(replyObj)

                        mReplyList.add(tempReplyData)
                    }
                    runOnUiThread {
                        mChildReplyAdapter.notifyDataSetChanged()

                    }
                }
            })
    }

}