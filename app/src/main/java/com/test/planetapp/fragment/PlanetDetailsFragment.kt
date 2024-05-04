package com.test.planetapp.fragment

import android.os.Build
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
import com.test.planetapp.adapter.ResidentListAdapter
import com.test.planetapp.databinding.FragmentPlanetDetailsBinding
import com.test.planetapp.model.Planet
import com.test.planetapp.model.PlanetListResponse
import com.test.planetapp.model.ResidentResponse
import com.test.planetapp.network.Status
import com.test.planetapp.utils.CommonUtils
import com.test.planetapp.viewmodel.PlanetDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlanetDetailsFragment : BaseFragment() {

    lateinit var binding: FragmentPlanetDetailsBinding
    val mViewModel: PlanetDetailsViewModel by viewModel()
    var planet: Planet? = null
    var residentList: ArrayList<ResidentResponse> = arrayListOf()
    var layoutManager: LinearLayoutManager? = null
    var isResidentLastPage = false
    var isResidentLoading = false
    var totalResidentCount = 0
    var residentCountFetched = 0
    var residentnextGetURL: String? = null
    var residentListAdapter: ResidentListAdapter? = null

    companion object {
        fun newInstance(planet: Planet?): PlanetDetailsFragment {
            val fragment = PlanetDetailsFragment()
            val bundle = Bundle()
            bundle.putParcelable("data", planet)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            planet = arguments?.getParcelable("data", Planet::class.java)
        } else {
            planet = arguments?.getParcelable("data")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_planet_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init() {
        binding.lifecycleOwner = this
        setPlanetDetails()
    }

    private fun setPlanetDetails() {

        binding.txtName.text = planet?.name
        binding.txtRotationPeriod.text = planet?.rotationPeriod
        binding.txtOrbitalPeriod.text = planet?.orbitalPeriod
        binding.txtDiameter.text = planet?.diameter
        binding.txtTerrain.text = planet?.terrain
        binding.txtPopulation.text = planet?.population

        if (planet?.residents.isNullOrEmpty()) {

        } else {
            totalResidentCount = planet?.residents?.size ?: 0
            if (CommonUtils.isNetworkAvailable(requireContext())) {
                planet?.residents?.let {
                    fetchResidentDetails(it.get(0), true)
                }
            }
        }

    }

    private fun fetchResidentDetails(resident: String, firstTimeCall: Boolean) {
        mViewModel.getResidentDetails(resident).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    residentList.add(it?.data!!)
                    if(firstTimeCall){
                        setResidentAdapter(residentList)
                    }else{
                        handleSuccessForResidentNextPage(it.data)
                    }
                    residentCountFetched = residentCountFetched + 1
                }

                Status.FAIL -> {
                    fetchResidentDetails(resident, firstTimeCall)
                }
            }
        })
    }


    fun setResidentAdapter(residents: ArrayList<ResidentResponse>?) {
        residents?.let {
            isResidentLoading = false
            isResidentLastPage = false
            if (residentListAdapter == null) {
                residentListAdapter = ResidentListAdapter(requireContext())
                residentListAdapter?.setOnMyItemClickListener(object: BaseAdapter.OnMyItemClickListener {
                    override fun onItemClick(position: Int, view: View) {
                    }
                })
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.recyclerViewResidents.layoutManager = layoutManager
                binding.recyclerViewResidents.adapter = residentListAdapter
                binding.recyclerViewResidents.addOnScrollListener(recyclerViewOnScrollListener)
            } else {
                residentListAdapter?.clear()
            }
            this.residentList = it
            residentListAdapter?.addAll(it)
        }
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

            if (!isResidentLoading && !isResidentLastPage) {
                if (visibleItemCount!! + firstVisibleItemPosition!! >= totalItemCount!! && firstVisibleItemPosition >= 0 && totalItemCount >= 1) {
                    loadMoreResidentItems()
                }
            }
        }
    }

    fun loadMoreResidentItems() {
        if(totalResidentCount != residentCountFetched) {
            isResidentLoading = true
            planet?.residents?.get(residentCountFetched)?.let {
                residentListAdapter?.addFooter()
                fetchResidentDetails(it, false)
            }
        }
    }

    fun handleSuccessForResidentNextPage(data: ResidentResponse?) {
        isResidentLoading = false
        residentListAdapter?.removeFooter()
        residentListAdapter?.add(data!!)
        if(totalResidentCount == residentCountFetched){
            isResidentLastPage = true
            residentnextGetURL = null
        }else{
            residentnextGetURL = planet?.residents?.get(residentCountFetched)
        }

    }

}