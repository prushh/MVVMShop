package com.prushh.mvvmshop.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.prushh.mvvmshop.R
import com.prushh.mvvmshop.adapters.ProductAdapter
import com.prushh.mvvmshop.ui.ShopActivity
import com.prushh.mvvmshop.ui.viewmodels.ShopViewModel
import com.prushh.mvvmshop.utils.Resource
import kotlinx.android.synthetic.main.fragment_shop.*

class ShopFragment : Fragment(R.layout.fragment_shop) {

    private lateinit var viewModel: ShopViewModel
    private lateinit var productAdapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ShopActivity).viewModel
        setupRecyclerView()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (findNavController().currentDestination?.id == R.id.shopFragment) {
                    val exit =
                        { _: DialogInterface, _: Int -> (activity as ShopActivity).finish() }
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

        productAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable(getString(R.string.arg), it)
            }
            findNavController().navigate(
                R.id.action_shopFragment_to_productFragment,
                bundle
            )
        }

        viewModel.productShop.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressRefreshSwipe()
                    response.data?.let { shopResponse ->
                        Toast.makeText(
                            activity,
                            shopResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        productAdapter.differ.submitList(shopResponse.products.toList())
                    }
                }
                is Resource.Error -> {
                    hideProgressRefreshSwipe()
                    response.message?.let { message ->
                        Toast.makeText(
                            activity,
                            resources.getString(R.string.error_occurred, message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressRefreshSwipe()
                }
            }
        })

        viewModel.getAllProducts()

        swipeRefresh.setOnRefreshListener {
            viewModel.getAllProducts()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.refreshSwipeItem -> {
            showProgressRefreshSwipe()
            viewModel.getAllProducts()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun hideProgressRefreshSwipe() {
        swipeRefresh.isRefreshing = false
    }

    private fun showProgressRefreshSwipe() {
        swipeRefresh.isRefreshing = true
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter()
        rvProducts.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}