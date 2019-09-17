package com.example.tdm_project.view.adapters

import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.tdm_project.R
import com.example.tdm_project.model.data.Video


class VideoPlayerViewHolder( var parent: View) : RecyclerView.ViewHolder(parent) {

     var media_container: FrameLayout
     var title: TextView
     var thumbnail: ImageView
     var volumeControl: ImageView
     var progressBar: ProgressBar
     var btnControl : Button
     lateinit var requestManager: RequestManager


    init {
        media_container = parent.findViewById(R.id.media_container)
        thumbnail = parent.findViewById(R.id.thumbnail)
        title = parent.findViewById(R.id.title)
        progressBar = parent.findViewById(R.id.progressBar)
        volumeControl = parent.findViewById(R.id.volume_control)
        btnControl = parent.findViewById(R.id.btn_control)
    }

    fun onBind(mediaObject: Video, requestManager: RequestManager) {
        this.requestManager = requestManager
        parent.setTag(this)
        title.setText(mediaObject.videoTitle)
        this.requestManager
            .load(mediaObject.thumbnail)
            .into(thumbnail)
    }

}