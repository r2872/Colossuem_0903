package com.example.colossuem_0903.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFCMService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

//        푸시알림을 받았을때 실행시킬 코드?
        Log.d("푸시알림", "수신이벤트")
    }
}