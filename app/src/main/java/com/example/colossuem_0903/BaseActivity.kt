package com.example.colossuem_0903

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

abstract class BaseActivity : AppCompatActivity() {

    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this

//        모든화면의 onCreate 에서 커스텀액션바 적용도 실행.
//        액션바가 존재하는 화면에서만 실행.
//        액션바가 존재할때만 -> 별개의 함수를 실행.
        supportActionBar?.let {
//            액션바가 null 이 아닐때만 실행시켜 줄 코드
            setCustomActionBar()
        }

    }

    abstract fun setValues()
    abstract fun setupEvents()

    //    액션바를 커스텀 액션바로 바꿔주는 함수
    fun setCustomActionBar() {

//        기본액션바를 불러오자.
        val defaultActionBar = supportActionBar!!

//        커스텀모드로 변경 -> 우리가 만든 xml 로 적용
        defaultActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        defaultActionBar.setCustomView(R.layout.my_custom_action_bar)

//        양 옆의 여백 제거. -> 모든 영역이 커스텀뷰
        val myToolBar = defaultActionBar.customView.parent as Toolbar
        myToolBar.setContentInsetsAbsolute(0, 0)
    }
}