package com.example.colossuem_0903

import android.os.Bundle
import android.widget.Toast
import com.example.colossuem_0903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject

class SignUpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setupEvents()
        setValues()
    }

    override fun setValues() {

    }

    override fun setupEvents() {

        checkEmail_Btn.setOnClickListener {

//            1. 입력한 이메일을 받아서
            val inputEmail = email_Edt.text.toString()

//            2. 서버에 이메일 중복확인 요청 -> 응답에 따라, 결과 텍스트뷰의 문구 수정.
            ServerUtil.getRequestDupCheck("EMAIL", inputEmail, null)
        }

        signUp_Btn.setOnClickListener {
//            1. 입력한 값 받아서
            val inputEmail = email_Edt.text.toString()
            val inputPw = pw_Edt.text.toString()
            val inputNickName = nickName_Edt.text.toString()

//            2. 서버에서 가입요청 -> 응답자에 따라 어떤 처리?
            ServerUtil.putRequestSignUp(
                inputEmail,
                inputPw,
                inputNickName,
                object : ServerUtil.JsonResponseHandler {
                    override fun onResponse(jsonObj: JSONObject) {

//                        가입 성공 (200) / 실패 (200 외의 값)
//                        실패: 이메일 양식 x, 중복된 이메일, 중복된 닉네임

//                        성공일때 -> {} 내부 비워두자.
//                        실패시만 동작 -> message 에 담긴 가입 실패 사유를 갖고 -> 토스트로 출력,

                        val code = jsonObj.getInt("code")

                        if (code == 200) {

//                            가입에 성공했습니다. 토스트
//                            이전 화면으로 복귀
                            runOnUiThread {
                                Toast.makeText(mContext, "가입에 성공했습니다.", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        } else {
                            val message = jsonObj.getString("message")

                            runOnUiThread {

                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
        }
    }
}