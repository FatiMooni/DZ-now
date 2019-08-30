package com.example.tdm_project.view.activities

import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.example.tdm_project.authentication.LogInActivity
import com.facebook.AccessToken


class EmptyMainActivity : AppCompatActivity() {
        override fun onCreate(@Nullable savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val activityIntent: Intent
            // go straight to main if a token is stored
            if (AccessToken.getCurrentAccessToken() != null) {
                activityIntent = Intent(this, MainActivity::class.java)
            } else {
                activityIntent = Intent(this, LogInActivity::class.java)
            }
            startActivity(activityIntent)
            finish()
        }
    }
