package com.prushh.mvvmshop.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prushh.mvvmshop.repository.LoginRepository

/**
 * This class provide to add params for LoginViewModel and is responsible to instantiate it.
 * @property app Application object.
 * @property loginRepository Repository that contain login operations.
 */
class LoginViewModelProviderFactory(
    val app: Application,
    private val loginRepository: LoginRepository
) : ViewModelProvider.Factory {

    /**
     * OnCreate for this class that accept template.
     * @param modelClass Class of specified type.
     * @return T: ViewModel conversion.
     */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LoginViewModel(app, loginRepository) as T
    }
}