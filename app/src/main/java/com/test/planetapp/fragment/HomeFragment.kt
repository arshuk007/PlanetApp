package com.test.planetapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.test.planetapp.R
import com.test.planetapp.databinding.FragmentHomeBinding
import com.test.planetapp.model.PlanetListResponse
import com.test.planetapp.network.Status
import com.test.planetapp.utils.CommonUtils
import com.test.planetapp.utils.showToast
import com.test.planetapp.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: BaseFragment() {

    lateinit var binding : FragmentHomeBinding
    private val mViewModel: HomeViewModel by viewModel()

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

    private fun init(){
        binding.lifecycleOwner = this
        getPlanetDetails()
    }

    private fun getPlanetDetails(){
        if (CommonUtils.isNetworkAvailable(requireContext())){
            mViewModel.getPlanetList().observe(viewLifecycleOwner, Observer {
                when(it.status){
                    Status.SUCCESS -> {
                        handleSuccess(it.data)
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

    private fun handleFail(message: String?) {
        message?.showToast(requireContext())
    }

    private fun handleSuccess(data: PlanetListResponse?) {

    }

}