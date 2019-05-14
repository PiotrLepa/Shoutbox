package com.example.shoutbox.ui.shoutbox

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.shoutbox.R
import com.example.shoutbox.util.wrapper.Status
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.shoutbox_fragment.*
import org.kodein.di.Kodein
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

        setupRecyclerView()
        observeMessages()
    }

    private fun setupRecyclerView() {
        messagesAdapter = GroupAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = messagesAdapter
    }

    private fun observeMessages() {
        viewModel.messages.observe(viewLifecycleOwner, Observer {
            Timber.d("onActivityCreated: messages status: ${it.status}")
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    messagesAdapter.clear()
                    messagesAdapter.addAll(it.data!!.map { MessageItem(it) })
                }
                Status.ERROR -> Toast.makeText(context, "Error ${it.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
