package com.example.colossuem_0903

import android.os.Bundle
import com.example.colossuem_0903.utils.ServerUtil
import org.json.JSONObject

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setValues()
        setupEvents()
    }

    override fun setValues() {

        getMainDataFromServer()
    }

    override fun setupEvents() {

    }

//    서버에서, 메인화면에 보여줄 정보 받아오기
    private fun getMainDataFromServer() {

        ServerUtil.getRequestMainData(mContext, object: ServerUtil.JsonResponseHandler {
            override fun onResponse(jsonObj: JSONObject) {


            }
        })
    }
}