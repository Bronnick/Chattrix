package com.bettersocialize.chattrix.view_models

import androidx.lifecycle.ViewModel
import com.bettersocialize.chattrix.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    fun loadMessages(chatId: String) {
        db.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null ) {
                    Log.d("myLogs", "An error occured while extracting chats")
                    return@addSnapshotListener
                }
                if(snapshot != null) {
                    val messageList = snapshot.documents.map { doc ->
                        Message(
                            id = doc.id,
                            senderId = doc.getString("senderId").toString(),
                            text = doc.getString("text").toString(),
                            timestamp = doc.getTimestamp("timestamp")?.seconds ?: 0
                        )
                    }
                    _messages.value = messageList
                }
            }
    }

    fun sendMessage(chatId: String, text: String, senderId: String) {
        val message = hashMapOf(
            "senderId" to senderId,
            "text" to text,
            "timestamp" to com.google.firebase.Timestamp.now()
        )
        viewModelScope.launch {
            db.collection("chats").document(chatId).collection("messages")
                .add(message)
        }
    }
}