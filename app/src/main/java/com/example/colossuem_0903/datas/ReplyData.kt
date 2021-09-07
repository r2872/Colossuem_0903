package com.example.colossuem_0903.datas

import org.json.JSONObject

class ReplyData(
    var id: Int,
    var content: String,
    var likeCount: Int,
    var disLikeCount: Int,
    var myLike: Boolean,
    var myDisLike: Boolean,
    var replyCount: Int
) {

    constructor() : this(0, "", 0, 0, false, false, 0)

    companion object {

        //        JSON 을 넣으면 -> ReplyData 로 변환해주는 기능
        fun getReplyDataFromJson(json: JSONObject): ReplyData {

            val replyData = ReplyData()

            replyData.id = json.getInt("id")
            replyData.content = json.getString("content")
            replyData.likeCount = json.getInt("like_count")
            replyData.disLikeCount = json.getInt("dislike_count")
            replyData.myLike = json.getBoolean("my_like")
            replyData.myDisLike = json.getBoolean("my_dislike")
            replyData.replyCount = json.getInt("reply_count")

            return replyData
        }
    }
}