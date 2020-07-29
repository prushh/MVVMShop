package com.prushh.mvvmshop.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.prushh.mvvmshop.R
import com.prushh.mvvmshop.repository.LoginRepository
import com.prushh.mvvmshop.ui.viewmodels.LoginViewModel
import com.prushh.mvvmshop.ui.viewmodels.LoginViewModelProviderFactory
import kotlinx.android.synthetic.main.main_toolbar.*

/**
 * This class will contain the LoginFragment and initialize the view model.
 */
class LoginActivity : AppCompatActivity() {

    /**
     * Represent view model, "late" for initialization.
     */
    lateinit var viewModel: LoginViewModel

    /**
     * Create LoginActivity instance, with some initialization.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)

        // Initialize repository, provider factory and view model.
        val loginRepository = LoginRepository()
        val viewModelProviderFactory = LoginViewModelProviderFactory(application, loginRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(LoginViewModel::class.java)
    }
}