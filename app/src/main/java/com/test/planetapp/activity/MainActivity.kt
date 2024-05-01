package com.test.planetapp.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil
import com.test.planetapp.R
import com.test.planetapp.databinding.ActivityMainBinding
import com.test.planetapp.fragment.HomeFragment

class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        init()

    }

    private fun init(){
        showHomeFragment()
    }

    private fun showHomeFragment(){
        addFragment(HomeFragment.newInstance(), HomeFragment::class.java.simpleName)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
