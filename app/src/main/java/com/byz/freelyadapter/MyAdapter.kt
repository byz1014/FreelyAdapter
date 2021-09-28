package com.byz.freelyadapter

import android.content.Context
import android.view.View
import com.byz.freelykib.ManagerRecycleAdapter
import kotlinx.android.synthetic.main.item_layout.view.*
import java.util.ArrayList

class MyAdapter(var mContext: Context, list: ArrayList<String>) :
    ManagerRecycleAdapter<String>(list) {

    override fun onBindView(view: View, position: Int, t: String) {
        view.tv_num.text = "$position"
        view.tv_message.text = "$t"
        addChildClick(position, view.iv_icon,view.tv_num)
    }

    override fun onLayout(): Int {
        return R.layout.item_layout
    }

    override fun onContext(): Context {
        return mContext
    }

    override fun getEmptyLayout(): Int? {
        return R.layout.layout_item_empty
    }
}