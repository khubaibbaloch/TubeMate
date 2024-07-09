package com.TubeMateApp.tubemate.TubeMateApp.MainViewModel


import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Movie
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TubeMateApp.tubemate.TubeMateApp.helperClasses.History.MediaFile
import com.TubeMateApp.tubemate.TubeMateApp.helperClasses.Instagram.InstagramDownloadItem
import com.TubeMateApp.tubemate.TubeMateApp.helperClasses.TubeMate.SharedPreferencesHelper
import com.TubeMateApp.tubemate.TubeMateApp.helperClasses.TubeMate.TubeMateRepository
import com.TubeMateApp.tubemate.TubeMateApp.helperClasses.YouTube.YouTubeDownloadItem
import com.TubeMateApp.tubemate.TubeMateApp.helperClasses.YouTube.YouTubeDownloadItemDetails
import com.arthenica.mobileffmpeg.FFmpeg
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.ByteBuffer
import javax.inject.Inject


@Suppress("DEPRECATION")
@HiltViewModel
class TubeMateViewModel @Inject constructor(
    private val repository: TubeMateRepository,
    private val context: Context,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
) : ViewModel() {

    // Global Section
    private val python: Python by lazy {
        // Python.start(AndroidPlatform(context))
        Python.getInstance()
    }

    private val _mediaFiles = MutableStateFlow<List<MediaFile>>(emptyList())
    val mediaFiles: StateFlow<List<MediaFile>> = _mediaFiles

    //Instagram Section

    private val _isInstagramPostFounded = MutableStateFlow<Boolean>(false)
    val isInstagramPostFounded: StateFlow<Boolean> get() = _isInstagramPostFounded

    private val _instagramDownloadItems = MutableStateFlow<List<InstagramDownloadItem>>(emptyList())
    val instagramDownloadItems: StateFlow<List<InstagramDownloadItem>> = _instagramDownloadItems

    //WhatsApp Section
    private val _uriPermissionGranted = MutableStateFlow(false)
    val uriPermissionGranted: StateFlow<Boolean> get() = _uriPermissionGranted


    //YouTube Section
    private val _isYouTubeVideoFounded = MutableStateFlow<Boolean>(false)
    val isYouTubeVideoFounded: StateFlow<Boolean> get() = _isYouTubeVideoFounded

    private val _YouTubeVideoSelectedOption = MutableStateFlow<Boolean>(false)
    val YouTubeVideoSelectedOption: StateFlow<Boolean> = _YouTubeVideoSelectedOption


    private val _YouTubeDownloadItems = MutableStateFlow<List<YouTubeDownloadItem>>(emptyList())
    val YouTubeDownloadItems: StateFlow<List<YouTubeDownloadItem>> = _YouTubeDownloadItems

    private val _YouTubeDownloadItemDetails =
        MutableStateFlow<List<YouTubeDownloadItemDetails>>(emptyList())
    val YouTubeDownloadItemDetails: StateFlow<List<YouTubeDownloadItemDetails>> =
        _YouTubeDownloadItemDetails


    init {
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(context))
        }
        _uriPermissionGranted.value = sharedPreferencesHelper.getBoolean("uri_permission_granted")
    }


    // Global Section
    fun openAppOrBrowser(context: Context, url: String, packageName: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            setPackage(packageName)
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            // App is not installed, open in browser
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent)
        }
    }

    fun loadMediaFiles(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val mediaFiles = fetchMediaFiles()
            withContext(Dispatchers.Main) {
                _mediaFiles.value = mediaFiles
            }
        }
    }

    private fun fetchMediaFiles(): List<MediaFile> {
        val mediaFiles = mutableListOf<MediaFile>()

        // Specify the directory to log data from
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val tubeMateDirectory = File(directory, "TubeMate")

        // Check if the directory exists and is a directory
        if (tubeMateDirectory.exists() && tubeMateDirectory.isDirectory) {
            tubeMateDirectory.listFiles()?.forEach { file ->
                // Assuming MediaFile has appropriate properties like Uri, displayName, size
                mediaFiles.add(MediaFile(Uri.fromFile(file), file.name, file.length()))
            }
        }

        return mediaFiles
    }



    //Instagram Section
    fun downloadInstagramPost(
        videoUrls: List<String>,
        userName: String,
        postUrl: String,
        postSize: Int,
        isYoutubeVideo: Boolean,
        isYoutubeAudio: Boolean,
    ) {
        viewModelScope.launch {
            try {
                val downloadIds =
                    repository.downloadFiles(videoUrls, isYoutubeVideo, isYoutubeAudio)
                Log.d("TAG", "Download started with ids=$downloadIds")
                addInstagramDownloadItem(userName, postUrl, postSize, downloadIds)
                monitorInstagramPostDownloadProgress(downloadIds)
            } catch (e: Exception) {
                Log.e("ViewModel", "Error downloading video: ${e.message}")
            }
        }
    }

    private fun addInstagramDownloadItem(
        userName: String,
        postUrl: String,
        postSize: Int,
        downloadIds: List<Long>,
    ) {
        val newItem = InstagramDownloadItem(
            userName = userName,
            postUrl = postUrl,
            postSize = postSize,
            downloadProgress = 0,
            downloadIds = downloadIds
        )
        val updatedList = _instagramDownloadItems.value + newItem
        _instagramDownloadItems.value = updatedList
        Log.d("TAG", "Added new download item: $newItem")
    }

    private suspend fun monitorInstagramPostDownloadProgress(downloadIds: List<Long>) {
        withContext(Dispatchers.IO) {
            while (true) {
                try {
                    val progress = repository.getDownloadProgress(downloadIds)
                    Log.d("TAG", "Fetched download progress for ids=$downloadIds: $progress%")
                    updateInstagramPostDownloadProgress(downloadIds, progress)
                    if (progress == 100) {
                        Log.d("TAG", "Download complete for ids=$downloadIds")
                        break
                    }
                    delay(1000) // Check progress every second
                } catch (e: Exception) {
                    Log.e("ViewModel", "Error monitoring download progress: ${e.message}")
                    break // Exit loop on error
                }
            }
        }
    }

    private suspend fun updateInstagramPostDownloadProgress(
        downloadIds: List<Long>,
        progress: Int,
    ) {
        val currentList = _instagramDownloadItems.value
        Log.d("TAG", "Current _instagramDownloadItems: $currentList")

        val updatedList = currentList.map { item ->
            if (item.downloadIds == downloadIds) {
                Log.d("TAG", "Updating item: $item with progress=$progress")
                item.copy(downloadProgress = progress)
            } else {
                item
            }
        }

        Log.d("TAG", "UpdatedList before setting _instagramDownloadItems: $updatedList")

        withContext(Dispatchers.Main) {
            _instagramDownloadItems.value = updatedList
            Log.d(
                "TAG",
                "Updated _instagramDownloadItems: ${_instagramDownloadItems.value.map { it.downloadProgress }}"
            )
        }
    }

    fun fetchInstagramInfo(videoUrl: String, context: Context) {
        _isInstagramPostFounded.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val getVideoInfo = python.getModule("InstagramScraper")
                val videoInfo = getVideoInfo.callAttr("get_url_insta", videoUrl)

                if (videoInfo != null) {
                    val postUrl = videoInfo.callAttr("get", "post_url")?.toString() ?: ""
                    val userName = videoInfo.callAttr("get", "username")?.toString() ?: ""
                    val mediaUrlsPyObject = videoInfo.callAttr("get", "media_urls")
                    val mediaUrls = mediaUrlsPyObject.asList().map { it.toString() }

                    withContext(Dispatchers.Main) {
                        downloadInstagramPost(
                            videoUrls = mediaUrls,
                            postUrl = postUrl,
                            postSize = mediaUrls.size,
                            userName = userName,
                            isYoutubeVideo = false,
                            isYoutubeAudio = false
                        )
                        _isInstagramPostFounded.value = false
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.e("TAG", "Failed to retrieve video information")
                        _isInstagramPostFounded.value = false
                        Toast.makeText(
                            context, "Please check the URL and try again", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("TAG", "Error fetching video information: ${e.message}")
                    _isInstagramPostFounded.value = false
                    Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // WhatsApp Section

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getWhatsAppStatuses(context: Context): List<Uri> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10 and above, retrieve status URIs using storage access framework
            getWhatsAppStatusesQ(context)
        } else {
            // For versions less than Android 10, retrieve status files directly from file system
            getWhatsAppStatusesBeforeQ()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getWhatsAppStatusesQ(context: Context): List<Uri> {
        val uriString =
            sharedPreferencesHelper.getString("whatsapp_status_uri") ?: return emptyList()
        val uri = Uri.parse(uriString)
        val contentResolver = context.contentResolver
        val documentsUri = DocumentsContract.buildChildDocumentsUriUsingTree(
            uri, DocumentsContract.getTreeDocumentId(uri)
        )

        val statusFiles = mutableListOf<Uri>()
        contentResolver.query(
            documentsUri, arrayOf(
                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                DocumentsContract.Document.COLUMN_MIME_TYPE
            ), null, null, null
        )?.use { cursor ->
            val idIndex = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID)
            val mimeTypeIndex = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE)
            while (cursor.moveToNext()) {
                val documentId = cursor.getString(idIndex)
                val mimeType = cursor.getString(mimeTypeIndex)
                if (mimeType.startsWith("image/") || mimeType.startsWith("video/")) {
                    val documentUri = DocumentsContract.buildDocumentUriUsingTree(uri, documentId)
                    statusFiles.add(documentUri)
                    Log.d("TAG", "Status file found: $documentUri")
                } else {
                    Log.d(
                        "TAG", "File is not an image or video: $documentId (MIME type: $mimeType)"
                    )
                }
            }
        }

        return statusFiles
    }

    private fun getWhatsAppStatusesBeforeQ(): List<Uri> {
        val statusDir = File(Environment.getExternalStorageDirectory(), "/WhatsApp/Media/.Statuses")
        val statusFiles = mutableListOf<Uri>()

        if (statusDir.exists() && statusDir.isDirectory) {
            statusDir.listFiles()?.forEach { file ->
                // Filter files based on your criteria (e.g., only images or videos)
                if (file.isFile && (file.name.endsWith(".jpg") || file.name.endsWith(".mp4"))) {
                    val fileUri = Uri.fromFile(file)
                    statusFiles.add(fileUri)
                    Log.d("TAG", "Status file found: ${fileUri.path}")
                }
            }
        } else {
            Log.d("TAG", "Status directory not found or not accessible")
        }

        return statusFiles
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestPermissionQ(context: Context) {
        val sm = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager

        val intent = sm.primaryStorageVolume.createOpenDocumentTreeIntent()
        val startDir = "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses"

        var uri = intent.getParcelableExtra<Uri>("android.provider.extra.INITIAL_URI")

        var scheme = uri.toString()
        scheme = scheme.replace("/root/", "/document/")
        scheme += "%3A$startDir"

        uri = Uri.parse(scheme)

        Log.d("URI", uri.toString())

        intent.putExtra("android.provider.extra.INITIAL_URI", uri)
        intent.setFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        )

        (context as? Activity)?.startActivityForResult(intent, 123)
    }

    fun saveUri(uri: Uri) {
        sharedPreferencesHelper.saveString("whatsapp_status_uri", uri.toString())
        sharedPreferencesHelper.saveBoolean("uri_permission_granted", true)
        _uriPermissionGranted.value = true
    }

    fun copyFileToDownloads(context: Context, uri: Uri, fileName: String): Boolean {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val tubeMateDirectory = File(directory, "TubeMate")
        val destinationPath = File(tubeMateDirectory, fileName)


        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(destinationPath).use { outputStream ->
                    copyStream(inputStream, outputStream)
                }
            }
            Log.d("copyFileToDownloads", "File copied to: ${destinationPath.absolutePath}")
            val fileExists = destinationPath.exists()
            Log.d("copyFileToDownloads", "File exists after copy: $fileExists")
            if (fileExists) {
                // Scan the file to make it visible in the gallery
                MediaScannerConnection.scanFile(
                    context, arrayOf(destinationPath.absolutePath), null
                ) { path, uri ->
                    Log.d("copyFileToDownloads", "Scanned file: $path, Uri: $uri")
                }
            }
            fileExists
        } catch (e: Exception) {
            Log.e("copyFileToDownloads", "Failed to copy file: ${e.message}")
            false
        }
    }

    private fun copyStream(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (input.read(buffer).also { bytesRead = it } != -1) {
            output.write(buffer, 0, bytesRead)
        }
    }

    fun getFileExtension(context: Context, uri: Uri): String? {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri)
        return when (mimeType) {
            "image/jpeg" -> "jpg"
            "image/png" -> "png"
            "video/mp4" -> "mp4"
            else -> null
        }
    }
    fun  getExtensionTypes (context: Context, uri: Uri): String?{
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri)
        return when (mimeType) {
            "image/jpeg" -> "Image"
            "image/png" -> "Image"
            "video/mp4" -> "video"
            else -> null
        }
    }

    // Facebook Section

    fun fetchFacebookInfo(context: Context, videoUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val getVideoInfo = python.getModule("FacebookScraper")
                val videoInfo = getVideoInfo.callAttr("get_video_url", videoUrl)
                if (videoInfo != null) {
                    val videoUrl = videoInfo.callAttr("get", "video_url").toString()
                    val audioUrl = videoInfo.callAttr("get", "audio_url").toString()

                    //val listUrls = listOf(videoUrl,audioUrl)
                    // downloadVideo(listUrls,"2","1",1)
                    // Download video and audio files
                    val videoFile = downloadFile(context, videoUrl, "video.mp4")
                    val audioFile = downloadFile(context, audioUrl, "audio.aac")

                    if (videoFile != null && audioFile != null) {
                        // Combine video and audio using FFmpeg
                        val outputFilePath = combineVideoAndAudio(context, videoFile, audioFile)
                        withContext(Dispatchers.Main) {
                            Log.d("TAG", "Combined video path: $outputFilePath")
                            // Handle success, e.g., update UI with combined video path
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Log.e("TAG", "Failed to download video or audio")
                            // Handle failure to download video or audio
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.e("TAG", "Failed to retrieve video information")
                        // Handle failure to retrieve video information
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("TAG", "Error fetching video information: ${e.message}")
                    // Handle error fetching video information
                }
            }
        }
    }

    // Function to download a file and return its File object
    suspend fun downloadFile(context: Context, fileUrl: String, fileName: String): File? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(fileUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 15000
                connection.readTimeout = 15000
                connection.requestMethod = "GET"
                connection.doInput = true
                connection.connect()

                val inputStream = connection.inputStream
                val file = File(context.filesDir, fileName)
                val outputStream = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var len: Int
                while (inputStream.read(buffer).also { len = it } != -1) {
                    outputStream.write(buffer, 0, len)
                }
                outputStream.flush()
                outputStream.close()
                inputStream.close()
                file
            } catch (e: IOException) {
                Log.e("TAG", "Error downloading file: ${e.message}")
                null
            }
        }
    }

    suspend fun combineVideoAndAudio(context: Context, videoFile: File, audioFile: File): String? {
        // Get external storage directory (Downloads directory)
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs()  // Create the directory if it doesn't exist
        }

        val outputFileName = "combined_video.mp4"
        val outputFilePath = File(downloadsDir, outputFileName).absolutePath

        /*       val command = arrayOf(
                   "-i",
                   videoFile.absolutePath,
                   "-i",
                   audioFile.absolutePath,
                   "-c:v",
                   "libx264",  // Re-encode video to H.264
                   "-c:a",
                   "aac",      // Re-encode audio to AAC
                   "-strict",
                   "experimental",
                   "-y",               // Overwrite output file if it exists
                   outputFilePath
               )*/
        val command = arrayOf(
            "-i",
            videoFile.absolutePath,
            "-i",
            audioFile.absolutePath,
            "-c:v", "copy",  // Copy video stream instead of re-encoding
            "-c:a", "copy",  // Copy audio stream instead of re-encoding
            "-strict", "experimental",
            "-y",
            outputFilePath
        )
        return withContext(Dispatchers.IO) {
            val rc = FFmpeg.execute(command)

            if (rc == 0) {
                // Command executed successfully
                Log.d("TAG", "Combined video and audio successfully: $outputFilePath")
                outputFilePath
            } else {
                // Command failed with error
                Log.e("TAG", "Error combining video and audio: FFmpeg returned $rc")
                null
            }
        }
    }


    // YouTube Section
    /*    fun fetchYouTubeVideoInfo(videoUrl: String, context: Context) {
        _isYouTubeVideoFounded.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val getVideoInfo = python.getModule("YouTubeScraper")
                val videoInfo = getVideoInfo.callAttr("get_url", videoUrl)
                if (videoInfo != null) {
                    val id = videoInfo.callAttr("get", "id")?.toString() ?: "N/A"
                    val title = videoInfo.callAttr("get", "title")?.toString() ?: "N/A"
                    val description = videoInfo.callAttr("get", "description")?.toString() ?: "N/A"
                    val thumbnail = videoInfo.callAttr("get", "thumbnail")?.toString() ?: "N/A"
                    val uploadDate = videoInfo.callAttr("get", "upload_date")?.toString() ?: "N/A"
                    val uploader = videoInfo.callAttr("get", "uploader")?.toString() ?: "N/A"
                    val duration = videoInfo.callAttr("get", "duration")?.toString() ?: "N/A"
                    val viewCount = videoInfo.callAttr("get", "view_count")?.toString() ?: "N/A"
                    val likeCount = videoInfo.callAttr("get", "like_count")?.toString() ?: "N/A"
                    val dislikeCount = videoInfo.callAttr("get", "dislike_count")?.toString() ?: "N/A"

                    // Extract audio URL
                    val audioUrl = videoInfo.callAttr("get", "audio_url")?.toString() ?: "N/A"

                    // Extract video URLs
                    val videoUrlsPyObject = videoInfo.callAttr("get", "video_urls")
                    val videoUrlsMap: Map<PyObject, PyObject> = videoUrlsPyObject?.asMap() ?: emptyMap()

                    val videoUrls: Map<String, Map<String, String>> = videoUrlsMap.map { (key, value) ->
                        val valueMap: Map<PyObject, PyObject> = value.asMap()
                        val url = valueMap.entries.find { it.key.toString() == "url" }?.value?.toString() ?: "N/A"
                        val size = valueMap.entries.find { it.key.toString() == "size" }?.value?.toString() ?: "N/A"
                        key.toString() to mapOf(
                            "url" to url,
                            "size" to size
                        )
                    }.toMap()

                    // Log the information with appropriate tags
                    withContext(Dispatchers.Main) {
                        Log.d("TAG", "Video ID: $id")
                        Log.d("TAG", "Title: $title")
                        Log.d("TAG", "Description: $description")
                        Log.d("TAG", "Thumbnail URL: $thumbnail")
                        Log.d("TAG", "Upload Date: $uploadDate")
                        Log.d("TAG", "Uploader: $uploader")
                        Log.d("TAG", "Duration: $duration")
                        Log.d("TAG", "View Count: $viewCount")
                        Log.d("TAG", "Like Count: $likeCount")
                        Log.d("TAG", "Dislike Count: $dislikeCount")
                        Log.d("TAG", "Audio URL: $audioUrl")

                        videoUrls.forEach { (resolution, data) ->
                            Log.d("TAG", "Video URL ($resolution): ${data["url"]}, Size: ${data["size"]}")
                        }

                        val video1080pUrl = videoUrls["1080p"]?.get("url")

                        val videoList = videoUrls.values.map { it["url"].toString() }
                        val audioList = listOf(audioUrl)
                        addYouTubeDownloadItemDetails(
                            title = title,
                            thumbnail = thumbnail,
                            videoList = videoUrls,
                            audioList = audioList
                        )
                        _YouTubeVideoSelectedOption.value = true
                        _isYouTubeVideoFounded.value = false
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.e("TAG", "Failed to retrieve video information")
                        _isYouTubeVideoFounded.value = false
                        Toast.makeText(context, "Please check the URL and try again", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("TAG", "Error fetching video information: ${e.message}")
                    _isYouTubeVideoFounded.value = false
                    Toast.makeText(context, "try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
*/


    fun fetchYouTubeVideoInfo(videoUrl: String, context: Context) {
        _isYouTubeVideoFounded.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val getVideoInfo = python.getModule("YouTubeScraper")
                val videoInfo = getVideoInfo.callAttr("get_url", videoUrl)
                if (videoInfo != null) {
                    //val id = videoInfo.callAttr("get", "id")?.toString() ?: "N/A"
                    val title = videoInfo.callAttr("get", "title")?.toString() ?: "N/A"
                    //val description = videoInfo.callAttr("get", "description")?.toString() ?: "N/A"
                    val thumbnail = videoInfo.callAttr("get", "thumbnail")?.toString() ?: "N/A"
                    // val uploadDate = videoInfo.callAttr("get", "upload_date")?.toString() ?: "N/A"
                    //val uploader = videoInfo.callAttr("get", "uploader")?.toString() ?: "N/A"
                    //val duration = videoInfo.callAttr("get", "duration")?.toString() ?: "N/A"
                    //val viewCount = videoInfo.callAttr("get", "view_count")?.toString() ?: "N/A"
                    //val likeCount = videoInfo.callAttr("get", "like_count")?.toString() ?: "N/A"
                    //val dislikeCount =
                    videoInfo.callAttr("get", "dislike_count")?.toString() ?: "N/A"

                    // Extract audio URLs with formats and sizes
                    val audioUrlsPyObject = videoInfo.callAttr("get", "audio_urls")
                    val audioUrlsList: List<Map<String, String>> =
                        audioUrlsPyObject?.asList()?.map {
                            val audioUrl = it.callAttr("get", "url")?.toString() ?: "N/A"
                            val format = it.callAttr("get", "format")?.toString() ?: "N/A"
                            mapOf("url" to audioUrl, "format" to format)
                        } ?: emptyList()

                    // Extract video URLs
                    val videoUrlsPyObject = videoInfo.callAttr("get", "video_urls")
                    val videoUrlsMap: Map<PyObject, PyObject> =
                        videoUrlsPyObject?.asMap() ?: emptyMap()

                    val videoUrls: Map<String, Map<String, String>> =
                        videoUrlsMap.map { (key, value) ->
                            val valueMap: Map<PyObject, PyObject> = value.asMap()
                            val url =
                                valueMap.entries.find { it.key.toString() == "url" }?.value?.toString()
                                    ?: "N/A"
                            val size =
                                valueMap.entries.find { it.key.toString() == "size" }?.value?.toString()
                                    ?: "N/A"
                            key.toString() to mapOf(
                                "url" to url, "size" to size
                            )
                        }.toMap()

                    // Log the information with appropriate tags
                    withContext(Dispatchers.Main) {
                        //Log.d("TAG", "Video ID: $id")
                        Log.d("TAG", "Title: $title")
                        //Log.d("TAG", "Description: $description")
                        Log.d("TAG", "Thumbnail URL: $thumbnail")
                        /* Log.d("TAG", "Upload Date: $uploadDate")
                         Log.d("TAG", "Uploader: $uploader")
                         Log.d("TAG", "Duration: $duration")
                         Log.d("TAG", "View Count: $viewCount")
                         Log.d("TAG", "Like Count: $likeCount")
                         Log.d("TAG", "Dislike Count: $dislikeCount")*/

                        /*audioUrlsList.forEachIndexed { index, audioInfo ->
                            val audioUrl = audioInfo["url"] ?: "N/A"
                            val format = audioInfo["format"] ?: "N/A"
                            Log.d("TAG", "Audio URL $index: Format: $format, URL: $audioUrl")
                        }

                        videoUrls.forEach { (resolution, data) ->
                            Log.d(
                                "TAG",
                                "Video URL ($resolution): ${data["url"]}, Size: ${data["size"]}"
                            )
                        }*/

                        // Update your LiveData or perform further actions
                        addYouTubeDownloadItemDetails(
                            title = title,
                            thumbnail = thumbnail,
                            videoList = videoUrls,
                            audioList = audioUrlsList
                        )

                        _YouTubeVideoSelectedOption.value = true
                        _isYouTubeVideoFounded.value = false
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.e("TAG", "Failed to retrieve video information")
                        _isYouTubeVideoFounded.value = false
                        Toast.makeText(
                            context,
                            "Please check the URL and try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("TAG", "Error fetching video information: ${e.message}")
                    _isYouTubeVideoFounded.value = false
                    Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun downloadYouTubeVideo(
        videoUrls: List<String>,
        title: String,
        thumbnail: String,
        isYouTubeVideo: Boolean,
        isYoutubeAudio: Boolean,
    ) {
        viewModelScope.launch {
            try {
                val downloadIds = repository.downloadFiles(
                    videoUrls,
                    isYouTubeVideo,
                    isYoutubeAudio
                )
                Log.d("TAG", "Download started with ids=$downloadIds")
                addYouTubeDownloadItem(title, thumbnail, downloadIds)
                monitorYouTubeVideoDownloadProgress(downloadIds)
            } catch (e: Exception) {
                Log.e("ViewModel", "Error downloading video: ${e.message}")
            }
        }
    }

    private fun addYouTubeDownloadItem(
        title: String,
        thumbnail: String,
        downloadIds: List<Long>,
    ) {
        val newItem = YouTubeDownloadItem(
            title = title, thumbnail = thumbnail, downloadProgress = 0, downloadIds = downloadIds
        )
        val updatedList = _YouTubeDownloadItems.value + newItem
        _YouTubeDownloadItems.value = updatedList

        Log.d("TAG", "Added new download item: $newItem")
    }

    private suspend fun monitorYouTubeVideoDownloadProgress(downloadIds: List<Long>) {
        withContext(Dispatchers.IO) {
            while (true) {
                try {
                    val progress = repository.getDownloadProgress(downloadIds)
                    Log.d("TAG", "Fetched download progress for ids=$downloadIds: $progress%")
                    updateYouTubeVideoDownloadProgress(downloadIds, progress)
                    if (progress == 100) {
                        Log.d("TAG", "Download complete for ids=$downloadIds")
                        break
                    }
                    delay(1000) // Check progress every second
                } catch (e: Exception) {
                    Log.e("ViewModel", "Error monitoring download progress: ${e.message}")
                    break // Exit loop on error
                }
            }
        }
    }

    private suspend fun updateYouTubeVideoDownloadProgress(downloadIds: List<Long>, progress: Int) {
        val currentList = _YouTubeDownloadItems.value
        Log.d("TAG", "Current _youTubeDownloadItems: $currentList")

        val updatedList = currentList.map { item ->
            if (item.downloadIds == downloadIds) {
                Log.d("TAG", "Updating item: $item with progress=$progress")
                item.copy(downloadProgress = progress)
            } else {
                item
            }
        }

        Log.d("TAG", "UpdatedList before setting _youTubeDownloadItems: $updatedList")

        withContext(Dispatchers.Main) {
            _YouTubeDownloadItems.value = updatedList
            Log.d(
                "TAG",
                "Updated _youTubeDownloadItems: ${_YouTubeDownloadItems.value.map { it.downloadProgress }}"
            )
        }
    }

    fun updateYouTubeVideoSelectedOption(newValue: Boolean) {
        _YouTubeVideoSelectedOption.value = newValue
        _YouTubeDownloadItemDetails.value = emptyList()
    }

    private fun addYouTubeDownloadItemDetails(
        title: String,
        thumbnail: String,
        videoList: Map<String, Map<String, String>>,
        audioList: List<Map<String, String>>,
    ) {
        val newItem = YouTubeDownloadItemDetails(
            title = title, thumbnail = thumbnail, videoList = videoList, audioList = audioList
        )
        val updatedList = _YouTubeDownloadItemDetails.value + newItem
        _YouTubeDownloadItemDetails.value = updatedList

        Log.d("TAG", "Added new download item: $newItem")
    }

}

