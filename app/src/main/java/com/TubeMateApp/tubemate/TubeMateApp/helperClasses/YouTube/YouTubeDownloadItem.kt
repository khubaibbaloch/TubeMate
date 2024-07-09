package com.TubeMateApp.tubemate.TubeMateApp.helperClasses.YouTube

data class YouTubeDownloadItem(
    val title: String,
    val thumbnail: String,
    val downloadProgress: Int,
    val downloadIds: List<Long>
)
data class YouTubeDownloadItemDetails(
    val title: String,
    val thumbnail: String,
    val videoList: Map<String,Map<String,String>>,
    val audioList: List<Map<String,String>>,
)
