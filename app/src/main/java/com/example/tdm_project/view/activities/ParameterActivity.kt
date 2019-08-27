package com.example.tdm_project.view.activities


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.appcompat.widget.LinearLayoutCompat
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.tdm_project.R
import com.example.tdm_project.model.Topic
import com.example.tdm_project.model.getTopics
import com.example.tdm_project.sharedPreferences.MyContextWrapper
import com.example.tdm_project.sharedPreferences.PreferencesProvider
import kotlinx.android.synthetic.main.parameters.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat


class ParameterActivity : CustomBaseActivity() {
    lateinit var myPreference: PreferencesProvider
    val TAKE_PICTURE = 1
    val SELECT_PICTURE = 2
    lateinit var modeSwitch: Switch
    lateinit var pref: PreferencesProvider
    lateinit var btnEditPhoto : Button
    lateinit var btnChange : Button
    lateinit var btnEditPseudo : Button
    var currentPath : String? = null
    var pseudo : String? = null
    var topics : ArrayList<Topic>? = null



    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //get the view
        setContentView(R.layout.parameters)

        //for the shared preferences
        pref = PreferencesProvider(this)
        setContentView(R.layout.parameters)
        btnEditPhoto = this.findViewById<View>(R.id.btn_edit_pic) as Button
        modeSwitch = this.findViewById(R.id.mode_switcher)
        btnEditPseudo = this.findViewById(R.id.btn_edit_pseudo)

        //just to be current to the shared preferences theme
        if (pref.load() == "dark") {
            modeSwitch.isChecked = true
        }
        //the switch button actions : change the theme
        modeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                //if it is switched then it is the dark mode
                pref.setDarkModeState("dark")
                recreate()

            }
            else {

                //if it is not pressed then it is the light mode
                pref.setDarkModeState("light")
                recreate()
            }
        }

        Log.i("here", modeSwitch.id.toString())

//Languages :


        btnChange= this.findViewById<Button>(R.id.btn_changeLang)
        btnChange.setOnClickListener {
            showChangeLanguageDialog()
        }
