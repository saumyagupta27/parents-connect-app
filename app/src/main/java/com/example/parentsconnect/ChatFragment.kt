package com.example.parentsconnect

import android.content.Context
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ChatFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        val db = DatabaseHelper(requireContext())
        val sharedPref = requireActivity().getSharedPreferences("parentsConnectPref", Context.MODE_PRIVATE)

        val senderId = sharedPref.getInt("userId", 0)
        val receiverId = requireArguments().getInt("EXTRA_USER_SELECTED_ID")

        val recyclerView = view.findViewById<RecyclerView>(R.id.chatRecyclerView)
        val messageBox = view.findViewById<EditText>(R.id.messageBox)
        val sendButton = view.findViewById<ImageView>(R.id.sendButtonImage)
        val backBtn = view.findViewById<ImageView>(R.id.backBtnChat)
        val userSelectedTextView = view.findViewById<TextView>(R.id.userSelectedTextView)

        // set the receiver name to the action bar
        val userSelectedName = db.getUserName(receiverId)
        userSelectedTextView.text = "$userSelectedName"

        // mark all unread messages between users to read
        db.markAllChatsRead(receiverId, senderId)

        // store all messages exchanged between the two users in a list
        val messageList = ArrayList<ChatMessage>()
        val cursor = db.getMessageList(senderId, receiverId)
        while(cursor.moveToNext()) {
            val msg = cursor.getString(cursor.getColumnIndexOrThrow("MESSAGE"))
            val cursorSenderId = cursor.getInt(cursor.getColumnIndexOrThrow("SENDER_ID"))
            messageList.add(ChatMessage(msg, cursorSenderId))
        }

        // create and set ChatMessageAdapter to display all messages
        val msgAdapter = ChatMessageAdapter(requireContext(), messageList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = msgAdapter

        // set an OnClickListener to the send
        sendButton.setOnClickListener{

            val message = messageBox.text.toString()
            val res = db.addChatMessage(senderId, receiverId, message)
            if(res) {
                messageBox.setText("")
            } else {
                Toast.makeText(requireContext(), "Message could not be sent", Toast.LENGTH_SHORT).show()
            }
            val cursor = db.getMessageList(senderId, receiverId)
            messageList.clear()
            while(cursor.moveToNext()) {
                val msg = cursor.getString(cursor.getColumnIndexOrThrow("MESSAGE"))
                val cursorSenderId = cursor.getInt(cursor.getColumnIndexOrThrow("SENDER_ID"))
                messageList.add(ChatMessage(msg, cursorSenderId))
            }
            msgAdapter.notifyDataSetChanged()
        }

        backBtn.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        return view
    }

//    override fun onResume() {
//        super.onResume()
//        val dbHelper = DatabaseHelper(requireContext())
//        dbHelper.setDataChangeListener(this)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        val dbHelper = DatabaseHelper(requireContext())
//        dbHelper.setDataChangeListener(this)
//    }

//    override fun onDataChanged(view: View) {
//        val db = DatabaseHelper(requireContext())
//
//        val receiverId = requireArguments().getInt("EXTRA_USER_SELECTED_ID")
//        val userName = db.getUserName(receiverId)
//        (requireActivity() as AppCompatActivity).supportActionBar?.title = userName
//
//        val sharedPref = requireActivity().getSharedPreferences("parentsConnectPref", Context.MODE_PRIVATE)
//        val senderId = sharedPref.getInt("userId", 0)
//
//
//        val recyclerView = view.findViewById<RecyclerView>(R.id.chatRecyclerView)
//        val messageBox = view.findViewById<EditText>(R.id.messageBox)
//        val sendButton = view.findViewById<ImageView>(R.id.sendButtonImage)
//        val messageList = ArrayList<ChatMessage>()
//
//        val cursor = db.getMessageList(senderId, receiverId)
//        while(cursor.moveToNext()) {
//            val msg = cursor.getString(cursor.getColumnIndexOrThrow("MESSAGE"))
//            val cursorSenderId = cursor.getInt(cursor.getColumnIndexOrThrow("SENDER_ID"))
//            messageList.add(ChatMessage(msg, cursorSenderId))
//        }
//
//        val msgAdapter = ChatMessageAdapter(requireContext(), messageList)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = msgAdapter
//
//    }


}