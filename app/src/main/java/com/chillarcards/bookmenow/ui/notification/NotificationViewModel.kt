package com.chillarcards.bookmenow.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chillarcards.bookmenow.model.NotificationItem
import com.google.firebase.messaging.RemoteMessage

class NotificationViewModel : ViewModel() {
    private val notificationRepository = NotificationRepository()
    private val _notifications = MutableLiveData<List<NotificationItem>>()
    val notifications: LiveData<List<NotificationItem>> get() = _notifications

    private val _newMessageReceived = MutableLiveData<RemoteMessage>()
    val newMessageReceived: LiveData<RemoteMessage> get() = _newMessageReceived

    fun handleFCMMessage(remoteMessage: RemoteMessage) {
        //_newMessageReceived.value = remoteMessage
        _newMessageReceived.postValue(remoteMessage)

//        val notification = NotificationItem(
//            id = 0,
//            title = remoteMessage.notification?.title.orEmpty(),
//            message = remoteMessage.notification?.body.orEmpty()
//        )
//
//        val currentNotifications = _notifications.value.orEmpty().toMutableList()
//        currentNotifications.add(notification)
//        _notifications.postValue(currentNotifications)
    }

    fun updateNotifications(newNotifications: List<NotificationItem>) {
        _notifications.value = newNotifications
    }

    fun getNotifications(): List<NotificationItem> {
        return notificationRepository.getNotifications()
    }

    fun addNotification(notification: NotificationItem) {
        notificationRepository.addNotification(notification)
    }
}
