package com.example.colossuem_0903

import android.os.Bundle
import com.example.colossuem_0903.datas.TopicData
import com.example.colossuem_0903.utils.ServerUtil
import org.json.JSONObject

class MainActivity : BaseActivity() {

    val mTopicList = ArrayList<TopicData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setValues()
        setupEvents()
    }

    override fun setValues() {

        getMainDataFromServer()
    }

    override fun setupEvents() {

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
                    val tempTopicData = TopicData()
                    tempTopicData.id = topicObj.getInt("id")
                    tempTopicData.title = topicObj.getString("title")
                    tempTopicData.imgURL = topicObj.getString("img_url")

//                    mTopicList 에 하나씩 추가.
                    mTopicList.add(tempTopicData)
                }
            }
        })
    }
}