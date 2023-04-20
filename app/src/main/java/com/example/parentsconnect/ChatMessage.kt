package com.example.parentsconnect

class ChatMessage {
    var message: String? = null
    var sender_id: Int? = null
//    var receiver_id: Int? = null

    constructor() {}

    constructor(message: String?, sender_id: Int?) {
        this.message = message
        this.sender_id = sender_id
//        this.receiver_id = receiver_id
    }
}