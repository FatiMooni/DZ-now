package com.example.tdm_project.model

import com.example.tdm_project.R

data class Topic(
    var displayedTitle: Int,
    var title: String,
    var categoryId: String,
    var IconLink: Int
) {
    companion object {

        fun getTopics(categories: List<Category>): ArrayList<Topic> {
            val list = ArrayList<Topic>()
            categories.forEach {
                list.add(this.getTopic(it))
            }
            return list

        }

        fun getTopic(category: Category): Topic {
            val displayedTitle = searchTitle(category.title)
            val title = category.title
            val categoryId = category._id
            val iconLink = searchIcon(category.title)

            return Topic(displayedTitle, title, categoryId, iconLink)
        }

        private fun searchTitle(title: String): Int {
            return when (title) {
                "sport" -> R.string.sport
                "actualité" -> R.string.actualité
                "social" -> R.string.social
                "culture" -> R.string.culture
                "femme" -> R.string.femme
                "international" -> R.string.international
                "national" -> R.string.nat
                "santé" -> R.string.health
                else -> R.string.economics
            }
        }

        private fun searchIcon(title: String): Int {
            return when (title) {
                "sport" -> R.drawable.tech_icon
                "culture" -> R.drawable.art_icon

                else -> R.drawable.tech_icon
            }
        }
    }


}


