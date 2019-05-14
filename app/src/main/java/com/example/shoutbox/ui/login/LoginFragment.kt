package com.example.shoutbox.ui.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.shoutbox.R
import kotlinx.android.synthetic.main.login_fragment.*
import android.app.Activity
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.drawerlayout.widget.DrawerLayout
import com.example.shoutbox.MainActivity


class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        setupViews()

        viewModel.isUserSaved.observe(viewLifecycleOwner, Observer {
            if (it) {
                hideKeyboard()
                openShoutboxFragment()
            }
        })
    }

    private fun setupViews() {
        loginButton.setOnClickListener {
            val userName = userNameInput.text.toString()
            viewModel.onLoginButtonClicked(userName)
        }
    }

    private fun openShoutboxFragment() {
        val action = LoginFragmentDirections.actionStartShoutboxFragment()
        findNavController().navigate(action)
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity?.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