//Photo Edition

        btnEditPhoto.setOnClickListener{
            val inflater:LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            // Inflate a custom view using layout inflater
            val view = inflater.inflate(R.layout.pop_up_photo_view,null)

            // Initialize a new instance of popup window
            val popupWindow = PopupWindow(
                view, // Custom view to show in popup window
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
                LinearLayout.LayoutParams.WRAP_CONTENT // Window height
            )

            // Set an elevation for the popup window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.elevation = 10.0F
            }


            // If API level 23 or higher then execute the code
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // Create a new slide animation for popup window enter transition
                val slideIn = Slide()
                slideIn.slideEdge = Gravity.TOP
                popupWindow.enterTransition = slideIn

                // Slide animation for popup window exit transition
                val slideOut = Slide()
                slideOut.slideEdge = Gravity.RIGHT
                popupWindow.exitTransition = slideOut
                popupWindow.setFocusable(true)
                popupWindow.update()
            }
            // popupWindow.setBackgroundDrawable(BitmapDrawable())

            // Get the widgets reference from custom view
            var btnGallery = view.findViewById<Button>(R.id.btn_gallery)
            var btnCamera = view.findViewById<Button>(R.id.btn_camera)

               btnGallery.setOnClickListener {
                   Toast.makeText(this,"GALLERY",Toast.LENGTH_SHORT).show()
                  DispatchGalleryIntent ()

               }
            btnCamera.setOnClickListener {
             Toast.makeText(this,"CAMERA",Toast.LENGTH_SHORT).show()
                DispatchCameraIntent ()


            }


            // Finally, show the popup window on app
            TransitionManager.beginDelayedTransition(root_layout)
            popupWindow.showAtLocation(
                root_layout, // Location to display popup window
                Gravity.CENTER_HORIZONTAL, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
            )
        }



     // PopUp Editing Pseudo
        btnEditPseudo.setOnClickListener{
            val inflater:LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            // Inflate a custom view using layout inflater
            val view = inflater.inflate(R.layout.pop_up_pseudo,null)

            // Initialize a new instance of popup window
            val popupWindow = PopupWindow(
                view, // Custom view to show in popup window
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
                LinearLayout.LayoutParams.WRAP_CONTENT // Window height
            )

            // Set an elevation for the popup window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.elevation = 10.0F
            }


            // If API level 23 or higher then execute the code
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // Create a new slide animation for popup window enter transition
                val slideIn = Slide()
                slideIn.slideEdge = Gravity.TOP
                popupWindow.enterTransition = slideIn

                // Slide animation for popup window exit transition
                val slideOut = Slide()
                slideOut.slideEdge = Gravity.RIGHT
                popupWindow.exitTransition = slideOut
                popupWindow.setFocusable(true)
                popupWindow.update()
            }


            // Get the widgets reference from custom view
            var newPseudo = view.findViewById<EditText>(R.id.new_pseudo)
            var confirmBtn = view.findViewById<Button>(R.id.btn_confirm)


            confirmBtn.setOnClickListener {
                popupWindow.dismiss()
                pseudo= newPseudo.text.toString()
                Toast.makeText(this,pseudo,Toast.LENGTH_SHORT).show()
                val pseudoIntent = Intent (this, MainActivity::class.java)
                pseudoIntent.putExtra("PSEUDO",pseudo)
                startActivity(pseudoIntent)
            }


            // Finally, show the popup window on app
            TransitionManager.beginDelayedTransition(root_layout)
            popupWindow.showAtLocation(
                root_layout, // Location to display popup window
                Gravity.CENTER_HORIZONTAL, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
            )
        }

        //set topics list
        topics = pref.loadTopicsList()
        initializeTopicsList()


    }

  private fun initializeTopicsList(){
      val list = getTopics()
      var layout = findViewById<LinearLayoutCompat>(R.id.topics_choice_holder)
      Toast.makeText(this,layout.toString(),Toast.LENGTH_LONG).show()
     list.forEach {
          val check = CheckBox(this)
          check.text = this.getString(it.displayedTitle)

          if (topics!!.contains(it)){
              check.isChecked = true
          }
          check.setOnClickListener { v ->
              val checked = (v as CheckBox).isChecked
              if (checked) {
                  topics!!.add(it)
                  pref.setTopicsList(topics!!)
              } else {
                  topics!!.remove(it)
                  pref.setTopicsList(topics!!)

              }
          }
         layout.addView(check)
      }
  }

    /////Language

    private fun showChangeLanguageDialog () {

        var languagesList= arrayOf("Français","العربية")
        val lang = pref.getLoginCount()
        val index = languagesList.indexOf(lang)
        var mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle(this.getString(R.string.chang_lang))
        mBuilder.setSingleChoiceItems( languagesList,index) { dialog , i: Int ->

            if (i==0) {
                pref.setLoginCount("fr")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            else
            {
                pref.setLoginCount("ar")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

            dialog.dismiss()

        }

        mBuilder.setNeutralButton(this.getString(R.string.cancel)) { dialog, _: Int ->
            dialog.dismiss()

        }

        var mDialog = mBuilder.create()
        mDialog.show()


    }







    //Changing Profile Picture

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var uri : Uri?= null
    if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK )
        { try
        {
          val file = File(currentPath)
           uri = Uri.fromFile(file)
        }catch ( e: IOException)
        {
            e.printStackTrace()
        }

        }

        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK )
        { try
        {
            uri = data!!.data
        }catch ( e: IOException)
        {
            e.printStackTrace()
        }

        }


        var stringUri = uri.toString()
        val pictureIntent = Intent (this, MainActivity::class.java)
        pictureIntent.putExtra("PHOTO",stringUri)
        startActivity(pictureIntent)


    }
    private fun DispatchGalleryIntent()
    {
      val intent = Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"selectionner une image") , SELECT_PICTURE)
    }
    private fun DispatchCameraIntent() {
   val intent = Intent (MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null)
        {
            var photoFile: File? = null
            try
            {
            photoFile = createImage()

            }catch (e : IOException )
            {
                e.printStackTrace()
            }
            if ( photoFile != null )
            {
                var photoUri = FileProvider.getUriForFile(this ,"com.example.tdm_project.fileProvider"
                    ,photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(intent,TAKE_PICTURE)
            }

        }
    }

    fun createImage () : File
    {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss")
        val imageName = "JPEG_"+timeStamp+"_"
        var storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var image = File.createTempFile(imageName,".jpg",storageDir)
        currentPath = image.absolutePath
        return image
    }

    override fun attachBaseContext(newBase: Context?) {
        myPreference = PreferencesProvider(newBase!!)
        val lang = myPreference.getLoginCount()
        super.attachBaseContext(MyContextWrapper.wrap(newBase,lang))
    }






}