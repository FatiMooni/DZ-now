package com.example.tdm_project.authentication

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.tdm_project.R
import com.example.tdm_project.model.data.UserPost
import com.example.tdm_project.services.ArticleService
import com.example.tdm_project.services.ServiceBuilder
import com.example.tdm_project.view.activities.MainActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.Profile
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.util.*


class LogInActivity : AppCompatActivity() {
    val TAG = "LOGIN_ACTIVITY"
    val RC_SIGN_IN = 9001
    val callbackManager = CallbackManager.Factory.create()
    val loginManager = LoginManager.getInstance()
   lateinit var facebookSignInButton :Button

    val api = ServiceBuilder.buildService(ArticleService::class.java)
    var profile : Profile? = null
    lateinit var user : UserPost
  /*  lateinit var googleSignInButton: SignInButton
    var googleApiClient: GoogleApiClient? = null
    var googleSignInOptions : GoogleSignInOptions? = null*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)


        facebookSignInButton = findViewById(R.id.login_button)


        facebookSignInButton.setOnClickListener(View.OnClickListener {
            // Login
            Log.i(TAG,"CLICKED")
            loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile"))
            Log.i(TAG,"Permissions")
            loginManager.registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        Log.d(TAG, "Facebook token: " + loginResult.accessToken.token)

                        /***Saving User in data base**/
                        profile = Profile.getCurrentProfile()
                        if( profile != null ) {
                            user = UserPost(profile!!.id)
                            saveUser(user)
                        }
                        /****/

                       startActivity(Intent(applicationContext,MainActivity::class.java))
                    }

                    override fun onCancel() {
                        Log.i(TAG, "Facebook onCancel.")

                    }

                    override fun onError(error: FacebookException) {
                        Log.d(TAG, "Facebook onError." + error.message )

                    }
                })
        })




    }
    fun saveUser ( user :UserPost )
    {

        val call0 = api.saveUser(user)
        call0.enqueue(object : Callback<UserPost> {
            override fun onResponse(call: Call<UserPost>, response: Response<UserPost>) {
                Log.i(TAG, "Saving User : call enqueue")

                if (!response.isSuccessful()) {
                    Log.i(TAG,"SAVED")
                    Log.i(TAG, "CODE:" + response.code().toString())
                    return
                }


            }
            override fun onFailure(call: Call<UserPost>, t: Throwable) {
                Log.i(TAG, "error CODE:" + t.message)
            }
        })

    }

    /* private fun handleFacebookAccessToken(token: AccessToken) {
         Log.d(TAG, "handleFacebookAccessToken:" + token)
         val accessToken = AccessToken.getCurrentAccessToken()
         val isLoggedIn = accessToken != null && !accessToken.isExpired

                 if (isLoggedIn) {
                     // Sign in success, update UI with the signed-in user's information
                     Log.d(TAG, "SUCCESS")
                    /* val user = firebaseAuth!!.currentUser
                     startActivity(Intent(this@CreateAccountActivity, MainActivity::class.java))*/
                 } else {
                     // If sign in fails, display a message to the user.
                     Log.d(TAG, "FAAAILED")
                     Toast.makeText(this@LogInActivity, "Authentication failed.",
                         Toast.LENGTH_SHORT).show()
                 }
             }*/


   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       callbackManager!!.onActivityResult(requestCode, resultCode, data)
       super.onActivityResult(requestCode, resultCode, data)

       Log.i(TAG,"RESULT"+resultCode.toString())
       Log.i(TAG,"REQUEST"+requestCode.toString())


   }

    /*  For the HaShKey

         val packageName = this.getApplicationContext().getPackageName()

           val info : PackageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
           for ( signature in info.signatures) {
               var md = MessageDigest.getInstance("SHA")
               md.update(signature.toByteArray())
               val hashKey = android.util.Base64.encodeToString(md.digest(), 0)
               Log.i(TAG,"HADHKEEEY"+hashKey)
           }
           Log.i(TAG,"NAAAMEE"+packageName) */


    }

