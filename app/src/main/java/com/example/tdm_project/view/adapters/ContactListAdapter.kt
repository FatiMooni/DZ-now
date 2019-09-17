package com.example.tdm_project.view.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.example.tdm_project.R
import com.example.tdm_project.model.Contact
import java.util.*

class ContactListAdapter(val context: Context, val arrayList: ArrayList<Contact>) :
    RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {

    var callback: onContactClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val newsCard = LayoutInflater.from(context).inflate(R.layout.contact_details, parent, false)
        return ViewHolder(newsCard, callback!!)
    }

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = arrayList[position]
        holder.bind(contact)
    }

    fun getLetterDrawable(Title: String?): TextDrawable {
        val Name = Title.orEmpty()
        val rnd = Random()
        val color = Color.argb(
            255,
            rnd.nextInt(256),
            rnd.nextInt(256),
            rnd.nextInt(256)
        ) // The color is specific to the feedId (which shouldn't change)
        val lettersForName =
            if (Name.length < 2) Name.toUpperCase() else Name.substring(0, 2).trim().toUpperCase()
        return TextDrawable.builder().buildRect(lettersForName, color)

}


inner class ViewHolder(val rowView: View, val listener: onContactClick) :
    RecyclerView.ViewHolder(rowView) {


    fun bind(item: Contact) {
        val userName = rowView.findViewById<TextView>(R.id.contactName)
        val imageView = rowView.findViewById<AppCompatImageView>(R.id.contactimg)
        val userNumber = rowView.findViewById<TextView>(R.id.contactNumber)


        userName.text = item.name
        userNumber.text = item.number
        imageView.setImageDrawable(getLetterDrawable(item.name))

        rowView.setOnClickListener {
            listener.onContactClick(adapterPosition, item.number)
        }

    }

}

interface onContactClick {
    fun onContactClick(position: Int, number: String)
}

fun setContactListener(callback: onContactClick) {
    this.callback = callback
}
}