package com.test.planetapp.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.test.planetapp.R
import com.test.planetapp.databinding.ActivityMainBinding
import com.test.planetapp.fragment.HomeFragment

class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding

    val currentFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        init()

    }

    private fun init(){
        showHomeFragment()
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun showHomeFragment(){
        addFragment(HomeFragment.newInstance(), HomeFragment::class.java.simpleName)
    }



    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(currentFragment is HomeFragment){
                finish()
            }else{
                supportFragmentManager.popBackStack()
            }
        }
    }
}
