package com.prushh.mvvmshop.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.prushh.mvvmshop.R
import com.prushh.mvvmshop.ui.ShopActivity
import com.prushh.mvvmshop.ui.viewmodels.ShopViewModel
import com.prushh.mvvmshop.utils.Resource
import com.prushh.mvvmshop.utils.SharedPref
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    lateinit var viewModel: ShopViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ShopActivity).viewModel

        // If context is not null, create SharedPref object.
        val sharedPref = activity?.applicationContext?.let { SharedPref(it) }

        val user = sharedPref?.user!!
        val wallet = sharedPref.wallet
        updateFields(user, wallet)

        viewModel.userCoupon.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { shopResponse ->
                        val oldWallet = shopResponse.user?.wallet!!

                        if (
                            (shopResponse.message == getString(R.string.add_coupon_ok)) &&
                            (oldWallet != wallet)
                        ) {
                            // Update UI and SharedPref.
                            val sameUser = shopResponse.user.username
                            val newWallet = shopResponse.user.wallet

                            sharedPref.wallet = newWallet
                            updateFields(sameUser, newWallet)

                            Toast.makeText(
                                context,
                                getString(R.string.add_coupon_ok),
                                Toast.LENGTH_SHORT
                            ).show()
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
                    etAddCoupon.text?.clear()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        btnAddCoupon.setOnClickListener {
            val userId = sharedPref.id
            val coupon = etAddCoupon.text.toString()

            viewModel.addCoupon(userId, coupon)
        }
    }

    private fun updateFields(user: String, wallet: Float) {
        tvUsernameInfo.text = HtmlCompat.fromHtml(
            getString(R.string.username_bold, user),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        tvBalanceInfo.text = HtmlCompat.fromHtml(
            getString(R.string.balance_bold, wallet),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        etAddCoupon.clearFocus()
        etAddCoupon.text?.clear()
    }

    override fun onResume() {
        super.onResume()
        etAddCoupon.clearFocus()
    }

    private fun hideProgressBar() {
        couponProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        couponProgressBar.visibility = View.VISIBLE
    }
}