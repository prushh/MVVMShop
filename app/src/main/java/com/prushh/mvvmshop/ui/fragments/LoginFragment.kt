package com.prushh.mvvmshop.ui.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.prushh.mvvmshop.R
import com.prushh.mvvmshop.ui.LoginActivity
import com.prushh.mvvmshop.ui.ShopActivity
import com.prushh.mvvmshop.ui.viewmodels.LoginViewModel
import com.prushh.mvvmshop.utils.Resource
import com.prushh.mvvmshop.utils.SharedPref
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var viewModel: LoginViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as LoginActivity).viewModel

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (findNavController().currentDestination?.id == R.id.loginFragment) {
                    val exit =
                        { _: DialogInterface, _: Int -> (activity as LoginActivity).finish() }
                    val builder = context?.let { AlertDialog.Builder(it) } ?: return
                    builder.apply {
                        setCancelable(false)
                        setTitle(resources.getString(R.string.title_exit_dialog))
                        setMessage(resources.getString(R.string.message_exit_dialog))
                        setIcon(resources.getDrawable(R.drawable.ic_error, null))

                        setPositiveButton(resources.getString(R.string.yes), exit)
                        setNegativeButton(resources.getString(R.string.no), null)

                        create()
                        show()
                    }
                } else {
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        // If context is not null, create SharedPref object.
        val sharedPref = activity?.applicationContext?.let { SharedPref(it) }

        viewModel.userAuth.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { shopResponse ->
                        if (
                            (shopResponse.message == getString(R.string.check_ok)) ||
                            (shopResponse.message == getString(R.string.auth_ok))
                        ) {
                            if (shopResponse.message == getString(R.string.auth_ok)) {
                                // Update user information only on first login.
                                sharedPref?.apply {
                                    id = shopResponse.user?.id!!
                                    user = shopResponse.user.username
                                    password = shopResponse.user.password
                                }
                            }

                            // Update wallet every time after login or checkAuth.
                            sharedPref?.apply {
                                wallet = shopResponse.user?.wallet!!
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
                    sharedPref?.clear()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        if (sharedPref != null) {
            viewModel.checkAuth(sharedPref)
        }

        btnLogin.setOnClickListener {
            val user = etUsername.text.toString()
            val password = etPassword.text.toString()
            viewModel.login(user, password)
        }

        tvSignIn.setOnClickListener {
            findNavController().navigate(R.id.signInFragment)
        }
    }

    private fun hideProgressBar() {
        loginProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        loginProgressBar.visibility = View.VISIBLE
    }
}