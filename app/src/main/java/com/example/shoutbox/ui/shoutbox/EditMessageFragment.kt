package com.example.shoutbox.ui.shoutbox

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.example.shoutbox.R
import com.example.shoutbox.db.model.Message
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_edit_message.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import timber.log.Timber

class EditMessageFragment : BottomSheetDialogFragment(), KodeinAware {

    override val kodein by kodein()

    companion object {

        private const val CLICKED_ITEM_ARGS = "CLICKED_ITEM_ARGS"

        fun getInstance(selectedMessage: Message): EditMessageFragment {
            val instance =  EditMessageFragment()
            val bundle = Bundle().apply {
                putParcelable(CLICKED_ITEM_ARGS, selectedMessage)
            }
            instance.arguments = bundle
            return instance
        }
    }

    private val viewModelFactory: ShoutboxViewModelFactory by instance()
    private lateinit var viewModel: ShoutboxViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(ShoutboxViewModel::class.java)
        Timber.d("viewmodel: $viewModel")
        val selectedMessage: Message? = arguments?.getParcelable(CLICKED_ITEM_ARGS)
        selectedMessage?.let {
            setupOnSaveClickListener(it)
        }
    }

    private fun setupOnSaveClickListener(selectedMessage: Message) {
        saveButton.setOnClickListener {
            Timber.d("onViewCreated: save Button clicked")
            val newContent = newContentInput.text.toString()
            viewModel.onEditButtonClicked(newContent, selectedMessage)
            dismiss()
        }
    }
}