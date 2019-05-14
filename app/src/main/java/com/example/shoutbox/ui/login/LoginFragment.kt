package com.example.shoutbox.ui.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.shoutbox.R
import kotlinx.android.synthetic.main.login_fragment.*

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
}
