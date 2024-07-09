package com.TubeMateApp.tubemate.TubeMateApp.helperClasses.Instagram

data class InstagramDownloadItem(
    val userName: String,
    val postUrl: String,
    val postSize: Int,
    val downloadProgress: Int,
    val downloadIds: List<Long> // Changed to List<Long>
)
