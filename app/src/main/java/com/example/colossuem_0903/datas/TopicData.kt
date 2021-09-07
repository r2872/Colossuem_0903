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

//    내가 투표한 진영의 id 가 뭔지?
    var mySideId = 0 // Int 가 들어올 예정

    companion object {
        //        json {} 를 넣으면 -> 파싱해서 -> TopicData 객체로 리턴해주는 함수.
        fun getTopicDataFromJson(json: JSONObject): TopicData {

//            결과로 사용 될 TopicData 객체 하나 생성.
            val topicData = TopicData()

//            json 내부에서 값을 파싱 -> TopicData 의 변수들에 대입.
            topicData.id = json.getInt("id")
            topicData.title = json.getString("title")
            topicData.imgURL = json.getString("img_url")
//            내가 선택한 진영의 id?
            topicData.mySideId = json.getInt("my_side_id")

//            토론의 하위정보로 => sides 라는 JSONArray 를 내려줌.
//            JSONArray : for 문 돌려서 파싱 -> topicData 의 sideList 에 추가해주기.
            val sidesArr = json.getJSONArray("sides")

            for (i in 0 until sidesArr.length()) {

                val sideObj = sidesArr.getJSONObject(i)

//                jsonObject -> SideData() 로 변환.
                val sideData = SideData.getSideDataFromJson(sideObj)

//                topicData 의 sideList 에 추가하기.
                topicData.sideList.add(sideData)
            }

//            최종 결과 선정
            return topicData
        }
    }

    //    보조 생성자 추가.
    constructor() : this(0, "제목없음", "")

    //    연습. id 값만 받는 보조 생성자
    constructor(id: Int) : this(id, "제목없음", "")
}