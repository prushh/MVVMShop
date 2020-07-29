package com.prushh.mvvmshop.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.prushh.mvvmshop.R
import com.prushh.mvvmshop.ui.LoginActivity
import com.prushh.mvvmshop.ui.ShopActivity
import com.prushh.mvvmshop.ui.viewmodels.LoginViewModel
import com.prushh.mvvmshop.utils.Resource
import com.prushh.mvvmshop.utils.SharedPref
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    lateinit var viewModel: LoginViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as LoginActivity).viewModel

        // If context is not null, create SharedPref object.
        val sharedPref = activity?.applicationContext?.let { SharedPref(it) }

        viewModel.userSignIn.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { shopResponse ->
                        if (shopResponse.message == getString(R.string.sign_in_ok)) {
                            // Store user information.
                            sharedPref?.apply {
                                id = shopResponse.user?.id!!
                                user = shopResponse.user.username
                                password = shopResponse.user.password
                                wallet = shopResponse.user.wallet
                            }

                            val intent = Intent(context, ShopActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(
                            activity,
                            resources.getString(R.string.error_occurred, message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    etPassword.text?.clear()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        btnSignIn.setOnClickListener {
            val user = etUsername.text.toString()
            val password = etPassword.text.toString()
            val email = etEmail.text.toString()
            viewModel.signIn(user, password, email)
        }
    }

    private fun hideProgressBar() {
        signInProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        signInProgressBar.visibility = View.VISIBLE
    }
}