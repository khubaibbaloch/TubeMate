package com.TubeMateApp.tubemate.TubeMateApp.helperClasses.TubeMate

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import com.arthenica.mobileffmpeg.FFmpeg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import javax.inject.Inject

class TubeMateRepository @Inject constructor(
    private val context: Context,
    private val downloadHelper: DownloadHelper,
) {

    suspend fun downloadFiles(
        fileUrls: List<String>,
        isYoutubeVideo: Boolean,
        isYoutubeAudio: Boolean,
    ): List<Long> {
        val downloadIds = mutableListOf<Long>()

        if (isYoutubeVideo) {
            // Separate video and audio URLs and process each pair
            val urlPairs = fileUrls.chunked(2)
            for ((videoUrl, audioUrl) in urlPairs) {
                val uniqueId = UUID.randomUUID().toString()
                val videoFileName = generateFileName(videoUrl, "video_${uniqueId}mp4")
                val audioFileName = generateFileName(audioUrl, "audio_${uniqueId}mp3")

                Log.d(
                    "TubeMateRepository",
                    "Starting download for video: $videoUrl with filename: $videoFileName"
                )
                val videoDownloadId = downloadHelper.downloadFile(videoUrl, videoFileName)
                downloadIds.add(videoDownloadId)

                Log.d(
                    "TubeMateRepository",
                    "Starting download for audio: $audioUrl with filename: $audioFileName"
                )
                val audioDownloadId = downloadHelper.downloadFile(audioUrl, audioFileName)
                downloadIds.add(audioDownloadId)

                CoroutineScope(Dispatchers.IO).launch {
                    waitForDownloadsToComplete(listOf(videoDownloadId, audioDownloadId))
                    mergeAudioVideo(videoFileName, audioFileName)
                }
            }
        } else if (isYoutubeAudio) {
            for (fileUrl in fileUrls) {
                val audioFileName ="TubeMate.audio_${System.currentTimeMillis()}.mp3"
                Log.d(
                    "TubeMateRepository",
                    "Starting download for audio: $fileUrl with filename: $audioFileName"
                )
                val audioDownloadId = downloadHelper.downloadFile(fileUrl, audioFileName)
                downloadIds.add(audioDownloadId)
            }
        } else {
            for (fileUrl in fileUrls) {
                val fileName = generateFileName(fileUrl)
                Log.d(
                    "TubeMateRepository",
                    "Starting download for: $fileUrl with filename: $fileName"
                )
                val downloadId = downloadHelper.downloadFile(fileUrl, fileName)
                downloadIds.add(downloadId)
            }
        }

        Log.d("downloadIds", "downloadFiles: $downloadIds")
        return downloadIds
    }

    private suspend fun generateFileName(
        fileUrl: String,
        defaultName: String = "downloaded_file",
    ): String {
        return withContext(Dispatchers.IO) {
            var attempt = 0
            var success = false
            var fileName = ""

            while (attempt < 3 && !success) {
                try {
                    val connection = URL(fileUrl).openConnection() as HttpURLConnection
                    connection.connect()
                    val contentType = connection.contentType
                    connection.disconnect()

                    val extension = when {
                        contentType.contains("image") -> ".jpg"
                        contentType.contains("video") -> ".mp4"
                        contentType.contains("audio/mpeg") -> ".mp3"
                        contentType.contains("audio/mp4") -> ".m4a"
                        else -> ""
                    }
                    val checkExtension = when(extension){
                        ".jpg" -> "Image"
                        ".mp4" -> "Video"
                        ".mp3" -> "Audio"
                        ".m4a"-> "Audio"
                        else -> ""
                    }

                    fileName =  "TubeMate.${checkExtension}_${System.currentTimeMillis()}.$extension"
                    success = true
                } catch (e: IOException) {
                    attempt++
                    Log.e(
                        "TubeMateRepository",
                        "Error determining file type: ${e.message}, attempt $attempt"
                    )
                    if (attempt >= 3) {
                        fileName = "${System.currentTimeMillis()}.dat"
                        success = true
                    }
                }
            }

            fileName
        }
    }

    @SuppressLint("Range")
    private suspend fun waitForDownloadsToComplete(downloadIds: List<Long>) {
        withContext(Dispatchers.IO) {
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            var allDownloadsComplete: Boolean
            do {
                allDownloadsComplete = true
                for (downloadId in downloadIds) {
                    val query = DownloadManager.Query().setFilterById(downloadId)
                    val cursor: Cursor = downloadManager.query(query)
                    if (cursor.moveToFirst()) {
                        val status =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        if (status != DownloadManager.STATUS_SUCCESSFUL) {
                            allDownloadsComplete = false
                        }
                    }
                    cursor.close()
                }
                if (!allDownloadsComplete) {
                    Thread.sleep(1000) // Sleep for a while before checking again
                }
            } while (!allDownloadsComplete)
        }
    }

    private fun mergeAudioVideo(videoFileName: String, audioFileName: String) {


        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val tubeMateDirectory = File(directory, "TubeMate")

        val videoFilePath = File(tubeMateDirectory , videoFileName).absolutePath
        val audioFilePath = File(tubeMateDirectory , audioFileName).absolutePath
        val outputFileName = "TubeMate.video_${System.currentTimeMillis()}.mp4"
        val outputFilePath = File(tubeMateDirectory , outputFileName).absolutePath

        val ffmpegCommand = arrayOf(
            "-i", videoFilePath,
            "-i", audioFilePath,
            "-c:v", "copy",  // Copy video stream instead of re-encoding
            "-c:a", "copy",  // Copy audio stream instead of re-encoding
            "-strict", "experimental",
            "-y",
            outputFilePath
        )

        Log.d("TubeMateRepository", "Executing FFmpeg command: ${ffmpegCommand.joinToString(" ")}")

        val startTime = System.currentTimeMillis()

        FFmpeg.executeAsync(ffmpegCommand) { executionId, returnCode ->
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime
            if (returnCode == 0) {
                Log.d("TubeMateRepository", "Merge successful: $outputFilePath in $duration ms")
                File(videoFilePath).delete() // Optionally delete the source video file
                File(audioFilePath).delete() // Optionally delete the source audio file

                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(outputFilePath),
                    null
                ) { path, uri ->
                    Log.d("TubeMateRepository", "Scanned $path:")
                }

            } else {
                Log.e("TubeMateRepository", "Merge failed: returnCode $returnCode in $duration ms")
            }
        }
    }


    @SuppressLint("Range")
    suspend fun getDownloadProgress(downloadIds: List<Long>): Int {
        return withContext(Dispatchers.IO) {
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val query = DownloadManager.Query()
            val cursor: Cursor = downloadManager.query(query)
            var totalDownloadedBytes = 0L
            var totalBytes = 0L

            while (cursor.moveToNext()) {
                val downloadId = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID))
                if (downloadIds.contains(downloadId)) {
                    val downloadedBytes =
                        cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    val fileSize =
                        cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                    totalDownloadedBytes += downloadedBytes
                    totalBytes += fileSize
                }
            }
            cursor.close()

            if (totalBytes > 0) {
                ((totalDownloadedBytes * 100L) / totalBytes).toInt()
            } else {
                0
            }
        }
    }
}


