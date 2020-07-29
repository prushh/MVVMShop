package com.prushh.mvvmshop.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.prushh.mvvmshop.R
import com.prushh.mvvmshop.db.ProductDatabase
import com.prushh.mvvmshop.repository.ShopRepository
import com.prushh.mvvmshop.ui.viewmodels.ShopViewModel
import com.prushh.mvvmshop.ui.viewmodels.ShopViewModelProviderFactory
import com.prushh.mvvmshop.utils.SharedPref
import kotlinx.android.synthetic.main.activity_shop.*
import kotlinx.android.synthetic.main.main_toolbar.*

/**
 * This class will contain some Fragment, it also initializes the view model and menu.
 */
class ShopActivity : AppCompatActivity() {

    /**
     * Represent view model, "late" for initialization.
     */
    lateinit var viewModel: ShopViewModel

    /**
     * Create ShopActivity instance, with some initialization.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        setSupportActionBar(toolbar)

        // Initialize repository, provider factory and view model.
        val shopRepository = ShopRepository(ProductDatabase(this))
        val viewModelProviderFactory = ShopViewModelProviderFactory(application, shopRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(ShopViewModel::class.java)

        // Navigation setup for bottomNavigationView through NavController
        bottomNavigationView.setupWithNavController(navShopFragment.findNavController())

        // Set visibility for bottomNavigationView based on fragment.
        navShopFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.shopFragment, R.id.savedFragment,
                    R.id.archiveFragment, R.id.profileFragment ->
                        bottomNavigationView.visibility = View.VISIBLE
                    else -> bottomNavigationView.visibility = View.GONE
                }
            }
    }

    /**
     * Inflate layout for options menu.
     * @param menu Menu component to update.
     * @return Boolean: is successful or not.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_more_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Specifies what to do when the item has been selected.
     * @param item Menu item selected.
     * @return Boolean: is successful or not.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.logoutItem -> {
            viewModel.logout(SharedPref(applicationContext))

            val intent = Intent(applicationContext, LoginActivity::class.java)
            // Set the start of a new task and clean the current activity.
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            true
        }
        R.id.balanceItem -> {
            val wallet = SharedPref(applicationContext).wallet
            Toast.makeText(
                applicationContext,
                getString(R.string.balance, wallet),
                Toast.LENGTH_SHORT
            ).show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}