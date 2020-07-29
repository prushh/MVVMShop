package com.prushh.mvvmshop.ui.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.prushh.mvvmshop.R
import com.prushh.mvvmshop.adapters.ProductAdapter
import com.prushh.mvvmshop.ui.ShopActivity
import com.prushh.mvvmshop.ui.viewmodels.ShopViewModel
import com.prushh.mvvmshop.utils.Resource
import com.prushh.mvvmshop.utils.SharedPref
import kotlinx.android.synthetic.main.fragment_archive.*
import kotlinx.android.synthetic.main.fragment_archive.swipeRefresh

class ArchiveFragment : Fragment(R.layout.fragment_archive) {

    private lateinit var viewModel: ShopViewModel
    private lateinit var productAdapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ShopActivity).viewModel
        setupRecyclerView()

        productAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable(getString(R.string.arg), it)
            }
            findNavController().navigate(
                R.id.action_archiveFragment_to_productFragment,
                bundle
            )
        }

        viewModel.productArchive.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressRefreshSwipe()
                    response.data?.let { shopResponse ->
                        Toast.makeText(
                            activity,
                            shopResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        val products = shopResponse.products

                        tvEmptyArchive.visibility = if (products.isEmpty()) {
                            View.VISIBLE
                        } else {
                            View.INVISIBLE
                        }

                        productAdapter.differ.submitList(products.toList())
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

        viewModel.getArchive(getUserId())

        swipeRefresh.setOnRefreshListener {
            viewModel.getArchive(getUserId())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.refreshSwipeItem -> {
            showProgressRefreshSwipe()
            viewModel.getArchive(getUserId())
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
        rvBoughtProduct.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun getUserId(): Int {
        val sharedPref = activity?.applicationContext?.let { SharedPref(it) }
        return sharedPref?.id!!
    }
}