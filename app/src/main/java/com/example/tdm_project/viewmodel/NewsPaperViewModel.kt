package com.example.tdm_project.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tdm_project.R

class NewsPaperViewModel : ViewModel {
    //lists
    private var newspaperMList = MutableLiveData<ArrayList<NewsPaperViewModel>>()
    private var newspaperInnerList = ArrayList<NewsPaperViewModel>()

    //attr
    var img : String = ""
    var title : String = ""
    var isPrefered : Boolean = false

    //primary
    constructor() : super()

    //secondary
    constructor(img: String, title: String, isPrefered: Boolean) : super() {
        this.img = img
        this.title = title
        this.isPrefered = isPrefered
    }


    fun getNewpapers(): MutableLiveData<ArrayList<NewsPaperViewModel>> {

        newspaperMList.value = newspaperInnerList

        return newspaperMList
    }

    fun loadNewsPaper(){

        newspaperInnerList.add(NewsPaperViewModel("https://upload.wikimedia.org/wikipedia/fr/1/18/Logo_ennahar.jpg" , "Ennahar",false))
        newspaperInnerList.add(NewsPaperViewModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS84Qurn9PNGPLEo13c2DF6RCnp7MP1CnN4scwi8s0pCjYzlNKd" , "El Khabar",false))
        newspaperInnerList.add(NewsPaperViewModel("http://bourse-dz.com/wp-content/uploads/2018/01/Logo_echorouk.jpg" , "Echorouk",false))
        newspaperInnerList.add(NewsPaperViewModel("https://i0.wp.com/elkhadra.com/fr/wp-content/uploads/2014/05/10338752_713741558681864_2346465047325620907_n.jpg" , "El Haddef",false))

        newspaperMList.value = newspaperInnerList
    }

}