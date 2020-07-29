package com.prushh.mvvmshop.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.prushh.mvvmshop.R
import com.prushh.mvvmshop.ui.ShopActivity
import com.prushh.mvvmshop.ui.viewmodels.ShopViewModel
import com.prushh.mvvmshop.utils.Resource
import com.prushh.mvvmshop.utils.SharedPref
import kotlinx.android.synthetic.main.fragment_product.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ProductFragment : Fragment(R.layout.fragment_product) {

    private lateinit var viewModel: ShopViewModel
    private val args: ProductFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ShopActivity).viewModel

        val product = args.product

        viewModel.productShop.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { shopResponse ->
                        // API getProductById return single product, get it.
                        if (shopResponse.products.size == 1) {
                            val responseProduct = shopResponse.products.first()
                            // API buyProductById return only id.
                            if (responseProduct.title != null) {
                                tvTitle.text = responseProduct.title
                                tvDescription.text = responseProduct.description
                                Glide.with(this).load(responseProduct.urlToImage)
                                    .into(ivProductImage)
                                tvCategory.text =
                                    resources.getString(R.string.category, responseProduct.category)
                                tvQuantity.text =
                                    resources.getString(R.string.quantity, responseProduct.quantity)
                                product.quantity = responseProduct.quantity
                                tvPrice.text =
                                    resources.getString(R.string.price, responseProduct.price)
                            } else {
                                viewModel.getProductById(product.id)
                            }
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
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        viewModel.getProductById(product.id)

        fabBuy.setOnClickListener {
            val sharedPref = activity?.applicationContext?.let { SharedPref(it) }
            val userId = sharedPref?.id!!
            val wallet = sharedPref.wallet

            if (product.quantity!! > 0) {
                if (userId != 0) {
                    if (wallet >= product.price!!) {

                        val newWallet = wallet - product.price.toFloat()
                        sharedPref.wallet = newWallet

                        viewModel.buyProductById(userId, product.id)
                        Snackbar.make(
                            view,
                            getString(R.string.snack_buy_ok, newWallet),
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        Snackbar.make(
                            view,
                            getString(R.string.snack_no_credit, wallet),
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                } else {
                    Snackbar.make(view, getString(R.string.error_occurred), Snackbar.LENGTH_SHORT)
                        .show()
                }
            } else {
                Snackbar.make(view, getString(R.string.snack_not_available), Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        fabFavorite.setOnClickListener {
            product.saveAt = getCurrentDate()
            viewModel.saveProduct(product)
            Snackbar.make(view, getString(R.string.snack_save_ok), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun hideProgressBar() {
        buyProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        buyProgressBar.visibility = View.VISIBLE
    }

    private fun getCurrentDate(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern(getString(R.string.date_pattern))
            current.format(formatter)
        } else {
            val current = Date()
            val sdf = SimpleDateFormat(getString(R.string.date_pattern), Locale.ITALY)
            sdf.format(current)
        }
    }

}