package com.example.shoutbox.ui.shoutbox

import android.content.Context
import android.graphics.Canvas
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.shoutbox.R
import com.example.shoutbox.util.USER_NAME_SHARED_PREF
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

    private val viewModelFactory: ShoutboxViewModelFactory by instance()
    private lateinit var viewModel: ShoutboxViewModel

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
        setHasOptionsMenu(true)
        setupWidgets()
        observeMessages()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_shoutbox_menu, menu)
    }

    private fun setupWidgets() {
        sendButton.setOnClickListener {
            val message = messageInput.text.toString()
            val userName = getUserName()
            messageInput.apply {
                clearFocus()
                hideKeyboard()
                setText("")
            }
            viewModel.onSendButtonClicked(message, userName)
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        messagesAdapter = GroupAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = messagesAdapter
        setupEditAndDeleteButtons()
    }

    private fun setupEditAndDeleteButtons() {
        val swipeController = SwipeController(object : SwipeControllerActions() {
            override fun onLeftClicked(position: Int) {
                val clickedItem = messagesAdapter.getItem(position) as MessageItem
                viewModel.onEditButtonClicked(clickedItem)
            }
            override fun onRightClicked(position: Int) {
                val clickedItem = messagesAdapter.getItem(position) as MessageItem
                viewModel.onDeleteButtonClicked(clickedItem)
            }
        })
        ItemTouchHelper(swipeController).attachToRecyclerView(recyclerView)

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

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
