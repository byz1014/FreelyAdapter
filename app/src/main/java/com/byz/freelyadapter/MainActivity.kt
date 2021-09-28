package com.byz.freelyadapter

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.byz.basekotlin.adapter.litener.OnChildItemListener
import com.byz.basekotlin.adapter.litener.OnRefreshAndMoreListener
import com.byz.freelykib.ManagerRecycleAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var leftAdapter:MyAdapter? = null
    var rightAdapter:MyAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        leftAdapter = MyAdapter(this,ArrayList<String>())
        rightAdapter = MyAdapter(this,ArrayList<String>())
        rv_left.layoutManager = LinearLayoutManager(this)
        rv_left.adapter = leftAdapter
        rv_right.layoutManager = LinearLayoutManager(this)
        rv_right.adapter = rightAdapter
        leftAdapter?.let {
            it.setRefreshAndMoreLoaderEnable(true)
            it.setNewData(addLiftView())
            it.setOnItemListener(object : ManagerRecycleAdapter.OnParentItemListener<String> {
                override fun onItem(t: String, position: Int, v: View) {
                     Toast.makeText(this@MainActivity,"左边list $position",Toast.LENGTH_SHORT).show()
                }
            })
            it.setOnRefreshAndMoreListener(object : OnRefreshAndMoreListener {
                override fun onRefresh(recycle: RecyclerView, adapter: ManagerRecycleAdapter<*>) {
                    handler.sendEmptyMessageDelayed(0,3000)
                }

                override fun onMoreData(recycle: RecyclerView, adapter: ManagerRecycleAdapter<*>) {
                    handler.sendEmptyMessageDelayed(0,3000)
                }

            })

            it.setOnChildItemListener(object : OnChildItemListener {
                override fun onChildItem(viewId: Int, position: Int) {
                     when(viewId){
                         R.id.iv_icon ->{
                             Toast.makeText(this@MainActivity,"左边list icon $position",Toast.LENGTH_SHORT).show()
                         }
                         R.id.tv_num ->{
                             Toast.makeText(this@MainActivity,"左边list num $position",Toast.LENGTH_SHORT).show()
                         }
                     }
                }
            })
        }
        rightAdapter?.let {
            it.addFooderView(R.layout.item_fooder)
            it.addHeaderView(R.layout.item_header)
            it.setRefreshAndMoreLoaderEnable(false)
            it.setNewData(addRightView())
            it.setOnItemListener(object : ManagerRecycleAdapter.OnParentItemListener<String> {
                override fun onItem(t: String, position: Int, v: View) {
                    Toast.makeText(this@MainActivity,"右边list $position",Toast.LENGTH_SHORT).show()
                }
            })
        }
    }



    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            leftAdapter?.let {
                it.closeRefreshAndMoreLoader()

            }
        }
    }


    fun addLiftView():ArrayList<String>{
        var list = ArrayList<String>()
        for(i in 0..26){
            list.add("全能Adapter")
        }
        return list
    }

    fun addRightView():ArrayList<String>{
        var list = ArrayList<String>()
        for(i in 0..6){
            list.add("全能Adapter")
        }
        return list
    }
}
