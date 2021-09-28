package com.byz.basekotlin.adapter.litener

import androidx.recyclerview.widget.RecyclerView
import com.byz.freelykib.ManagerRecycleAdapter

interface OnRefreshAndMoreListener {
    fun onRefresh(recycle:RecyclerView,adapter: ManagerRecycleAdapter<*>)
    fun onMoreData(recycle:RecyclerView,adapter:ManagerRecycleAdapter<*>)
}