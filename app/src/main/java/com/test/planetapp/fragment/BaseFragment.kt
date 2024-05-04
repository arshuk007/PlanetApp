package com.test.planetapp.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import com.test.planetapp.activity.MainActivity

open class BaseFragment: Fragment() {

    lateinit var parentActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = context as MainActivity
    }
}