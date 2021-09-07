package com.example.colossuem_0903.datas

import android.util.Log
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ReplyData(
    var id: Int,
    var content: String,
    var likeCount: Int,
    var disLikeCount: Int,
    var myLike: Boolean,
    var myDisLike: Boolean,
    var replyCount: Int
) {

    //    ReplyData 의 하위 개념들
//    이 댓글이 지지하는 진영
    lateinit var selectedSide: SideData

    //    이 댓글을 적은 사람
    lateinit var writer: UserData

    //    이 댓글이 적힌 시점. (날짜+시간) -> Calendar 클래스 활용.
//    SimpleDateFormat 을 이용하면 => 다양한 양식으로 가공 가능.
    val createdAt = Calendar.getInstance() // 일단 현재시간이 저장. -> 파싱을 통해 작성 된 시간으로 변경.


    constructor() : this(0, "", 0, 0, false, false, 0)

    //    각 댓글마다의 기능 : 작성된 일시를 보고 -> ~분전, ~일전, ~시간전 등으로 가공.
//    5일 이상: yyyy년 M월 d일 로 가공.
    fun getFormattedTimeAgo(): String {

//        지금으로부터, 얼마나 이전에 작성된 글인가? 두 일시의 텀 (지금시간 - 작성된 시간) 계산.
        val now = Calendar.getInstance()
        val interval = now.timeInMillis - this.createdAt.timeInMillis

        Log.d("두 시간의 간격", interval.toString())

        if (interval < 1000) {

//            간격: 밀리초 까지 계산. (1/1000)
//            1초도 안된다. => "방금 전" 으로 결과.
            return "방금 전"
        } else if (interval < 1 * 60 * 1000) {

//            1분 이내 => "?초 전" 으로 결과.
            return "${interval / 1000}초 전"
        } else if (interval < 1 * 60 * 60 * 1000) {

//            1시간 이내 => "?분 전" 으로 결과.
            return "${interval / 1000 / 60}분 전"
        } else if (interval < 24 * 60 * 60 * 1000) {

//            24시간 이내 => "?시간 전" 으로 결과.
            return "${interval / 1000 / 60 / 60}시간 전"
        } else if (interval < 5 * 24 * 60 * 60 * 1000) {

//            5일 이내 => "?일 전" 으로 결과.
            return "${interval / 1000 / 60 / 60 / 24}일 전"
        } else {

//            5일 이상. -> "yyyy년 M월 d일" 양식 가공.
            val replyDisplayFormat = SimpleDateFormat("yyyy년 M월 d일")
            return replyDisplayFormat.format(this.createdAt.time)
        }
    }

    companion object {

        //        서버가 주는 날짜 양식을 분석하기 위한 SimpleDateFormat
        val serverFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

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

//            선택진영 파싱 -> SideData 에 만들어둔 파싱 기능 활용.
            val selectedSideObj = json.getJSONObject("selected_side")
            replyData.selectedSide = SideData.getSideDataFromJson(selectedSideObj)

//            작성자 정보 파싱 -> UserData 에 만들어둔 파싱 기능 활용.
            val userObj = json.getJSONObject("user")
            replyData.writer = UserData.getUserDataFromJson(userObj)

//            작성일시 -> String 으로 받아서 -> Calendar 로 변환해서 저장.
            val createdAtString = json.getString("created_at")

//            댓글 데이터의 작성일시에, serverFormat 변수를 이용해서 시간 저장.
            replyData.createdAt.time = serverFormat.parse(createdAtString)

            return replyData
        }
    }
}