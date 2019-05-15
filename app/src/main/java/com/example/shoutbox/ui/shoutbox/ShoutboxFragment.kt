package com.example.shoutbox.ui.shoutbox

import android.content.Context
import android.graphics.Canvas
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.shoutbox.R
import com.example.shoutbox.USER_NAME_SHARED_PREF
import com.example.shoutbox.db.MessageInput
import com.example.shoutbox.ui.shoutbox.recyclerView.MessageItem
import com.example.shoutbox.ui.shoutbox.recyclerView.SwipeController
import com.example.shoutbox.ui.shoutbox.recyclerView.SwipeControllerActions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.shoutbox_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import timber.log.Timber

class ShoutboxFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private lateinit var viewModel: ShoutboxViewModel
    private val viewModelFactory: ShoutboxViewModelFactory by instance()

    private lateinit var messagesAdapter: GroupAdapter<ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shoutbox_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShoutboxViewModel::class.java)

        setupWidgets()
        setupRecyclerView()
        observeMessages()
    }

    private fun setupWidgets() {
        sendButton.setOnClickListener {
            val message = messageInput.text.toString()
            val userName = getUserName()
            if (message != "" && userName != null) {
                viewModel.sendMessage(MessageInput(message, userName))
            }
        }
    }

    private fun setupRecyclerView() {
        messagesAdapter = GroupAdapter()
        val swipeController = SwipeController(object : SwipeControllerActions() {
            override fun onLeftClicked(position: Int) {
                val item = messagesAdapter.getItem(position) as MessageItem
                val message = item.message
                val messageInput = MessageInput("changed", message.login)
                viewModel.updateMessage(messageInput, message.id)
            }

            override fun onRightClicked(position: Int) {
                val item = messagesAdapter.getItem(position) as MessageItem
                val message = item.message
                viewModel.deleteMessage(message.id)
            }
        })
        ItemTouchHelper(swipeController).attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = messagesAdapter
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })
    }

    private fun observeMessages() {
        viewModel.messages.observe(viewLifecycleOwner, Observer {
            Timber.d("onActivityCreated: messages size: ${it}")

            if (it.isNotEmpty()) {
                messagesAdapter.clear()
                messagesAdapter.addAll(it.map { MessageItem(it) })
                recyclerView.smoothScrollToPosition(messagesAdapter.itemCount - 1)
            }
        })
    }

    private fun getUserName(): String? {
        val sharedPref = context?.getSharedPreferences(USER_NAME_SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPref?.getString(USER_NAME_SHARED_PREF, null)
    }
}
