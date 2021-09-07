package com.example.colossuem_0903.datas

import org.json.JSONObject
import java.io.Serializable

class TopicData(
    var id: Int,
    var title: String,
    var imgURL: String
) : Serializable {

    //    선택진영 목록을 담아줄 ArrayList
    val sideList = ArrayList<SideData>()

    companion object {
        //        json {} 를 넣으면 -> 파싱해서 -> TopicData 객체로 리턴해주는 함수.
        fun getTopicDataFromJson(json: JSONObject): TopicData {

//            결과로 사용 될 TopicData 객체 하나 생성.
            val topicData = TopicData()

//            json 내부에서 값을 파싱 -> TopicData 의 변수들에 대입.
            topicData.id = json.getInt("id")
            topicData.title = json.getString("title")
            topicData.imgURL = json.getString("img_url")

//            최종 결과 선정
            return topicData
        }
    }

    //    보조 생성자 추가.
    constructor() : this(0, "제목없음", "")

    //    연습. id 값만 받는 보조 생성자
    constructor(id: Int) : this(id, "제목없음", "")
}