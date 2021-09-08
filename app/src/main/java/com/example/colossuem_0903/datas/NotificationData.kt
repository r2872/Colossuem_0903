package com.example.colossuem_0903.datas

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class NotificationData(
    var id: Int,
    var title: String,
) {

    //    생성자와 관계 없이 동작하는 멤버변수
    val createdAt: Calendar = Calendar.getInstance() // 현재 시간이 기본값.

    constructor() : this(0, "")

    companion object {

        private val serverFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        fun getNotificationDataFromJson(json: JSONObject): NotificationData {

            val notificationData = NotificationData()

            notificationData.id = json.getInt("id")
            notificationData.title = json.getString("title")

//            1. 서버가 알려주는 시간을 String 으로 받기.
            val createdAtString = json.getString("created_at")

//            2. 받아낸 String 을 => Calendar 의 time 값으로 대입. (SimpleDateFormat - parse 필요)
            notificationData.createdAt.time = serverFormat.parse(createdAtString)

            return notificationData
        }
    }

}