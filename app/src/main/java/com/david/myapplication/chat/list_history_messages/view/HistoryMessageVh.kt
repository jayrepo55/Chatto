package com.david.myapplication.chat.list_history_messages.view

import com.david.myapplication.chat.data_model.ChatRequest
import com.david.myapplication.register_login.user_model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.david.myapplication.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.history_data_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class HistoryMessageVh(val chatMessage: ChatRequest): Item<ViewHolder>(){
    var chatPartnerUser : User? = null

    override fun getLayout(): Int {
        return R.layout.history_data_layout
    }
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.latest_message_textView.text =  chatMessage.text

        val chatpart:String = if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
            chatMessage.toId
        }else{
            chatMessage.fromId
        }
        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatpart")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser = p0.getValue(User::class.java)
                chatPartnerUser?.username
                viewHolder.itemView.latest_username_textView.text = chatPartnerUser?.username
                val latestProfile = viewHolder.itemView.latest_profile_imageView
                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(latestProfile)
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
        viewHolder.itemView.latest_username_textView.text = chatMessage.text

        val date = SimpleDateFormat("MMM dd h:mm a").format(Date(chatMessage.timestamp * 1000))
        viewHolder.itemView.latest_timestamp_textView.text = date.toString()

    }
}