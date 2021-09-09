package com.example.colossuem_0903

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.colossuem_0903.adapters.ReplyAdapter
import com.example.colossuem_0903.datas.ReplyData
import com.example.colossuem_0903.datas.TopicData
import com.example.colossuem_0903.utils.GlobalData
import com.example.colossuem_0903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_view_topic_detail.*
import kotlinx.android.synthetic.main.my_custom_action_bar.*
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

//    화면에 들어올때마다 (onResume), 토론 현황 (+댓글) 새로 불러오기.

    override fun onResume() {
        super.onResume()

        getTopicDetailDataFromServer()
    }


    override fun setValues() {

        mTopicData = intent.getSerializableExtra("topics") as TopicData

//        투표 버튼에, 각 진영이 어떤 진영인지 버튼에 메모 해 두면 -> 투표할때, 그 진영이 뭔지 알아낼 수 있다.
        voteToFirstSide_Btn.tag = mTopicData.sideList[0].id
        voteToSecondSide_Btn.tag = mTopicData.sideList[1].id

        Glide.with(mContext)
            .load(mTopicData.imgURL)
            .into(topic_Img)
        title_Txt.text = mTopicData.title

//        나머지 데이터는 서버에서 가져오자. => onResume 에서 가겨오는것으로 일원화.
//        getTopicDetailDataFromServer()

        mReplyAdapter = ReplyAdapter(mContext, R.layout.reply_list_item, mReplyList)
        replyListView.adapter = mReplyAdapter

    }

    override fun setupEvents() {

        refreshLayout.setOnRefreshListener {
            getTopicDetailDataFromServer()

            refreshLayout.isRefreshing = false
        }

        addReply_Btn.setOnClickListener {

//            투표를 해야만 댓글작성 화면으로 이동시키자
//            선택한 진영이 없다면, myIntent 관련 코드 실행 X => Validation (입력값 검증) 작업.
            if (mTopicData.mySelectedSide == null) {
                Toast.makeText(mContext, "투표를 진행해야, 댓글 등록이 가능합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 결과 지정: 함수를 강제 종료.
            }
            val myIntent = Intent(mContext, ReplyEditActivity::class.java)
            myIntent.putExtra("selectedSide", mTopicData.mySelectedSide)
            startActivity(myIntent)
        }

//        첫번째 진영, 두번째 진영 투표버튼의 이벤트.
//        두개의 버튼이 하는일이 거의 동일함. => 코드를 한번만 짜서, 두개의 버튼에 똑같이 달아보자.

//        버튼이 눌리면 할 일 (setOnClickListener) 을 적어두는 변수. (Interface 변수)
        val ocl = object : View.OnClickListener {
            override fun onClick(view: View?) {
//                버튼이 눌리면 할 일
//                view -> 눌린게 어떤 버튼인지? 눌린 버튼을 담아준다.
                val clickedSideId = view!!.tag.toString().toInt()

//                해당 진영에 투표하기 (서버에 투표 실행)
                ServerUtil.postRequestTopicVote(
                    mContext,
                    clickedSideId,
                    object : ServerUtil.JsonResponseHandler {
                        override fun onResponse(jsonObj: JSONObject) {

//                        투표 결과 확인 => 새로 투표 현황을 다시 받아오자.
//                            이전에 함수로 분리해둔, 서버에서 상세정보 받아오기 호출.
                            getTopicDetailDataFromServer()

                        }
                    })
            }
        }

        voteToFirstSide_Btn.setOnClickListener(ocl)
        voteToSecondSide_Btn.setOnClickListener(ocl)


//         리스트뷰 스크롤 시 이벤트 작성중
        replyListView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {


            }

            override fun onScroll(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {

            }
        })

        replyListView.setOnItemLongClickListener { parent, view, position, id ->

//            1. 길게 눌린 댓글이 어떤건지 추출.
            val clickedReply = mReplyList[position]

//            2. 그 댓글의 작성자가 내가 맞는지 확인.
//            => 다른 사람이라면, 토스트 띄워주기
            if (GlobalData.loginUser?.id != clickedReply.writer.id) {
                Toast.makeText(mContext, "자신이 작성한 의견만 삭제 가능합니다.", Toast.LENGTH_SHORT).show()
                return@setOnItemLongClickListener true
            }

//            3. 내가 작성자가 맞다면, 경고창 띄우기.
            val alert = AlertDialog.Builder(mContext)
                .setMessage("정말 삭제하시겠습니까?")
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

//            4. 확인 누르면 -> 댓글 삭제 API 호출.
                    ServerUtil.deleteRequestReply(
                        mContext,
                        clickedReply.id,
                        object : ServerUtil.JsonResponseHandler {
                            override fun onResponse(jsonObj: JSONObject) {

                                runOnUiThread {
                                    Toast.makeText(mContext, "삭제했습니다.", Toast.LENGTH_SHORT).show()
                                }
//             => 응답 돌아오면 바로 새로고침.
                                getTopicDetailDataFromServer()
                            }
                        })
                })
                .setNegativeButton("취소", null)
            alert.show()

            return@setOnItemLongClickListener true
        }
    }

    //    투표 현황등, 최신 토론 상세 데이터를 다시 서버에서 불러오기.
    fun getTopicDetailDataFromServer() {

        ServerUtil.getRequestTopicDetail(
            mContext,
            mTopicData.id,
            object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObj: JSONObject) {

                    val dataObj = jsonObj.getJSONObject("data")
                    val topicObj = dataObj.getJSONObject("topic")

//                    mTopicData 를 새로 파싱한 데이터로 교체.
                    mTopicData = TopicData.getTopicDataFromJson(topicObj)

//                    계속 댓글을 다시 불러옴. -> 기존의 댓글은 지워주고 (중복 막기) 다시 추가.
                    mReplyList.clear()

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

    fun refreshTopicDataToUI() {
        runOnUiThread {
            firstSideTitle_Txt.text = mTopicData.sideList[0].title
            firstSideVoteCount_Txt.text = "${mTopicData.sideList[0].voteCount}표"

            secondSideTitle_Txt.text = mTopicData.sideList[1].title
            secondSideVoteCount_Txt.text = "${mTopicData.sideList[1].voteCount}표"

//            투표 여부에 따라 버튼들에 다른 문구 적용.
            if (mTopicData.mySideId == -1) {
                voteToFirstSide_Btn.text = "투표하기"
                voteToSecondSide_Btn.text = "투표하기"
            } else {

//                내 투표 진영 id 가 첫째 진영의 id 와 같은지?
                if (mTopicData.mySideId == mTopicData.sideList[0].id) {
                    voteToFirstSide_Btn.text = "취소하기"
                    voteToSecondSide_Btn.text = "투표하기"
                } else {
                    voteToFirstSide_Btn.text = "투표하기"
                    voteToSecondSide_Btn.text = "취소하기"
                }
            }

//            리스트뷰도 새로고침.
            mReplyAdapter.notifyDataSetChanged()

        }
    }

}