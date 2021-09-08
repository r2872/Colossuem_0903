package com.example.colossuem_0903

import android.os.Bundle
import android.util.Log
import com.example.colossuem_0903.adapters.NotificationAdapter
import com.example.colossuem_0903.datas.NotificationData
import com.example.colossuem_0903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_notification_list.*
import org.json.JSONObject

class NotificationListActivity : BaseActivity() {

    val mNotificationList = ArrayList<NotificationData>()
    lateinit var mNotificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)

        setValues()
        setupEvents()
    }

    override fun setValues() {

        getNotificationsFromServer()

        mNotificationAdapter =
            NotificationAdapter(mContext, R.layout.notification_list_item, mNotificationList)
        notiListView.adapter = mNotificationAdapter
    }

    override fun setupEvents() {

    }

    private fun getNotificationsFromServer() {

        ServerUtil.getRequestNotificationCountOrList(
            mContext,
            true,
            object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObj: JSONObject) {

                    val dataObj = jsonObj.getJSONObject("data")
                    val notificationArr = dataObj.getJSONArray("notifications")

                    for (i in 0 until notificationArr.length()) {

                        val notiObj = notificationArr.getJSONObject(i)
                        Log.d("서버응답", notiObj.toString())

                        val tempNotificationData =
                            NotificationData.getNotificationDataFromJson(notiObj)

                        mNotificationList.add(tempNotificationData)
                    }
                    runOnUiThread {
                        mNotificationAdapter.notifyDataSetChanged()
                    }
                    checkNotification()
                }
            })

    }

    private fun checkNotification() {

        ServerUtil.postRequestNotificationCheck(
            mContext,
            mNotificationList[0].id,
            null
        )

    }
}