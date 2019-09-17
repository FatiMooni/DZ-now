package com.example.tdm_project.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.RequestManager
import com.example.tdm_project.view.adapters.VideoPlayerRecyclerAdapter
import com.example.tdm_project.view.CustomComponent.VerticalSpacingItemDecorator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tdm_project.R
import com.example.tdm_project.model.data.Video
import com.example.tdm_project.services.App
import com.example.tdm_project.view.CustomComponent.VideoPlayerRecyclerView
import org.jetbrains.anko.doAsync
import java.util.*
import kotlin.collections.ArrayList


class VideosActivity : AppCompatActivity() {

    lateinit var mRecyclerView : VideoPlayerRecyclerView
   // var videosLiveData : LiveData<PagedList<Video>>?= null
   // var data : DataSource.Factory<Int,Video>?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)
        mRecyclerView = findViewById(R.id.recycler_view)

        initRecyclerView()



    }

    private fun initRecyclerView() {
       //mRecyclerView = VideoPlayerRecyclerView(this)
        val layoutManager = LinearLayoutManager(this)
        mRecyclerView.setLayoutManager(layoutManager)
        val itemDecorator = VerticalSpacingItemDecorator(10)
        mRecyclerView.addItemDecoration(itemDecorator)
        var dataList : List<Video>
        var mediaObjects = ArrayList<Video>()
      /*  var video = Video(0,"Description for media object #1",
        "https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/Sending+Data+to+a+New+Activity+with+Intent+Extras.mp4",
        "https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/Sending+Data+to+a+New+Activity+with+Intent+Extras.png"
        )
        mediaObjects.add(video)
        var video1 = Video (0,"Description for media object #3",
        "https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/MVVM+and+LiveData+for+youtube.mp4",
        "https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/mvvm+and+livedata.png"
        )*/

        var video2= Video (0,"test","https://r4---sn-hpa7znsd.googlevideo.com/videoplayback?expire=1568750916&ei=4-iAXcSIObWXmLAP5o-XiAo&ip=129.45.44.198&id=o-APYKGa8Tyj1bzo9yTvM5YZ11CyfhWBEe9KH3pzxo5_lS&itag=22&source=youtube&requiressl=yes&mime=video%2Fmp4&ratebypass=yes&dur=245.783&lmt=1564951563624559&fvip=4&fexp=23842630&c=WEB&txp=2316222&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cmime%2Cratebypass%2Cdur%2Clmt&sig=ALgxI2wwRQIhAK9V8UngmU4swzb3xBhzMHqq-sqCabv7S5QeNdsA04DlAiAkg3L8BrDYP-_4tpaq15sq5u9hPXg3baSBPOJ8oKCTag%3D%3D&redirect_counter=1&cm2rm=sn-qwx11t-j1al7e&req_id=54d960d72bdba3ee&cms_redirect=yes&mm=29&mn=sn-hpa7znsd&ms=rdu&mt=1568729216&mv=m&mvi=3&pl=24&lsparams=mm,mn,ms,mv,mvi,pl&lsig=AHylml4wRAIgbYBSbjQLUIph5wDRr1-j6EAWc8UYGZQ7HFzhxAOUiy8CIG64_ZR0BXdrx7H5UNv307Iyg7Ndbmr4SB_8MSwy-17d","https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/mvvm+and+livedata.png")
       /* mediaObjects.add(video1)
        mediaObjects.add(video)*/
        mediaObjects.add(video2)
        doAsync {
            dataList=App.db.videoDao().getVideos()
            for (l in dataList )
            {
                Log.i("HEERE",dataList.size.toString())
                mediaObjects.add(l)
            }

             //data = App.db.videoDao().getVideos()
          /*  Log.i ("DATAA",data.toString())
            videosLiveData = LivePagedListBuilder(data!!, /* page size */ 20).build()*/

        }


       /* if (videosLiveData != null ) videosLiveData!!.observe(this, androidx.lifecycle.Observer{ pagedList ->

           Log.i("SIZEEE" ,pagedList.size.toString())

            var list =pagedList.toList()
            for (l in list )
            {
                mediaObjects.add(l)
            }

            Log.i("articles in main ", "ma list ${pagedList.size}")
        })  */


        mRecyclerView.setMediaObjects(mediaObjects)
        val adapter = VideoPlayerRecyclerAdapter(mediaObjects, initGlide())
        mRecyclerView.setAdapter(adapter)
    }

    private fun initGlide(): RequestManager {
        val options = RequestOptions()
            .placeholder(R.drawable.white_background)
            .error(R.drawable.white_background)

        return Glide.with(this)
            .setDefaultRequestOptions(options)
    }


    override fun onDestroy() {
        if (mRecyclerView != null)
            mRecyclerView.releasePlayer()
        super.onDestroy()
    }
}
