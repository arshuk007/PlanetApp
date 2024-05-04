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
import com.test.planetapp.adapter.FilmListAdapter
import com.test.planetapp.adapter.PlanetListAdapter
import com.test.planetapp.adapter.ResidentListAdapter
import com.test.planetapp.databinding.FragmentPlanetDetailsBinding
import com.test.planetapp.model.FilmResponse
import com.test.planetapp.model.Planet
import com.test.planetapp.model.PlanetListResponse
import com.test.planetapp.model.ResidentResponse
import com.test.planetapp.network.Status
import com.test.planetapp.utils.CommonUtils
import com.test.planetapp.utils.showToast
import com.test.planetapp.viewmodel.PlanetDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlanetDetailsFragment : BaseFragment() {

    lateinit var binding: FragmentPlanetDetailsBinding
    val mViewModel: PlanetDetailsViewModel by viewModel()
    var planet: Planet? = null

    //resident items
    var residentList: ArrayList<ResidentResponse> = arrayListOf()
    var residentLayoutManager: LinearLayoutManager? = null
    var isResidentLastPage = false
    var isResidentLoading = false
    var totalResidentCount = 0
    var residentCountFetched = 0
    var residentNextGetURL: String? = null
    var residentListAdapter: ResidentListAdapter? = null

    //film items
    var filmList: ArrayList<FilmResponse> = arrayListOf()
    var filmLayoutManager: LinearLayoutManager? = null
    var isFilmLastPage = false
    var isFilmLoading = false
    var totalFilmCount = 0
    var filmCountFetched = 0
    var filmNextGetURL: String? = null
    var filmListAdapter: FilmListAdapter? = null

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
            //handle resident empty
        } else {
            totalResidentCount = planet?.residents?.size ?: 0
            if (CommonUtils.isNetworkAvailable(requireContext())) {
                planet?.residents?.let {
                    fetchResidentDetails(it.get(0), true)
                }
            }
        }

        if (planet?.films.isNullOrEmpty()) {
            //handle films empty
        } else {
            totalFilmCount = planet?.films?.size ?: 0
            if (CommonUtils.isNetworkAvailable(requireContext())) {
                planet?.films?.let {
                    fetchFilmDetails(it.get(0), true)
                }
            }
        }

    }

    /*
        Fetch each resident details. If fails, retry it
     */
    private fun fetchResidentDetails(resident: String, firstTimeCall: Boolean) {
        mViewModel.getResidentDetails(resident).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    residentList.add(it?.data!!)
                    if (firstTimeCall) {
                        setResidentAdapter(residentList)
                    } else {
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

    /*
         Set adapter for resident list
      */
    fun setResidentAdapter(residents: ArrayList<ResidentResponse>?) {
        residents?.let {
            isResidentLoading = false
            isResidentLastPage = false
            if (residentListAdapter == null) {
                residentListAdapter = ResidentListAdapter(requireContext())
                residentListAdapter?.setOnMyItemClickListener(object :
                    BaseAdapter.OnMyItemClickListener {
                    override fun onItemClick(position: Int, view: View) {
                        "Details page need to be implemented".showToast(requireContext())
                    }
                })
                residentLayoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.recyclerViewResidents.layoutManager = residentLayoutManager
                binding.recyclerViewResidents.adapter = residentListAdapter
                binding.recyclerViewResidents.addOnScrollListener(recyclerViewOnScrollListener)
            } else {
                residentListAdapter?.clear()
            }
            this.residentList = it
            residentListAdapter?.addAll(it)
        }
    }

    /*
     RecyclerView scroll listener to check pagination
    */
    val recyclerViewOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = residentLayoutManager?.childCount
                val totalItemCount = residentLayoutManager?.itemCount
                val firstVisibleItemPosition = residentLayoutManager?.findFirstVisibleItemPosition()

                if (!isResidentLoading && !isResidentLastPage) {
                    if (visibleItemCount!! + firstVisibleItemPosition!! >= totalItemCount!! && firstVisibleItemPosition >= 0 && totalItemCount >= 1) {
                        loadMoreResidentItems()
                    }
                }
            }
        }

    /*
        Fetch next resident details
     */
    fun loadMoreResidentItems() {
        if (totalResidentCount != residentCountFetched) {
            isResidentLoading = true
            planet?.residents?.get(residentCountFetched)?.let {
                residentListAdapter?.addFooter()
                fetchResidentDetails(it, false)
            }
        }
    }

    /*
    Display next resident details
    */
    fun handleSuccessForResidentNextPage(data: ResidentResponse?) {
        isResidentLoading = false
        residentListAdapter?.removeFooter()
        residentListAdapter?.add(data!!)
        if (totalResidentCount == residentCountFetched) {
            isResidentLastPage = true
            residentNextGetURL = null
        } else {
            residentNextGetURL = planet?.residents?.get(residentCountFetched)
        }

    }


    /*
        Fetch each resident details. If fails, retry it
     */
    private fun fetchFilmDetails(film: String, firstTimeCall: Boolean) {
        mViewModel.getFilmDetails(film).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    filmList.add(it?.data!!)
                    if (firstTimeCall) {
                        setFilmAdapter(filmList)
                    } else {
                        handleSuccessForFilmNextPage(it.data)
                    }
                    filmCountFetched = filmCountFetched + 1
                }

                Status.FAIL -> {
                    fetchFilmDetails(film, firstTimeCall)
                }
            }
        })
    }

    /*
         Set adapter for Film list
      */
    fun setFilmAdapter(films: ArrayList<FilmResponse>?) {
        films?.let {
            isFilmLoading = false
            isFilmLastPage = false
            if (filmListAdapter == null) {
                filmListAdapter = FilmListAdapter(requireContext())
                filmListAdapter?.setOnMyItemClickListener(object :
                    BaseAdapter.OnMyItemClickListener {
                    override fun onItemClick(position: Int, view: View) {
                        "Details page need to be implemented".showToast(requireContext())
                    }
                })
                filmLayoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.recyclerViewFilms.layoutManager = filmLayoutManager
                binding.recyclerViewFilms.adapter = filmListAdapter
                binding.recyclerViewFilms.addOnScrollListener(recyclerFilmViewOnScrollListener)
            } else {
                filmListAdapter?.clear()
            }
            this.filmList = it
            filmListAdapter?.addAll(it)
        }
    }

    /*
     RecyclerView scroll listener to check pagination
  */
    val recyclerFilmViewOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = filmLayoutManager?.childCount
                val totalItemCount = filmLayoutManager?.itemCount
                val firstVisibleItemPosition = filmLayoutManager?.findFirstVisibleItemPosition()

                if (!isFilmLoading && !isFilmLastPage) {
                    if (visibleItemCount!! + firstVisibleItemPosition!! >= totalItemCount!! && firstVisibleItemPosition >= 0 && totalItemCount >= 1) {
                        loadMoreFilmItems()
                    }
                }
            }
        }

    /*
        Fetch next Film details
     */
    fun loadMoreFilmItems() {
        if (totalFilmCount != filmCountFetched) {
            isFilmLoading = true
            planet?.films?.get(filmCountFetched)?.let {
                filmListAdapter?.addFooter()
                fetchFilmDetails(it, false)
            }
        }
    }

    /*
    Display next Film details
 */
    fun handleSuccessForFilmNextPage(data: FilmResponse?) {
        isFilmLoading = false
        filmListAdapter?.removeFooter()
        filmListAdapter?.add(data!!)
        if (totalFilmCount == filmCountFetched) {
            isFilmLastPage = true
            filmNextGetURL = null
        } else {
            filmNextGetURL = planet?.films?.get(filmCountFetched)
        }

    }

}