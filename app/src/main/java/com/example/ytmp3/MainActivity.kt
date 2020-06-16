package com.example.ytmp3

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var buttonView: Button
    private lateinit var urlView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonView = findViewById<Button>(R.id.downloadButton)
        urlView = findViewById<EditText>(R.id.urlEditText)

        //urlView.setText("https://www.youtube.com/watch?v=ZSRHeXYDLko")
    }

    fun startDownload(view: View) {
        var url = urlView.text.toString()
        var id: String? = extractID(url)

        if (id == null) {
            return;
        }

        var message = "Downloading ${id}"
        var toast = Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG)
        toast.show()

        var api_uri = Uri.parse("https://api.transcode.ca/cors/video/youtube/${id}")
        var req = DownloadManager.Request(api_uri)
            .setTitle("${id}.m4a")
            .setDescription("Downloading ${url}")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalFilesDir(this@MainActivity, Environment.DIRECTORY_MUSIC, "${id}.m4a")
            .setAllowedOverMetered(false)
            .setAllowedOverRoaming(true)

        var downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        var downloadID = downloadManager.enqueue(req)
    }

    fun extractID(url: String): String? {
        var regex = Regex("""^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#&?]*).*""")
        var matches = regex.find(url)?.groupValues
        if (matches?.get(7)?.length == 11) {
            return matches.get(7);
        }
        return null;
    }
}
