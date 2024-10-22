package com.efkan.chatapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class UserAdapter(
    val context: Context,
    val userList: ArrayList<User>,
    private val firebaseStorage: FirebaseStorage // firebaseStorage'ı ekleyin
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName = itemView.findViewById<TextView>(R.id.txt_namerow)
        val userImage = itemView.findViewById<ImageView>(R.id.rowuserImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.recycler_rowuser, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textName.text = currentUser.name
        val newReference = firebaseStorage.getReference(currentUser.uid!!)

        newReference.downloadUrl.addOnSuccessListener { uri ->
            val downloadUrl = uri.toString()
            Picasso.get().load(downloadUrl).into(holder.userImage)
            println("URL: $downloadUrl")
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)
            holder.itemView.context.startActivity(intent)
        }
    }
}
