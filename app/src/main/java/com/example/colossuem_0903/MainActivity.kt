package com.example.colossuem_0903

import android.os.Bundle
import com.example.colossuem_0903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_main.*

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
            ServerUtil.postRequestSignIn(inputId, inputPw)
        }
    }

    override fun setValues() {


    }

}