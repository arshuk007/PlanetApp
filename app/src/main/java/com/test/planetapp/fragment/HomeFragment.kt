package com.test.planetapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.planetapp.R
import com.test.planetapp.adapter.BaseAdapter
import com.test.planetapp.adapter.PlanetListAdapter
import com.test.planetapp.databinding.FragmentHomeBinding
import com.test.planetapp.model.Planet
import com.test.planetapp.model.PlanetListResponse
import com.test.planetapp.network.Status
import com.test.planetapp.utils.CommonUtils
import com.test.planetapp.utils.showToast
import com.test.planetapp.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: BaseFragment() {

    lateinit var binding : FragmentHomeBinding
    val mViewModel: HomeViewModel by viewModel()

    var isLastPage = false
    var isLoading = false
    var nextGetURL: String? = null
    var adapter: PlanetListAdapter? = null
    var layoutManager: LinearLayoutManager? = null
    var planets: ArrayList<Planet>? = null

    companion object {
        fun newInstance(): HomeFragment{
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init(){
        binding.lifecycleOwner = this
        getPlanetDetails()
    }

    /*
        fetch the latest planet details from server if online
        if offline, show data from from db
     */
    fun getPlanetDetails(){
        if (CommonUtils.isNetworkAvailable(requireContext())){
            mViewModel.getPlanetList().observe(viewLifecycleOwner, Observer {
                when(it.status){
                    Status.SUCCESS -> {
                        handleSuccessForFirstPage(it.data)
                    }
                    Status.FAIL -> {
                        handleFail(it.message)
                    }
                }
            })
        }else{
            getString(R.string.no_internet).showToast(requireContext())
        }
    }

    /*
        show failure message
     */
    fun handleFail(message: String?) {
        message?.showToast(requireContext())
    }

    /*
        handle the response for first page
     */
    fun handleSuccessForFirstPage(data: PlanetListResponse?) {

        if(data?.planets.isNullOrEmpty()){
            //handle empty screen
        }else{
            setAdapter(data?.planets)
            if (data?.next.isNullOrEmpty()) {
                isLastPage = true
                nextGetURL = null
            } else {
                adapter?.addFooter()
                nextGetURL = data?.next
            }
        }
    }

    /*
            handle the response for upcoming pages
    */
    fun handleSuccessForSecondPage(data: PlanetListResponse?) {
        isLoading = false
        adapter?.removeFooter()
        nextGetURL = null

        if(data?.planets.isNullOrEmpty().not()){
            this.planets?.addAll(data?.planets!!)
            adapter?.addAll(data?.planets!!)
            if (data?.next.isNullOrEmpty()) {
                isLastPage = true
                nextGetURL = null
            } else {
                adapter?.addFooter()
                nextGetURL = data?.next
            }
        }
    }

    fun setAdapter(planets: List<Planet>?) {
        isLoading = false
        isLastPage = false
        if (adapter == null) {
            adapter = PlanetListAdapter(requireContext())
            adapter?.setOnMyItemClickListener(object: BaseAdapter.OnMyItemClickListener {
                override fun onItemClick(position: Int, view: View) {
                    parentActivity.addFragment(PlanetDetailsFragment.newInstance(planets?.get(position)), PlanetDetailsFragment::class.java.simpleName)
                }
            })
            layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.layoutManager = layoutManager
            binding.recyclerView.adapter = adapter
            binding.recyclerView.addOnScrollListener(recyclerViewOnScrollListener)
        } else {
            adapter?.clear()
        }
        this.planets = planets as ArrayList<Planet>?
        adapter?.addAll(planets!!)
    }

    val recyclerViewOnScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemCount = layoutManager?.childCount
            val totalItemCount = layoutManager?.itemCount
            val firstVisibleItemPosition = layoutManager?.findFirstVisibleItemPosition()

            if (!isLoading && !isLastPage) {
                if (visibleItemCount!! + firstVisibleItemPosition!! >= totalItemCount!! && firstVisibleItemPosition >= 0 && totalItemCount >= 10) {
                    loadMoreItems()
                }
            }
        }
    }

    fun loadMoreItems() {
        isLoading = true
        getPlanetListForNextPage()
    }

    fun getPlanetListForNextPage(){
        if (CommonUtils.isNetworkAvailable(requireContext())){
            mViewModel.getPlanetListForNextPage(nextGetURL).observe(viewLifecycleOwner, Observer {
                when(it.status){
                    Status.SUCCESS -> {
                        handleSuccessForSecondPage(it.data)
                    }
                    Status.FAIL -> {
                        handleFail(it.message)
                    }
                }
            })
        }else{
            getString(R.string.no_internet).showToast(requireContext())
        }
    }


}