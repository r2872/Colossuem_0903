package com.example.colossuem_0903.datas

import org.json.JSONObject

class UserData(
    var id: Int,
    var email: String,
    var nickname: String
) {

    constructor() : this(0, "", "")

    companion object {

        //        json 넣으면 -> UserData 형태로 돌려주는 함수
        fun getUserDataFromJson(json: JSONObject): UserData {

            val userData = UserData()

            userData.id = json.getInt("id")
            userData.email = json.getString("email")
            userData.nickname = json.getString("nick_name")

            return userData
        }

//        ReplyData 의 writer 에 활용
    }
}