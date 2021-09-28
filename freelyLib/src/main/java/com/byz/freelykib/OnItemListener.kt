package com.byz.basekotlin.adapter.litener

import android.view.View

abstract class OnItemListener<T> (var t:T, var position:Int) : View.OnClickListener {
    override fun onClick(p0: View?) {
        p0?.let {
            onItemClick(t,position,it)
        }
    }

    abstract fun onItemClick(t:T, position:Int,v:View)


}