//package com.chillarcards.bookmenow.ui.notification
//
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import androidx.navigation.fragment.findNavController
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.chillarcards.bookmenow.R
//import com.chillarcards.bookmenow.databinding.FragmentNotificationBinding
//import com.chillarcards.bookmenow.model.NotificationItem
//import com.chillarcards.bookmenow.utills.Const
//
//class NotificationFragment_Copy : Fragment() {
//
//    private lateinit var notificationViewModel: NotificationViewModel
//    private lateinit var notificationAdapter: NotificationAdapter
//    lateinit var binding: FragmentNotificationBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentNotificationBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setToolbar()
//        setNotification()
//
////        notificationViewModel = ViewModelProvider(this).
////            get(NotificationViewModel::class.java)
////
////        notificationAdapter = NotificationAdapter(emptyList())
////        binding.notifyRv.adapter = notificationAdapter
////        binding.notifyRv.layoutManager = LinearLayoutManager(context)
////
////        notificationViewModel.notifications.observe(viewLifecycleOwner) { notifications ->
////            Log.d("MyFirebaseMsgService", "Received notifications: $notifications")
////            notificationAdapter.setNotifications(notifications)
////            notificationViewModel.updateNotifications(notifications)
////
////        }
// //        val sampleNotifications = listOf(
////            NotificationItem(1,"Notification 1","one"),
////            NotificationItem(2,"Notification 2","two"),
////        )
////        notificationViewModel.updateNotifications(sampleNotifications)
//    }
//
//    private fun setNotification() {
//        Log.d("MyFirebaseMsgService", "setNotify")
//        notificationViewModel = ViewModelProvider(requireActivity()).get(NotificationViewModel::class.java)
//        Log.d("MyFirebaseMsgService", "setNotify 1")
//
//        notificationAdapter = NotificationAdapter(emptyList())
//        binding.notifyRv.adapter = notificationAdapter
//        binding.notifyRv.layoutManager = LinearLayoutManager(context)
//        Log.d("MyFirebaseMsgService", "setNotify 2")
//
//        // Observe the LiveData in the ViewModel
//        Log.d("MyFirebaseMsgService", "Before LiveData Observation")
//        notificationViewModel.newMessageReceived.observe(viewLifecycleOwner) { remoteMessage ->
//            Log.d("MyFirebaseMsgService", "New Message Received: ${remoteMessage.notification?.body}")
//            notificationAdapter.addNotification(
//                NotificationItem(
//                    0,
//                    remoteMessage.notification?.title.orEmpty(),
//                    remoteMessage.notification?.body.orEmpty()
//                )
//            )
//            Log.d("MyFirebaseMsgService", "setNotify 3")
//
//        }
//        Log.d("MyFirebaseMsgService", "setNotify 4")
//
//    }
//
//
//    private fun setToolbar() {
//        binding.toolbar.toolbarBack.setOnClickListener {
//            findNavController().popBackStack()
//        }
//        binding.toolbar.toolbarTitle.text = getString(R.string.notify)
//    }
//}
