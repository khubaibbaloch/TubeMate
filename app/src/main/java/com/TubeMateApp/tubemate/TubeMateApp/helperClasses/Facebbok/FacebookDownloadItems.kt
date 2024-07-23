package com.TubeMateApp.tubemate.TubeMateApp.helperClasses.Facebbok

data class FacebookDownloadItems(
    val title: String="Facebook_video",
    val thumbnail: String,
    val downloadProgress: Int,
    val downloadIds: List<Long>,
)
