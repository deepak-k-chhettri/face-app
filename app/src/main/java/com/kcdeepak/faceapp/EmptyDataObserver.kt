package com.kcdeepak.faceapp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver

class EmptyDataObserver(rv: RecyclerView, view: View) : AdapterDataObserver() {
    private var emptyView: View?=null
    private var recyclerView:RecyclerView?=null

    init {
        recyclerView = rv
        emptyView = view
        checkIfEmpty()
    }

    private fun checkIfEmpty() {
        if(emptyView!=null && recyclerView!!.adapter!=null){
            val emptyViewVisible = recyclerView!!.adapter!!.itemCount==0
            emptyView!!.visibility = if(emptyViewVisible) View.VISIBLE else View.GONE
            recyclerView!!.visibility = if(emptyViewVisible) RecyclerView.GONE else RecyclerView.VISIBLE
        }
    }

    override fun onChanged() {
        super.onChanged()
        checkIfEmpty()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        super.onItemRangeChanged(positionStart, itemCount)
    }
}