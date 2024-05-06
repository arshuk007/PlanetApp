package com.test.planetapp.activity

import android.content.Context
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.test.planetapp.R

open class BaseActivity: FragmentActivity() {

    private lateinit var progressDialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressDialog = AlertDialog.Builder(this)
            .setView(ProgressDialogLayout(this))
            .create()
    }

    fun addFragment(fragment: Fragment, tag: String){
        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, fragment)
        transaction.addToBackStack(tag)
        transaction.commit()
        supportFragmentManager.executePendingTransactions()
    }

    fun showLoading() {
        progressDialog.show()
    }

    fun dismissLoading() {
        progressDialog.dismiss()
    }

}

class ProgressDialogLayout(context: Context) : ProgressBar(context) {
    init {
        isIndeterminate = true
        setPadding(20, 20, 20, 20)
    }
}
