package com.prushh.mvvmshop.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prushh.mvvmshop.R
import com.prushh.mvvmshop.ShopApplication
import com.prushh.mvvmshop.models.auth.LoginResponse
import com.prushh.mvvmshop.models.shop.Product
import com.prushh.mvvmshop.models.shop.ShopResponse
import com.prushh.mvvmshop.repository.ShopRepository
import com.prushh.mvvmshop.utils.Constants.SHOP_100
import com.prushh.mvvmshop.utils.Constants.SHOP_250
import com.prushh.mvvmshop.utils.Constants.SHOP_500
import com.prushh.mvvmshop.utils.Network.hasInternetConnection
import com.prushh.mvvmshop.utils.Resource
import com.prushh.mvvmshop.utils.SharedPref
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

/**
 * ViewModel used by ShopActivity, it prepares data for the UI updates and provides
 * some shop operations.
 * @param app Current application.
 * @property shopRepository Repository for shop operations.
 */
class ShopViewModel(
    app: Application,
    private var shopRepository: ShopRepository
) : AndroidViewModel(app) {

    /**
     * Represent three type of operations, each type is like an object.
     */
    private enum class TypeOperation { ALL, ID, BUY }

    /**
     * Observable object for show API response in ShopFragment and ProductFragment.
     */
    val productShop: MutableLiveData<Resource<ShopResponse>> = MutableLiveData()

    /**
     * Contains the last API response received, working with productShop.
     */
    private var productShopResponse: ShopResponse? = null

    /**
     * Observable object for show API response in ArchiveFragment.
     */
    val productArchive: MutableLiveData<Resource<ShopResponse>> = MutableLiveData()

    /**
     * Contains the last API response received, working with productArchive.
     */
    private var productArchiveResponse: ShopResponse? = null

    /**
     * Observable object for show API response in ProfileFragment.
     */
    val userCoupon: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()

    /**
     * Contains the last API response received, working with userCoupon.
     */
    private var userCouponResponse: LoginResponse? = null

    /**
     * This method call safeProductShop with ALL type operation inside CoroutineScope.
     */
    fun getAllProducts() = viewModelScope.launch {
        safeProductShop(TypeOperation.ALL)
    }

    /**
     * This method call safeProductShop with ID type operation inside CoroutineScope.
     * @param productId Unique identifier for product.
     */
    fun getProductById(productId: Int) = viewModelScope.launch {
        safeProductShop(TypeOperation.ID, productId = productId)
    }

    /**
     * This method call safeProductShop with BUY type operation inside CoroutineScope.
     * @param userId Unique identifier for user.
     * @param productId Unique identifier for product.
     */
    fun buyProductById(userId: Int, productId: Int) = viewModelScope.launch {
        safeProductShop(TypeOperation.BUY, userId = userId, productId = productId)
    }

    /**
     * This is a suspend function, it checks the internet connection,
     * processes the request and sends a message to the client.
     * @param type Indicates the type of operation.
     * @param userId Optional unique identifier for user, used only for BUY operation.
     * @param productId Optional unique identifier for product, used for ID and BUY operations.
     */
    private suspend fun safeProductShop(type: TypeOperation, userId: Int = 0, productId: Int = 0) {
        productShop.postValue(Resource.Loading())
        try {
            val response: Response<ShopResponse>
            if (hasInternetConnection(getApplication())) {
                response = when (type) {
                    TypeOperation.ALL -> shopRepository.getAllProducts()
                    TypeOperation.ID -> shopRepository.getProductById(productId)
                    TypeOperation.BUY -> shopRepository.buyProductById(userId, productId)
                }
                val resource = handleProductShopResponse(response)
                productShop.postValue(resource)
            } else {
                productShop.postValue(Resource.Error(getStringResource(R.string.err_internet)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> productShop.postValue(Resource.Error(getStringResource(R.string.err_network)))
                else -> productShop.postValue(Resource.Error(getStringResource(R.string.err_conversion)))
            }
        }
    }

    /**
     * This method call safeArchive inside CoroutineScope.
     * @param userId Unique identifier for user.
     */
    fun getArchive(userId: Int) = viewModelScope.launch {
        safeArchive(userId)
    }

    /**
     * This is a suspend function, it checks the internet connection,
     * processes the request and sends a message to the client.
     * @param userId Unique identifier for the user.
     */
    private suspend fun safeArchive(userId: Int) {
        productArchive.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(getApplication())) {
                val response = shopRepository.getArchive(userId)
                val resource = handleProductArchiveResponse(response)

                productArchive.postValue(resource)
            } else {
                productArchive.postValue(Resource.Error(getStringResource(R.string.err_internet)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> productArchive.postValue(Resource.Error(getStringResource(R.string.err_network)))
                else -> productArchive.postValue(Resource.Error(getStringResource(R.string.err_conversion)))
            }
        }
    }

    /**
     * This method call safeAddCoupon inside CoroutineScope if coupon is valid.
     * @param userId Unique identifier for user.
     * @param coupon Value of giveaway credit.
     */
    fun addCoupon(userId: Int, coupon: String) = viewModelScope.launch {
        val giveaway = when (coupon) {
            SHOP_100 -> 100
            SHOP_250 -> 250
            SHOP_500 -> 500
            else -> 0
        }
        if (giveaway != 0) {
            safeAddCoupon(userId, giveaway)
        } else {
            userCoupon.postValue(Resource.Error(getStringResource(R.string.not_valid_coupon)))
        }
    }

    /**
     * This is a suspend function, it checks the internet connection,
     * processes the request and sends a message to the client.
     * @param userId Unique identifier for the user.
     * @param giveaway Value of giveaway credit.
     */
    private suspend fun safeAddCoupon(userId: Int, giveaway: Int) {
        userCoupon.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(getApplication())) {
                val response = shopRepository.addCoupon(userId, giveaway)
                val resource = handleUserCouponResponse(response)

                userCoupon.postValue(resource)
            } else {
                userCoupon.postValue(Resource.Error(getStringResource(R.string.err_internet)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> userCoupon.postValue(Resource.Error(getStringResource(R.string.err_network)))
                else -> userCoupon.postValue(Resource.Error(getStringResource(R.string.err_conversion)))
            }
        }
    }

    /**
     * Convert server API response for productShopResponse in Resource with
     * specified template and add newProduct to the old list.
     * @param response Server API response based on ShopResponse.
     * @return Resource<ShopResponse>: use Resource to format the answer.
     */
    private fun handleProductShopResponse(
        response: Response<ShopResponse>,
        all: Boolean = true
    ): Resource<ShopResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                productShopResponse = if (productShopResponse == null) {
                    resultResponse
                } else {
                    if (all) {
                        val oldProducts = productShopResponse?.products
                        val newProducts = resultResponse.products
                        oldProducts?.addAll(newProducts)
                    }
                    resultResponse
                }
                return Resource.Success(productShopResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    /**
     * Convert server API response for productArchiveResponse in Resource with
     * specified template and add newProduct to the old list.
     * @param response Server API response based on ShopResponse.
     * @return Resource<ShopResponse>: use Resource to format the answer.
     */
    private fun handleProductArchiveResponse(
        response: Response<ShopResponse>
    ): Resource<ShopResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                productArchiveResponse = if (productArchiveResponse == null) {
                    resultResponse
                } else {
                    val oldProducts = productArchiveResponse?.products
                    val newProducts = resultResponse.products
                    oldProducts?.addAll(newProducts)
                    resultResponse
                }
                return Resource.Success(productArchiveResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    /**
     * Convert server API response for userCouponResponse in Resource with
     * specified template.
     * @param response Server API response based on LoginResponse.
     * @return Resource<LoginResponse>: use Resource to format the answer.
     */
    private fun handleUserCouponResponse(
        response: Response<LoginResponse>
    ): Resource<LoginResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                userCouponResponse = resultResponse
                return Resource.Success(userCouponResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    /**
     * Add single product to local database.
     * @param product Specific product.
     * @see ShopRepository.upsert
     */
    fun saveProduct(product: Product) = viewModelScope.launch {
        shopRepository.upsert(product)
    }

    /**
     * Delete single product.
     * @param product Specific product.
     * @see ShopRepository.delete
     */
    fun deleteProduct(product: Product) = viewModelScope.launch {
        shopRepository.delete(product)
    }

    /**
     * Get all saved products stored in local database.
     * @see ShopRepository.getSavedProducts
     */
    fun getSavedProducts() = shopRepository.getSavedProducts()

    /**
     * Delete shared preferences credentials.
     * @param sharedPref Contain the credentials information.
     */
    fun logout(sharedPref: SharedPref) {
        sharedPref.clear()
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