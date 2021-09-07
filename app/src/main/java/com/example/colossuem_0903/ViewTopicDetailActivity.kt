package com.example.colossuem_0903

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.colossuem_0903.adapters.ReplyAdapter
import com.example.colossuem_0903.datas.ReplyData
import com.example.colossuem_0903.datas.TopicData
import com.example.colossuem_0903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_view_topic_detail.*
import org.json.JSONObject

class ViewTopicDetailActivity : BaseActivity() {

    lateinit var mTopicData: TopicData
    val mReplyList = ArrayList<ReplyData>()
    lateinit var mReplyAdapter: ReplyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_topic_detail)

        setupEvents()
        setValues()
    }

    override fun setValues() {

        mTopicData = intent.getSerializableExtra("topics") as TopicData

        Glide.with(mContext)
            .load(mTopicData.imgURL)
            .into(topic_Img)
        title_Txt.text = mTopicData.title

//        나머지 데이터는 서버에서 가져오자.
        getTopicDetailDataFromServer()

        mReplyAdapter = ReplyAdapter(mContext, R.layout.reply_list_item, mReplyList)
        replyListView.adapter = mReplyAdapter

    }

    override fun setupEvents() {

//        첫번째 진영, 두번째 진영 투표버튼의 이벤트.
//        두개의 버튼이 하는일이 거의 동일함. => 코드를 한번만 짜서, 두개의 버튼에 똑같이 달아보자.

//        버튼이 눌리면 할 일 (setOnClickListener) 을 적어두는 변수. (Interface 변수)
        val ocl = object : View.OnClickListener {
            override fun onClick(v: View?) {
//                버튼이 눌리면 할 일

            }
        }

        voteToFirstSide_Btn.setOnClickListener(ocl)
        voteToSecondSide_Btn.setOnClickListener(ocl)
    }

    //    투표 현황등, 최신 토론 상세 데이터를 다시 서버에서 불러오기.
    private fun getTopicDetailDataFromServer() {

        ServerUtil.getRequestTopicDetail(
            mContext,
            mTopicData.id,
            object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObj: JSONObject) {

                    mReplyList.clear()

                    val dataObj = jsonObj.getJSONObject("data")
                    val topicObj = dataObj.getJSONObject("topic")

//                    mTopicData 를 새로 파싱한 데이터로 교체.
                    mTopicData = TopicData.getTopicDataFromJson(topicObj)

//                    topicObj 안에를 보면, 댓글 목록도 같이 들어있다 => 추가 파싱, UI 반영
                    val repliesArr = topicObj.getJSONArray("replies")

                    for (i in 0 until repliesArr.length()) {

//                        댓글 {} json -> ReplyData 파싱 (변환) -> mReplyData 목록에 추가.
//                        val replyObj = repliesArr.getJSONObject(i)
//                        val replyData = ReplyData.getReplyDataFromJson(replyObj)
//                        mReplyList.add(replyData)
                        mReplyList.add(ReplyData.getReplyDataFromJson(repliesArr.getJSONObject(i)))
                    }

//                    새로 받은 데이터로 UI 반영. (득표 수 등등)
                    refreshTopicDataToUI()
                }
            })
    }

    private fun refreshTopicDataToUI() {
        runOnUiThread {
            firstSideTitle_Txt.text = mTopicData.sideList[0].title
            firstSideVoteCount_Txt.text = "${mTopicData.sideList[0].voteCount}"

            secondSideTitle_Txt.text = mTopicData.sideList[1].title
            secondSideVoteCount_Txt.text = "${mTopicData.sideList[1].voteCount}"

//            리스트뷰도 새로고침.
            mReplyAdapter.notifyDataSetChanged()

        }
    }

    private fun voteTopic(side_id: Int) {
        ServerUtil.postRequestTopicVote(
            mContext,
            side_id,
            object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObj: JSONObject) {

                    getTopicDetailDataFromServer()
                }
            }
        )

    }
}