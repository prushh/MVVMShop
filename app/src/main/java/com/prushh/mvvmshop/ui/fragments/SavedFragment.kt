package com.prushh.mvvmshop.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.prushh.mvvmshop.R
import com.prushh.mvvmshop.adapters.ProductAdapter
import com.prushh.mvvmshop.ui.ShopActivity
import com.prushh.mvvmshop.ui.viewmodels.ShopViewModel
import kotlinx.android.synthetic.main.fragment_saved.*

class SavedFragment : Fragment(R.layout.fragment_saved) {

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
                R.id.action_savedFragment_to_productFragment,
                bundle
            )
        }

        // I don't create a class that redefines SimpleCallback but I edit some things.
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val product = productAdapter.differ.currentList[position]
                viewModel.deleteProduct(product)
                Snackbar.make(view, getString(R.string.snack_delete_ok), Snackbar.LENGTH_LONG)
                    .apply {
                        setAction(getString(R.string.snack_undo)) {
                            viewModel.saveProduct(product)
                        }
                        show()
                    }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedProduct)
        }

        viewModel.getSavedProducts().observe(viewLifecycleOwner, Observer { products ->
            tvEmptyDatabase.visibility = if (products.isEmpty()) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
            productAdapter.differ.submitList(products)
        })
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(true)
        rvSavedProduct.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}