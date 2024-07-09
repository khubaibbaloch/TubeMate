package com.TubeMateApp.tubemate.TubeMateApp.helperClasses.TubeMate

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File

/*class DownloadHelper(private val context: Context) {

    fun downloadFile(url: String, fileName: String): Long {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))
        request.setTitle("Downloading file")
        request.setDescription("Downloading file from $url")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        Log.d("DownloadHelper", "Download request created for: $url")
        return downloadManager.enqueue(request)
    }
}*/

class DownloadHelper(private val context: Context) {

    fun downloadFile(url: String, fileName: String): Long {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        // Create a directory named "TubeMate" in the Downloads folder if it doesn't exist
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val tubeMateDirectory = File(directory, "TubeMate")
        if (!tubeMateDirectory.exists()) {
            tubeMateDirectory.mkdirs()
        }

        // Set the destination path to the TubeMate directory
        val filePath = File(tubeMateDirectory, fileName).absolutePath

        val request = DownloadManager.Request(Uri.parse(url))
        request.setTitle("Downloading file")
        request.setDescription("Downloading file $fileName")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationUri(Uri.parse("file://$filePath"))

        Log.d("DownloadHelper", "Download request created for: $url")
        return downloadManager.enqueue(request)
    }
}
