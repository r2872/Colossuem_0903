package com.example.colossuem_0903.datas

import org.json.JSONObject

class SideData(
    var id: Int,
    var topicId: Int,
    var title: String,
    var voteCount: Int
) {
    constructor() : this(0, 0, "", 0)

    companion object {

        //        json 을 넣으면 -> sideData 로 변환해주는 기능.
        fun getSideDataFromJson(json: JSONObject): SideData {

            val sideData = SideData()

            sideData.id = json.getInt("id")
            sideData.topicId = json.getInt("topic_id")
            sideData.title = json.getString("title")
            sideData.voteCount = json.getInt("vote_count")

            return sideData
        }
    }
}