package com.prushh.mvvmshop.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prushh.mvvmshop.repository.ShopRepository

/**
 * This class provide to add params for ShopViewModel and is responsible to instantiate it.
 * @property app Application object.
 * @property shopRepository Repository that contain shop operations.
 */
class ShopViewModelProviderFactory(
    val app: Application,
    private val shopRepository: ShopRepository
) : ViewModelProvider.Factory {

    /**
     * OnCreate for this class that accept template.
     * @param modelClass Class of specified type.
     * @return T: ViewModel conversion.
     */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ShopViewModel(app, shopRepository) as T
    }
}