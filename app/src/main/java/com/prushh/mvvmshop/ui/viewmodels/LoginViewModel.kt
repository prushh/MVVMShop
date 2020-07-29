package com.prushh.mvvmshop.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prushh.mvvmshop.R
import com.prushh.mvvmshop.ShopApplication
import com.prushh.mvvmshop.models.auth.LoginResponse
import com.prushh.mvvmshop.models.auth.User
import com.prushh.mvvmshop.models.auth.UserMinimal
import com.prushh.mvvmshop.repository.LoginRepository
import com.prushh.mvvmshop.utils.Constants.SHARED_PREF_DEFAULT_STR
import com.prushh.mvvmshop.utils.Network.hasInternetConnection
import com.prushh.mvvmshop.utils.Resource
import com.prushh.mvvmshop.utils.SharedPref
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

/**
 * ViewModel used by LoginActivity, it prepares data for the UI updates and provides
 * two login operations.
 * @param app Current application.
 * @property loginRepository Repository for login operations.
 */
class LoginViewModel(
    app: Application,
    private val loginRepository: LoginRepository
) : AndroidViewModel(app) {

    /**
     * Observable object for show API response in LoginFragment.
     */
    val userAuth: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()

    /**
     * Observable object for show API response in SignInFragment.
     */
    val userSignIn: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()

    /**
     * Contains the last API response received.
     */
    private var userAuthResponse: LoginResponse? = null

    /**
     * This method call safeLoginOperations inside CoroutineScope if user and password aren't empty.
     * @param user Username or email of the user.
     * @param password Password for login.
     */
    fun login(user: String, password: String) = viewModelScope.launch {
        if (user.isNotEmpty() && password.isNotEmpty()) {
            val payload = UserMinimal(user, password)
            safeLoginOperations(payload)
        } else {
            userAuth.postValue(Resource.Error(getStringResource(R.string.err_field_empty)))
        }
    }

    /**
     * This method call safeLoginOperations inside CoroutineScope if user and password
     * are stored in shared preferences.
     * @param sharedPref Contain the credentials information.
     */
    fun checkAuth(sharedPref: SharedPref) = viewModelScope.launch {
        val default = SHARED_PREF_DEFAULT_STR
        val user = sharedPref.user
        val password = sharedPref.password

        if (user != default && password != default) {
            val payload = UserMinimal(user, password)
            safeLoginOperations(payload, false)
        }
    }

    /**
     * This is a suspend function, it checks the internet connection,
     * processes the request and sends a message to the client.
     * @param payload Authentication credential.
     * @param isLogin Optional, represent the type of operation.
     */
    private suspend fun safeLoginOperations(payload: UserMinimal, isLogin: Boolean = true) {
        userAuth.postValue(Resource.Loading())
        try {
            val response: Response<LoginResponse>
            if (hasInternetConnection(getApplication())) {
                response = if (isLogin) {
                    loginRepository.login(payload)
                } else {
                    loginRepository.checkAuth(payload)
                }
                userAuth.postValue(handleLoginResponse(response))
            } else {
                userAuth.postValue(Resource.Error(getStringResource(R.string.err_internet)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> userAuth.postValue(Resource.Error(getStringResource(R.string.err_network)))
                else -> userAuth.postValue(Resource.Error(getStringResource(R.string.err_conversion)))
            }
        }
    }

    /**
     * This method call safeSignIn inside CoroutineScope if fields aren't empty.
     * @param username Username of the user.
     * @param password Password for login.
     * @param email Email for the user.
     */
    fun signIn(username: String, password: String, email: String) = viewModelScope.launch {
        if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                val user = User(0, username, password, email, 0f)
                safeSignIn(user)
            } else {
                userSignIn.postValue(Resource.Error(getStringResource(R.string.err_email)))
            }
        } else {
            userSignIn.postValue(Resource.Error(getStringResource(R.string.err_field_empty)))
        }
    }

    /**
     * This is a suspend function, it checks the internet connection,
     * processes the request and sends a message to the client.
     * @param user User information.
     */
    private suspend fun safeSignIn(user: User) {
        userSignIn.postValue(Resource.Loading())
        try {
            val response: Response<LoginResponse>
            if (hasInternetConnection(getApplication())) {
                response = loginRepository.signIn(user)
                userSignIn.postValue(handleLoginResponse(response))
            } else {
                userSignIn.postValue(Resource.Error(getStringResource(R.string.err_internet)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> userSignIn.postValue(Resource.Error(getStringResource(R.string.err_network)))
                else -> userSignIn.postValue(Resource.Error(getStringResource(R.string.err_conversion)))
            }
        }
    }

    /**
     * Convert server API response in Resource with specified template.
     * @param response Server API response based on LoginResponse.
     * @return Resource<LoginResponse>: use Resource to format the answer.
     */
    private fun handleLoginResponse(response: Response<LoginResponse>): Resource<LoginResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                userAuthResponse = resultResponse
                return Resource.Success(userAuthResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    /**
     * Get string resource by id.
     * @param stringId Unique identifier for string resource.
     * @return String: string resource.
     */
    private fun getStringResource(stringId: Int): String {
        return getApplication<ShopApplication>().getString(stringId)
    }
}