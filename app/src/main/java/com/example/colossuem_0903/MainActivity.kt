package com.example.colossuem_0903

import android.os.Bundle
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

            val inputId = email_Edt.text
            val inputPw = password_Edt.text
        }
    }

    override fun setValues() {


    }

}