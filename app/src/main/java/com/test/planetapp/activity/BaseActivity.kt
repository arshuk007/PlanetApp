package com.test.planetapp.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.test.planetapp.R

open class BaseActivity: FragmentActivity() {

    fun addFragment(fragment: Fragment, tag: String){
        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, fragment)
        transaction.addToBackStack(tag)
        transaction.commit()
    }

}