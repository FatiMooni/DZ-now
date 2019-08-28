package com.example.tdm_project.viewmodel

/**
 ** This is to get the list of categories either from the back end using
 ** retrofit or the room data base when it is offline or in a regular situation
 **/
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tdm_project.model.Category
import com.example.tdm_project.services.App
import com.example.tdm_project.services.CategoryService
import com.example.tdm_project.services.ServiceBuilder
import com.example.tdm_project.sharedPreferences.PreferencesProvider
import com.example.tdm_project.view.fragments.HomeFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel : ViewModel {

    //lists
    val pref = PreferencesProvider(App.context)
    private var categoryMList = MutableLiveData<ArrayList<Category>>()
    private var categoryInnerList = ArrayList<Category>()

    //parameteres
    var _id: String = ""
    var title: String = ""
    var feeds: Map<String, String> = HashMap()

    constructor(
        category: Category
    ) : super() {
        this._id = category._id
        this.title = category.title
        this.feeds = category.feeds
    }

    constructor() : super()

    //to observe my list
    fun getCategories(): MutableLiveData<ArrayList<Category>> {
        categoryMList.value = categoryInnerList
        return categoryMList
    }

    //retrieve data from back end
    fun getData() {
        //TODO("when to call retrofit server")
        //Typically when am online and either it is my first use
        // of the app or my user wants to refresh the categories

        if (App.isOnline ) {
            val service = ServiceBuilder.buildService(CategoryService::class.java)
            val request = service.getAllAvailbleCategoris()

            request.enqueue(object : Callback<List<Category>> {

                override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                    Log.e("article fetch", "couldnt get the articles", t)
                }

                override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                    if (response.isSuccessful) {
                        response.body()!!.forEach {
                            categoryInnerList.add(it)
                        }

                        categoryMList.value = categoryInnerList
                    }
                }

            })
        }

        //Else I just wants to get what i already have in my room database
        else {
            categoryInnerList.addAll(App.db.categoryDao().getAllCategories())
            categoryMList.postValue(categoryInnerList)

            //now what if I requested a refresh action or it was my first use but i was offline
            // ==> handle on main fragment .. here just for data !
        }
    }
}