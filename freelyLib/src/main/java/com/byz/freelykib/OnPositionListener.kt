package com.byz.freelykib

import android.view.View

abstract class OnPositionListener(var position: Int) : View.OnClickListener {

    override fun onClick(p0: View?) {
        p0?.let {
            onChildItem(it,position  )
        }
    }

    abstract fun onChildItem(view:View,position:Int)
}