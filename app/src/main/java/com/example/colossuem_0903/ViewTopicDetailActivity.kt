package com.example.colossuem_0903

import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.colossuem_0903.datas.TopicData
import kotlinx.android.synthetic.main.activity_view_topic_detail.*

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

    }

    override fun setupEvents() {

    }
}