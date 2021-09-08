package com.example.colossuem_0903.fcm

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFCMService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

//        푸시알림을 받았을때 실행시킬 코드?
        Log.d("푸시알림", "수신이벤트")

//        토스트로, 받은 내용 (제목) 출력 => 앱을 켜둔 상태에서 알림을 받았을때만 실행됨.
        val title = p0.notification!!.title

//        핸들러 활용 -> UI Thread (메인 핸들러) 접근
        val myHandler = Handler(Looper.getMainLooper())
        myHandler.post {
//            runOnUiThread 와 같은 역할.
            Toast.makeText(applicationContext, title, Toast.LENGTH_SHORT).show()
        }

    }
}