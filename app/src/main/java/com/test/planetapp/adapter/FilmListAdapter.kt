package com.test.planetapp.adapter

import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.planetapp.R
import com.test.planetapp.databinding.ItemFilmBinding
import com.test.planetapp.databinding.ItemProgressbarForHorizontalBinding
import com.test.planetapp.model.FilmResponse

class FilmListAdapter(private val context: Context) : BaseAdapter<FilmResponse>() {

    private var footerViewHolder: FooterViewHolder? = null
    private var footerLayoutInflater: LayoutInflater? = null
    private var dataLayoutInflater: LayoutInflater? = null


    override fun getItemViewType(position: Int): Int {
        return if (isLastPosition(position) && isFooterAdded) FOOTER else ITEM
    }

    override fun createHeaderViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder? {
        return null
    }

    override fun createItemViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder? {
        if(dataLayoutInflater == null){
            dataLayoutInflater = LayoutInflater.from(parent?.context)
        }
        val binding = DataBindingUtil.inflate<ItemFilmBinding>(dataLayoutInflater!!,
            R.layout.item_film, parent, false)

        return DataViewHolder(binding)
    }

    override fun createOtherResultViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder? {
        return null
    }

    override fun bindOtherResultViewHolder(viewHolder: RecyclerView.ViewHolder?) {}

    override fun createFooterViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        if(footerLayoutInflater == null){
            footerLayoutInflater = LayoutInflater.from(parent?.context)
        }
        val binding = DataBindingUtil.inflate<ItemProgressbarForHorizontalBinding>(footerLayoutInflater!!,
            R.layout.item_progressbar_for_horizontal, parent, false)
        return FooterViewHolder(binding)
    }

    override fun bindHeaderViewHolder(viewHolder: RecyclerView.ViewHolder?) {}

    override fun bindItemViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        val holder = viewHolder as DataViewHolder?
        val item = getItem(position)
        item.let { setViews(holder, it, position) }
    }

    private fun setViews(holder: DataViewHolder?, film: FilmResponse, position: Int) {

        holder?.binding?.txtName?.text = film.title
        holder?.binding?.txtDetails?.text = film.openingCrawl

        holder?.binding?.root?.setOnClickListener {
            onItemClickListener?.onItemClick(position, it)
        }
    }

    override fun bindFooterViewHolder(viewHolder: RecyclerView.ViewHolder?) {
        val holder = viewHolder as FooterViewHolder?
        footerViewHolder = holder
    }

    override fun displayLoadMoreFooter() {
        footerViewHolder?.footerBinding?.loadingFl?.visibility = View.VISIBLE
    }

    override fun displayErrorFooter() {
        footerViewHolder?.footerBinding?.loadingFl?.visibility = View.GONE
        //handle footer error
    }

    override fun addFooter() {
        isFooterAdded = true
        add(FilmResponse("", "",""))
        notifyDataSetChanged()
    }

    inner class DataViewHolder(val binding: ItemFilmBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener{

        override fun onClick(v: View) {
            onItemClickListener?.onItemClick(adapterPosition, v)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    class FooterViewHolder(val footerBinding: ItemProgressbarForHorizontalBinding) : RecyclerView.ViewHolder(footerBinding.root)
}