/*class TubeMateRepository @Inject constructor(
    private val context: Context,
    private val downloadHelper: DownloadHelper,
) {
    private var _youtubeDownloadIds = mutableListOf<Long>()

    suspend fun downloadFiles(fileUrls: List<String>, isYoutubeVideo: Boolean): List<Long> {
        val downloadIds = mutableListOf<Long>()

        if (isYoutubeVideo) {
            // Separate video and audio URLs and process each pair
            val urlPairs = fileUrls.chunked(2)
            for ((videoUrl, audioUrl) in urlPairs) {
                val uniqueId = UUID.randomUUID().toString()
                val videoFileName = generateFileName(videoUrl, "video_$uniqueId.mp4")
                val audioFileName = generateFileName(audioUrl, "audio_$uniqueId.m4a")

                Log.d("TubeMateRepository", "Starting download for video: $videoUrl with filename: $videoFileName")
                val videoDownloadId = downloadHelper.downloadFile(videoUrl, videoFileName)
                downloadIds.add(videoDownloadId)
                _youtubeDownloadIds.add(videoDownloadId) // Add the video download ID immediately

                Log.d("TubeMateRepository", "Starting download for audio: $audioUrl with filename: $audioFileName")
                val audioDownloadId = downloadHelper.downloadFile(audioUrl, audioFileName)
                downloadIds.add(audioDownloadId)
                _youtubeDownloadIds.add(audioDownloadId) // Add the audio download ID immediately

                // Launch a coroutine to wait for downloads to complete and then merge files
                CoroutineScope(Dispatchers.IO).launch {
                    waitForDownloadsToComplete(listOf(videoDownloadId, audioDownloadId))
                    mergeAudioVideo(videoFileName, audioFileName)
                }
            }
        } else {
            for (fileUrl in fileUrls) {
                val fileName = generateFileName(fileUrl)
                Log.d("TubeMateRepository", "Starting download for: $fileUrl with filename: $fileName")
                val downloadId = downloadHelper.downloadFile(fileUrl, fileName)
                downloadIds.add(downloadId)
            }
        }

        Log.d("downloadIds", "downloadFiles: $downloadIds")
        return downloadIds // Return the list of download IDs immediately
    }



    suspend fun getYouTubeUrls(): List<Long> {
        Log.d("TubeMateRepository", "YouTube Download IDs: $_youtubeDownloadIds")
        return _youtubeDownloadIds
    }

    private suspend fun generateFileName(fileUrl: String, defaultName: String = "downloaded_file"): String {
        return withContext(Dispatchers.IO) {
            var attempt = 0
            var success = false
            var fileName = ""

            while (attempt < 3 && !success) {
                try {
                    val connection = URL(fileUrl).openConnection() as HttpURLConnection
                    connection.connect()
                    val contentType = connection.contentType
                    connection.disconnect()

                    val extension = when {
                        contentType.contains("image") -> ".jpg"
                        contentType.contains("video") -> ".mp4"
                        contentType.contains("audio/mpeg") -> ".mp3"
                        contentType.contains("audio/mp4") -> ".m4a"
                        else -> ""
                    }

                    fileName = "$defaultName$extension"
                    success = true
                } catch (e: IOException) {
                    attempt++
                    Log.e("TubeMateRepository", "Error determining file type: ${e.message}, attempt $attempt")
                    if (attempt >= 3) {
                        fileName = "${System.currentTimeMillis()}.dat"
                        success = true
                    }
                }
            }

            fileName
        }
    }

    @SuppressLint("Range")
    private suspend fun waitForDownloadsToComplete(downloadIds: List<Long>) {
        withContext(Dispatchers.IO) {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            var allDownloadsComplete: Boolean
            do {
                allDownloadsComplete = true
                for (downloadId in downloadIds) {
                    val query = DownloadManager.Query().setFilterById(downloadId)
                    val cursor: Cursor = downloadManager.query(query)
                    if (cursor.moveToFirst()) {
                        val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        if (status != DownloadManager.STATUS_SUCCESSFUL) {
                            allDownloadsComplete = false
                        }
                    }
                    cursor.close()
                }
                if (!allDownloadsComplete) {
                    delay(1000) // Sleep for a while before checking again
                }
            } while (!allDownloadsComplete)
        }
    }

    private fun mergeAudioVideo(videoFileName: String, audioFileName: String) {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val videoFilePath = File(downloadsDir, videoFileName).absolutePath
        val audioFilePath = File(downloadsDir, audioFileName).absolutePath
        val outputFileName = "merged_${System.currentTimeMillis()}.mp4"
        val outputFilePath = File(downloadsDir, outputFileName).absolutePath

        val ffmpegCommand = arrayOf(
            "-i", videoFilePath,
            "-i", audioFilePath,
            "-c:v", "copy",  // Copy video stream instead of re-encoding
            "-c:a", "copy",  // Copy audio stream instead of re-encoding
            "-strict", "experimental",
            "-y",
            outputFilePath
        )

        Log.d("TubeMateRepository", "Executing FFmpeg command: ${ffmpegCommand.joinToString(" ")}")

        val startTime = System.currentTimeMillis()

        FFmpeg.executeAsync(ffmpegCommand) { executionId, returnCode ->
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime
            if (returnCode == 0) {
                Log.d("TubeMateRepository", "Merge successful: $outputFilePath in $duration ms")
                File(videoFilePath).delete() // Optionally delete the source video file
                File(audioFilePath).delete() // Optionally delete the source audio file

                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(outputFilePath),
                    null
                ) { path, uri ->
                    Log.d("TubeMateRepository", "Scanned $path:")
                }

            } else {
                Log.e("TubeMateRepository", "Merge failed: returnCode $returnCode in $duration ms")
            }
        }
    }

    @SuppressLint("Range")
    suspend fun getDownloadProgress(downloadIds: List<Long>): Int {
        return withContext(Dispatchers.IO) {
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val query = DownloadManager.Query()
            val cursor: Cursor = downloadManager.query(query)
            var totalDownloadedBytes = 0L
            var totalBytes = 0L

            while (cursor.moveToNext()) {
                val downloadId = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID))
                if (downloadIds.contains(downloadId)) {
                    val downloadedBytes =
                        cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    val fileSize =
                        cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                    totalDownloadedBytes += downloadedBytes
                    totalBytes += fileSize
                }
            }
            cursor.close()

            if (totalBytes > 0) {
                ((totalDownloadedBytes * 100L) / totalBytes).toInt()
            } else {
                0
            }
        }
    }
}*/
