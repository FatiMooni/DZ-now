package com.example.tdm_project.view.activities
import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.Menu
import com.example.tdm_project.R

import com.example.tdm_project.sharedPreferences.MyContextWrapper
import com.example.tdm_project.sharedPreferences.PreferencesProvider
import com.example.tdm_project.view.fragments.HomeFragment
import com.example.tdm_project.view.fragments.ProfileFragment
import com.example.tdm_project.view.fragments.SavedFragment


class MainActivity : CustomBaseActivity() {
    lateinit var myPreference: PreferencesProvider
    val TAG = "TAG-MainActivity"
    lateinit var fragProfile : ProfileFragment
    var stringImageUri : String? = null
    var newPseudo : String ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpBottomNavigationBar()
        val intent = intent
        newPseudo = intent.getStringExtra("PSEUDO")
        stringImageUri = intent.getStringExtra("PHOTO")
        if (stringImageUri != null) Log.i("URI_MAIN",stringImageUri)
        else Log.i("URI_MAIN","NULLLL")
        fragProfile = ProfileFragment.newInstance(newPseudo, stringImageUri)

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

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

}
