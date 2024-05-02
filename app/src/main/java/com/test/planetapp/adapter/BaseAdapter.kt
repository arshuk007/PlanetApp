package com.test.planetapp.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*


abstract class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    protected var items: MutableList<T>
    protected var onItemClickListener: OnMyItemClickListener? = null
    protected var isFooterAdded: Boolean = false

    interface OnMyItemClickListener {
        fun onItemClick(position: Int, view: View)
    }

    interface OnMyReloadClickListener {
        fun onReloadClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        when (viewType) {
            HEADER -> viewHolder = createHeaderViewHolder(parent)
            ITEM -> viewHolder = createItemViewHolder(parent)
            FOOTER -> viewHolder = createFooterViewHolder(parent)
            OTHER_RESULT -> viewHolder = createOtherResultViewHolder(parent)
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            HEADER -> bindHeaderViewHolder(viewHolder)
            ITEM -> bindItemViewHolder(viewHolder, position)
            FOOTER -> bindFooterViewHolder(viewHolder)
            OTHER_RESULT -> bindOtherResultViewHolder(viewHolder)
            else -> {
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    protected abstract fun createHeaderViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder?

    protected abstract fun createItemViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder?
    protected abstract fun createFooterViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder?
    protected abstract fun bindHeaderViewHolder(viewHolder: RecyclerView.ViewHolder?)
    protected abstract fun bindItemViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int)
    protected abstract fun bindFooterViewHolder(viewHolder: RecyclerView.ViewHolder?)
    protected abstract fun createOtherResultViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder?
    protected abstract fun bindOtherResultViewHolder(viewHolder: RecyclerView.ViewHolder?)
    protected abstract fun displayLoadMoreFooter()
    protected abstract fun displayErrorFooter()
    abstract fun addFooter()

    fun getItem(position: Int): T {
        return items[position]
    }

    fun add(item: T) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun add(item: T, position: Int) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    fun addAll(items: List<T>) {
        for (item in items) {
            add(item)
        }
    }

    fun remove(item: T) {
        val position = items.indexOf(item)
        if (position > -1) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isFooterAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    val isEmpty: Boolean
        get() = itemCount == 0

    fun isLastPosition(position: Int): Boolean {
        return position == items.size - 1
    }

    fun removeFooter() {
        isFooterAdded = false
        val position = items.size - 1
        val item: T? = getItem(position)
        if (item != null) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateFooter(footerType: FooterType?) {
        when (footerType) {
            FooterType.LOAD_MORE -> displayLoadMoreFooter()
            FooterType.ERROR -> displayErrorFooter()
            else -> {
            }
        }
    }

    fun setOnMyItemClickListener(onItemClickListener: OnMyItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    enum class FooterType {
        LOAD_MORE, ERROR
    }

    companion object {
        const val HEADER = 0
        const val ITEM = 1
        const val FOOTER = 2
        const val OTHER_RESULT = 3
    }

    init {
        items = ArrayList()
    }
}