package com.example.colossuem_0903

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.colossuem_0903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setValues()
        setupEvents()
    }

    override fun setupEvents() {

        signIn_Btn.setOnClickListener {

            val inputId = email_Edt.text.toString()
            val inputPw = password_Edt.text.toString()

//            서버에 이 데이터가 회원이 맞는지? 확인 요청. => 로그인 시도.
//            서버 로그인 시도 => 서버에 다녀오면 어떡할건지? 대응 가이드북 변수 첨부. (인터페이스 객체)
            ServerUtil.postRequestSignIn(inputId, inputPw, object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObj: JSONObject) {

//                    서버가 보내준 jsonObj 를 가지고 처리하는 코드 작성 영역.
//                    Log.d("화면에서 받은 JSON", jsonObj.toString())
//                    "code" 이름표가 붙은 Int 값 추출,
                    val code = jsonObj.getInt("code")

//                    원하는 의도대로 잘 동작 (ex. 로그인 성공) => code: 200
//                    어떤 이유든 에러가 있다: code: 200 이 아닌 값.
                    if (code == 200) {
//                        정상 작동 경우: 로그인 성공.

//                        그 뒤의 행동? 시나리오대로 작성
//                        임시 시나리오: 로그인한 사람의 닉네임을 토스트로.
//                        "~~님, 환영합니다!"
                        Toast.makeText(mContext, "하이", Toast.LENGTH_SHORT).show()
                    } else {
//                        코드가 200이 아니다 -> 무조건 실패로 간주.

//                        1. 우선 토스트를 "로그인 실패" 로 띄워보자.
//                        백그라운드에서 서버통신 중 -> UI 에 토스트 띄운다. -> 다른 쓰레드가 UI 조작. (위험요소)

//                        2. 서버가 알려주는 로그인 실패사유도 파싱. 토스트의 내용으로 띄워주자.
                        val message = jsonObj.getString("message")

                        runOnUiThread {
//                                UI 조작은, UI 쓰레드에게 일을 따로 맡겨주자.
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }

    override fun setValues() {


    }

}