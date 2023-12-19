package com.chillarcards.bookmenow.ui.notification

import com.chillarcards.bookmenow.model.NotificationItem

class NotificationRepository {
    private val notifications = mutableListOf<NotificationItem>()

    fun getNotifications(): List<NotificationItem> {
        return notifications
    }

    fun addNotification(notification: NotificationItem) {
        notifications.add(notification)
    }

}
