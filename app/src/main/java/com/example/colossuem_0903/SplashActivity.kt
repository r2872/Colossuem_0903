package com.example.colossuem_0903

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.colossuem_0903.datas.UserData
import com.example.colossuem_0903.utils.ContextUtil
import com.example.colossuem_0903.utils.GlobalData
import com.example.colossuem_0903.utils.ServerUtil
import org.json.JSONObject

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setValues()
        setupEvents()
    }

    override fun setValues() {

        val myHandler = Handler(Looper.getMainLooper())

        myHandler.postDelayed({

//        1. 자동 로그인 여부 판단 -> 상황에 따라 다른 화면으로 넘어가게.
//        다른 화면: Intent 의 목적지만 달라진다.
            val myIntent: Intent

//        자동 로그인 여부: 사용자가 자동로그인 하겠다 + 저장 된 토큰이 유효(들어있다)하다.
            if (ContextUtil.getAutoLogIn(mContext) && ContextUtil.getToken(mContext) != "") {

//            둘다 만족: 자동 로그인 O -> 메인화면으로 이동.
                myIntent = Intent(mContext, MainActivity::class.java)

//                내가 누구인지 정보를 받아오자. => API 활용용
//               어느 화면에서든 접근 할 수 있게 세팅해주자.
                ServerUtil.getRequestUserInfo(mContext, object : ServerUtil.JsonResponseHandler {
                    override fun onResponse(jsonObj: JSONObject) {

                        val dataObj = jsonObj.getJSONObject("data")
                        val userObj = dataObj.getJSONObject("user")
                        val loginUserData = UserData.getUserDataFromJson(userObj)

//                        서버가 알려준 로그인 한 사람 데이터를 => 모든 화면과 공유. (GlobalData 클래스 활용)
                        GlobalData.loginUser = loginUserData

                        Log.d("사용자 정보", "${GlobalData.loginUser?.nickname}")
                    }
                })

            } else {
//            하나라도 만족 안됨: 자동 로그인 실패 -> 로그인 화면으로 이동
                myIntent = Intent(mContext, SignInActivity::class.java)

//                내가 누구인지 받아오지 않겠다. (코드 작성 X)
            }
            startActivity(myIntent)
            finish()
        }, 1000)

    }

    override fun setupEvents() {


    }
}