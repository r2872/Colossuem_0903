package com.example.colossuem_0903

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.colossuem_0903.adapters.TopicAdapter
import com.example.colossuem_0903.datas.TopicData
import com.example.colossuem_0903.datas.UserData
import com.example.colossuem_0903.utils.ServerUtil
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.my_custom_action_bar.*
import org.json.JSONObject

class MainActivity : BaseActivity() {

    val mTopicList = ArrayList<TopicData>()
    lateinit var mTopicAdapter: TopicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setValues()
        setupEvents()
        Log.d("푸시알림-디바이스토큰", FirebaseInstanceId.getInstance().token.toString())
    }

    override fun setValues() {

        getMainDataFromServer()

        mTopicAdapter = TopicAdapter(mContext, R.layout.topic_list_item, mTopicList)
        topicListView.adapter = mTopicAdapter

//        back_Btn 의 숨김처리. (메인만 따로 숨김)
        back_Btn.visibility = View.GONE

//        알림버튼 보여주기 (메인만 따로 보여주기)
        notification_Btn.visibility = View.VISIBLE
    }

    override fun setupEvents() {

        clickedTopicItem()
    }

    private fun clickedTopicItem() {

        topicListView.setOnItemClickListener { _, _, position, _ ->

            val clickedTopicList = mTopicList[position]

            val myIntent = Intent(mContext, ViewTopicDetailActivity::class.java)
            myIntent.putExtra("topics", clickedTopicList)
            startActivity(myIntent)
        }
    }

    //    서버에서, 메인화면에 보여줄 정보 받아오기
    private fun getMainDataFromServer() {

        ServerUtil.getRequestMainData(mContext, object : ServerUtil.JsonResponseHandler {
            override fun onResponse(jsonObj: JSONObject) {

//                응답 - jsonObj 분석 (파싱) => 토론 주제들을 서버가 내려줌.
                val dataObj = jsonObj.getJSONObject("data")
                val topicsArr = dataObj.getJSONArray("topics")

//                서버가 내려주는 토론 주제들 (JSONObject 목록) => TopicData 로 변환해서 ArrayList 에 추가. (반복문 활용)

//                topicArr 에 4개 : 0 ~ 4 직전 까지 반복.
                for (i in 0 until topicsArr.length()) {

//                    순서에 맞는 {} 를 통째로 받아내기. => 토론 주제 하나하나에 대한 정보를 담게됨.
                    val topicObj = topicsArr.getJSONObject(i)

//                    TopicData 를 만들어서 -> 멤버변수들에 -> TopicObj 에서 파싱한 데이터를 대입.
//                    val tempTopicData = TopicData()
//                    tempTopicData.id = topicObj.getInt("id")
//                    tempTopicData.title = topicObj.getString("title")
//                    tempTopicData.imgURL = topicObj.getString("img_url")

                    val tempTopicData = TopicData.getTopicDataFromJson(topicObj)

//                    mTopicList 에 하나씩 추가. => 어댑터의 목록 구성 변수에 변화.
                    mTopicList.add(tempTopicData)
                }

//                로그인 한 사용자 닉네임 가져오기
                val userObj = dataObj.getJSONObject("user")
//                val nickName = user.getString("nick_name")
                val loginUser = UserData.getUserDataFromJson(userObj)

//                목록의 변화 -> 리스트뷰가 인지. -> 새로고침 공지. -> 리스트뷰 변경 -> 백그라운드에서 UI 변경
                runOnUiThread {
                    mTopicAdapter.notifyDataSetChanged()
                    Toast.makeText(mContext, "${loginUser.nickname}님 환영합니다.", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        })
    }
}