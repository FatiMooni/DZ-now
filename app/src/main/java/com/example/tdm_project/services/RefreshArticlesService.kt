package com.example.tdm_project.services

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.tdm_project.services.App.Companion.context
import kotlin.math.max


class RefreshArticlesService : JobService(){

    companion object {
        var ignoreNextJob = false

        private const val TWO_HOURS = 900
        private const val JOB_ID = 0

        fun initAutoRefresh(context: Context) {

            val jobSchedulerService = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

            val time = max(300, TWO_HOURS)

            if (App.hasNetwork()!!) {
                val builder = JobInfo.Builder(JOB_ID, ComponentName(context, RefreshArticlesService::class.java))
                    .setPeriodic(time * 1000L)
                    .setPersisted(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)

                if (Build.VERSION.SDK_INT >= 26) {
                    builder.setRequiresBatteryNotLow(true)
                        .setRequiresStorageNotLow(true)
                }

                ignoreNextJob = true // We can't add a initial delay with JobScheduler, so let's do this little hack instead
                val result = jobSchedulerService.schedule(builder.build())
                if(result == JobScheduler.RESULT_SUCCESS){
                    Log.i("Job info", "Has launched successfully")
                } else if (result == JobScheduler.RESULT_FAILURE){
                    Log.w("Job info", "Has not been launched successfully")
                }
            } else {
                jobSchedulerService.cancel(JOB_ID)
            }
        }
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(p0: JobParameters?): Boolean {

        val handler = Handler(Looper.getMainLooper())
        handler.post {
            Toast.makeText(
                this@RefreshArticlesService.applicationContext,
                "My Awesome service toast...",
                Toast.LENGTH_SHORT
            ).show()
        }

        val intent =
            Intent(context, FetchArticlesService::class.java)
                .setAction(FetchArticlesService.FROM_AUTO_REFRESH)
        FetchArticlesService.enqueue(context, intent)

        Log.i("smthing", "message haha")
        Toast.makeText(context,"am running",Toast.LENGTH_LONG).show()
        jobFinished(p0, false)



        ignoreNextJob = false

        return true    }

}