package com.amalip.cocktailapp.presentation.cocktails

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
//import android.widget.SearchView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amalip.cocktailapp.R
import com.amalip.cocktailapp.core.extension.failure
import com.amalip.cocktailapp.core.extension.observe
import com.amalip.cocktailapp.core.presentation.BaseFragment
import com.amalip.cocktailapp.core.presentation.BaseViewState
import com.amalip.cocktailapp.databinding.CocktailFragmentBinding
import com.amalip.cocktailapp.domain.model.Cocktail
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlinx.coroutines.DelicateCoroutinesApi

@AndroidEntryPoint
@WithFragmentBindings
@DelicateCoroutinesApi
class CocktailFragment : BaseFragment(R.layout.cocktail_fragment) {

    private lateinit var binding: CocktailFragmentBinding

    private lateinit var adapter: CocktailAdapter
    private lateinit var gridAdapter: CocktailGridAdapter
    private val cocktailViewModel by viewModels<CocktailViewModel>()
    private var flag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cocktailViewModel.apply {
            observe(state, ::onViewStateChanged)
            failure(failure, ::handleFailure)

            doGetCocktailsByName("")
        }
    }

    override fun onViewStateChanged(state: BaseViewState?) {
        super.onViewStateChanged(state)
        when (state) {
            is CocktailViewState.CocktailsReceived -> setUpAdapter(state.cocktails)
        }
    }

    private fun setUpAdapter(cocktails: List<Cocktail>) {
        binding.emptyView.isVisible = cocktails.isEmpty()

        adapter = CocktailAdapter()
        gridAdapter = CocktailGridAdapter()

        adapter.addData(cocktails)
        gridAdapter.addData(cocktails)

        binding.rcCocktails.apply {
            isVisible = cocktails.isNotEmpty()
            adapter = this@CocktailFragment.adapter
        }
    }

    override fun setBinding(view: View) {
        binding = CocktailFragmentBinding.bind(view)

        binding.btnChangeView.setOnClickListener {
            if(!flag){
                binding.rcCocktails.layoutManager = GridLayoutManager(requireContext(),3)
                binding.rcCocktails.apply {
                    adapter = this@CocktailFragment.gridAdapter
                }
                flag = true
            }else{
                binding.rcCocktails.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                binding.rcCocktails.apply {
                    adapter = this@CocktailFragment.adapter
                }
                flag = false
            }

        }

        binding.svCocktail.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(p0: String?): Boolean {
                binding.rcCocktails.layoutManager = LinearLayoutManager(requireContext())
                cocktailViewModel.apply {
                    doGetCocktailsByName(p0!!)
                }
                flag = false
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                binding.rcCocktails.layoutManager = LinearLayoutManager(requireContext())
                cocktailViewModel.apply {
                    doGetCocktailsByName(p0!!)
                }
                flag = false
                return true
            }

        })

        binding.lifecycleOwner = this
    }

}