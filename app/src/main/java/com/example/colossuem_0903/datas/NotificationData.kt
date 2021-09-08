package com.example.colossuem_0903.datas

import org.json.JSONObject

class NotificationData(
    var id: Int,
    var title: String
) {

    constructor() : this(0, "")

    companion object {
        fun getNotificationDataFromJson(json: JSONObject): NotificationData {

            val notificationData = NotificationData()

            notificationData.id = json.getInt("id")
            notificationData.title = json.getString("title")

            return notificationData
        }
    }

}