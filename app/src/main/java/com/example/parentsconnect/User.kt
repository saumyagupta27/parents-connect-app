package com.example.parentsconnect

class User {
    var id: Int? = null
    var email: String? = null
    var name: String? = null
    var image: ByteArray? = null
    var isUnread: Boolean? = null

    constructor() {}
    constructor(id: Int?, email: String?, name: String?, image: ByteArray?, isUnread: Boolean?) {
        this.id = id
        this.email = email
        this.name = name
        this.image = image
        this.isUnread = isUnread
    }
}
