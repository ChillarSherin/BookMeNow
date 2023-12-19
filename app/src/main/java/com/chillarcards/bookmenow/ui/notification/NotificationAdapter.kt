package com.chillarcards.bookmenow.ui.notification

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.chillarcards.bookmenow.R
import com.chillarcards.bookmenow.model.NotificationItem

class NotificationAdapter(private var notifications: List<NotificationItem>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notifications[position]
        holder.bind(notification)
        Log.d("TAG", "Message data Notification: ${notification.title}")
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    fun setNotifications(newNotifications: List<NotificationItem>) {
        notifications = newNotifications
        notifyDataSetChanged()
    }

    fun addNotification(notification: NotificationItem) {
        notifications = notifications.toMutableList().apply { add(notification) }
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.notify_title)
        private val messageTextView: TextView = view.findViewById(R.id.notify_msg)

        fun bind(notification: NotificationItem) {
            titleTextView.text = notification.title
            messageTextView.text = notification.message
        }
    }
}
