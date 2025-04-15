package com.bettersocialize.chattrix.view_models

import androidx.lifecycle.ViewModel
import com.bettersocialize.chattrix.model.Chat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow

class ChatListViewModel : ViewModel() {

    val db = FirebaseFirestore.getInstance()
    val _chats = MutableStateFlow<List<Chat>>(emptyList())
}