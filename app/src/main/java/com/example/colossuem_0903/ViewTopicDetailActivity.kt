package com.example.colossuem_0903

import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.colossuem_0903.datas.TopicData
import com.example.colossuem_0903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_view_topic_detail.*
import org.json.JSONObject

class ViewTopicDetailActivity : BaseActivity() {

    lateinit var mTopicData: TopicData

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

    }

    override fun setupEvents() {

    }

    //    투표 현황등, 최신 토론 상세 데이터를 다시 서버에서 불러오기.
    private fun getTopicDetailDataFromServer() {

        ServerUtil.getRequestTopicDetail(
            mContext,
            mTopicData.id,
            object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObj: JSONObject) {

                    val dataObj = jsonObj.getJSONObject("data")
                    val topicObj = dataObj.getJSONObject("topic")
                }
            })
    }
}