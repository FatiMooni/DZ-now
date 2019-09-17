package com.example.tdm_project.view.activities

import android.content.Context

import android.os.Build
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tdm_project.R
import com.example.tdm_project.authentication.LogInActivity
import com.example.tdm_project.services.App
import com.example.tdm_project.services.RefreshArticlesService
import com.example.tdm_project.sharedPreferences.MyContextWrapper
import com.example.tdm_project.sharedPreferences.PreferencesProvider
import com.example.tdm_project.view.fragments.HomeFragment
import com.example.tdm_project.view.fragments.ProfileFragment
import com.example.tdm_project.view.fragments.SavedFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.find
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.jetbrains.anko.doAsync
import java.util.*


class MainActivity : CustomBaseActivity() {
    lateinit var myPreference: PreferencesProvider
    val TAG = "TAG-MainActivity"
    lateinit var fragProfile : ProfileFragment
    var stringImageUri : String? = null
    var newPseudo : String ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setUpBottomNavigationBar()
        //pour lancer le jobservice
        RefreshArticlesService.initAutoRefresh(App.context)
        val intent = intent
        newPseudo = intent.getStringExtra("PSEUDO")
        stringImageUri = intent.getStringExtra("PHOTO")
        if (stringImageUri != null) Log.i("URI_MAIN",stringImageUri)
        else Log.i("URI_MAIN","NULLLL")
        fragProfile = ProfileFragment.newInstance(newPseudo, stringImageUri)

        doAsync{
            App.db.articleDao().deleteAllArticles(getDaysAgo(1).time)
        }


    }

    /** to get previous day **/
    fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)

        return calendar.time
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // ajouter le menu au top de l'activité

        menuInflater.inflate(R.menu.menu_top_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
       /* if(HomeFragment.verticallayout) find<RadioButton>(R.id.vert_disp).isChecked = true
        else find<RadioButton>(R.id.hori_disp).isChecked = true*/
        when(item!!.itemId){
            R.id.vert_disp -> HomeFragment.verticallayout = true
            R.id.hori_disp -> HomeFragment.verticallayout = false
            R.id.refresh_categorie -> HomeFragment.ACTION_REFRESH_CATEGORIES_FROM_BACK = true
            R.id.action_logout -> {
                signOut()
                Toast.makeText(this, "Sign out", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@MainActivity, LogInActivity::class.java))
                return super.onOptionsItemSelected(item)
            }
        }
        return true
    }


    // Sending the token to the fragement
    private fun setUpBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener{
            val fragment: Fragment
            when (it.itemId) {
                R.id.nav_home -> fragment = HomeFragment()
                R.id.nav_profile -> fragment = fragProfile
                R.id.nav_saved -> fragment = SavedFragment()
                else -> fragment = HomeFragment()
            }
            replaceFragment(fragment)
            return@setOnNavigationItemSelectedListener true

        }

        bottomNavigationView.selectedItemId = R.id.nav_home  /// consult .. just for the test
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }


    override fun attachBaseContext(newBase: Context?) {
        myPreference = PreferencesProvider(newBase!!)
        val lang = myPreference.getLoginCount()
        super.attachBaseContext(MyContextWrapper.wrap(newBase,lang))
    }


    private fun signOut() {
        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, GraphRequest.Callback {
                AccessToken.setCurrentAccessToken(null)
                LoginManager.getInstance().logOut()

                finish()
            }).executeAsync()
        }

    }
}
