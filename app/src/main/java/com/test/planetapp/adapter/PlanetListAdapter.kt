package com.test.planetapp.adapter

import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.planetapp.R
import com.test.planetapp.databinding.ItemPlanetBinding
import com.test.planetapp.databinding.ItemProgressbarBinding
import com.test.planetapp.model.Planet

class PlanetListAdapter(private val context: Context) : BaseAdapter<Planet>() {

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
        val binding = DataBindingUtil.inflate<ItemPlanetBinding>(dataLayoutInflater!!,
            R.layout.item_planet, parent, false)

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
        val binding = DataBindingUtil.inflate<ItemProgressbarBinding>(footerLayoutInflater!!,
            R.layout.item_progressbar, parent, false)
        return FooterViewHolder(binding)
    }

    override fun bindHeaderViewHolder(viewHolder: RecyclerView.ViewHolder?) {}

    override fun bindItemViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        val holder = viewHolder as DataViewHolder?
        val item = getItem(position)
        item.let { setViews(holder, it) }
    }

    private fun setViews(holder: DataViewHolder?, planet: Planet) {

        holder?.binding?.txtName?.text = planet.name
        holder?.binding?.txtRotationPeriod?.text = planet.rotationPeriod
        holder?.binding?.txtOrbitalPeriod?.text = planet.orbitalPeriod
        holder?.binding?.txtDiameter?.text = planet.diameter
        holder?.binding?.txtTerrain?.text = planet.terrain
        holder?.binding?.txtPopulation?.text = planet.population
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
        add(Planet("", "","",
            "","","","",
            "","","", "",
            "",null,null))
    }

    inner class DataViewHolder(val binding: ItemPlanetBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener{

        override fun onClick(v: View) {
            onItemClickListener?.onItemClick(adapterPosition, v)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    class FooterViewHolder(val footerBinding: ItemProgressbarBinding) : RecyclerView.ViewHolder(footerBinding.root)
}