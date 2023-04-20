package com.example.parentsconnect

import android.net.Uri

class NoticeViewModel(
    val noticeTitle: String,
    val noticeMessage: String,
    val isImportant: Boolean,
    val documentUri: Uri?
) {

}