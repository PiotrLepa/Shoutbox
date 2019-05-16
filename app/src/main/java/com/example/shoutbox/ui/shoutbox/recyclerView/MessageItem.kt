package com.example.shoutbox.ui.shoutbox.recyclerView

import com.example.shoutbox.R
import com.example.shoutbox.db.MessageEntry
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.message_item.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class MessageItem(
    val message: MessageEntry
) : Item() {

    private val dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            userNameText.text = message.login
            contentText.text = message.content
            dateText.text = DateTime(message.date).toString(dateFormat)
        }
    }

    override fun getLayout() = R.layout.message_item
}