package com.example.tdm_project.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.example.tdm_project.R
import com.example.tdm_project.model.Article
import com.example.tdm_project.model.data.SharedSavedNews
import com.example.tdm_project.view.activities.FavoriteActivity
import com.example.tdm_project.view.activities.ParameterActivity
import com.example.tdm_project.view.adapters.sharedPostsAdapter
import com.example.tdm_project.viewmodel.ArticleViewModel
import com.facebook.Profile
import com.facebook.login.LoginManager


class ProfileFragment : Fragment() {
    lateinit var rootView: View
    lateinit var sharedAdapter: sharedPostsAdapter
    //lateinit var rv: RecyclerView
    var newsList = ArrayList<ArticleViewModel>()

    companion object {
        private const val ARG_PSEUDO = "PSEUDO"
        private const val ARG_URI = "PHOTO"
        fun getInstance() = ProfileFragment()


        fun newInstance(pseudo: String?, uri: String?): ProfileFragment {
            val fragment = ProfileFragment()

            val bundle = Bundle().apply {
                putString(ARG_PSEUDO, pseudo)
                putString(ARG_URI, uri)
            }

            fragment.arguments = bundle

            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(
            R.layout.profile_fragment,
            container, false
        )
        val pseudoText = rootView.findViewById<TextView>(R.id.profile_pseudo)
        val profileView = rootView.findViewById<ImageView>(R.id.profile_photo)
        val profile= Profile.getCurrentProfile()
       var pictureUri= profile.getProfilePictureUri(127,127)
        Log.i("URIFACEBOOK",pictureUri.toString())
        val imageUrl = GlideUrl(pictureUri.toString(), LazyHeaders.Builder()
            .build())
        Glide.with(this@ProfileFragment).load(imageUrl).into(profileView)

        pseudoText.text  = profile.name.toString()
        newsList = SharedSavedNews.getListSharedPosts()
        // not used for now intialiserVertically()

        rootView.findViewById<Button>(R.id.param).setOnClickListener {
            // preparé l'activité d'ajout
            val intent = Intent(rootView.context, ParameterActivity::class.java)
            // lancer l'activité
            startActivity(intent)

        }

        rootView.findViewById<Button>(R.id.btn_fav).setOnClickListener {
            // preparé l'activité d'ajout
            val intent = Intent(rootView.context, FavoriteActivity::class.java)
            // lancer l'activité
            startActivity(intent)

        }

        val newPseudo = arguments?.getString("PSEUDO")
        val stringImageUri = arguments?.getString("PHOTO")

        if (newPseudo != null) pseudoText.text = newPseudo
        if (stringImageUri != null) {
            val imageUri = Uri.parse(stringImageUri)
            profileView.setImageURI(imageUri)
        }

        if (stringImageUri != null) Log.i("URI_PROFILE", stringImageUri)
        else Log.i("URI_PROFILE", "NULLLL")

        return rootView
    }


   /** Not used for now  **/
   /*private fun intialiserVertically() {
        rv = rootView.findViewById(R.id.recyler_view_sahred_post)
        val layout = androidx.recyclerview.widget.LinearLayoutManager(rootView.context)
        layout.orientation = RecyclerView.VERTICAL
        rv.layoutManager = layout
        sharedAdapter = sharedPostsAdapter(rootView.context,newsList)
        rv.adapter = sharedAdapter
    }*/



}