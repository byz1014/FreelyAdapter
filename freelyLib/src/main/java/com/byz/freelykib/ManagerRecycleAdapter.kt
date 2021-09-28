package com.byz.freelykib

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.byz.basekotlin.adapter.litener.OnChildItemListener
import com.byz.basekotlin.adapter.litener.OnItemListener
import com.byz.basekotlin.adapter.litener.OnRefreshAndMoreListener
import kotlinx.android.synthetic.main.layout_refresh.view.*
import java.text.SimpleDateFormat
import java.util.*

abstract class ManagerRecycleAdapter<T>(var list: ArrayList<T>) :
    RecyclerView.Adapter<BaseViewHolder>() {
    var TAG: String = "CheckAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        var view: View? = null
        when (viewType) {
            typeEmpty -> {
                view = if (getEmptyLayout() == null) {
                    LayoutInflater.from(onContext()).inflate(onLayout(), parent, false)
                } else {
                    LayoutInflater.from(onContext()).inflate(getEmptyLayout()!!, parent, false)
                }

                view?.let {
                    it.tag = typeEmpty
                }
            }
            typeHeader -> {
                view = if (headerLayout == null) {
                    LayoutInflater.from(onContext()).inflate(onLayout(), parent, false)
                } else {
                    LayoutInflater.from(onContext()).inflate(headerLayout!!, parent, false)
                }
                view?.let {
                    it.tag = typeHeader
                }
            }
            typeRefresh -> {
                view =LayoutInflater.from(onContext()).inflate(R.layout.layout_refresh, parent, false)
                view?.let {
                    it.tag = typeRefresh
                }
            }
            typeMoreLoader -> {
                view = LayoutInflater.from(onContext())
                    .inflate(R.layout.layout_more_load, parent, false)

                view?.let {
                    it.tag = typeMoreLoader
                }
            }
            typeFooder -> {
                view = if (fooderLayout == null) {
                    LayoutInflater.from(onContext()).inflate(onLayout(), parent, false)
                } else {
                    LayoutInflater.from(onContext()).inflate(fooderLayout!!, parent, false)
                }
                view?.let {
                    it.tag = typeFooder
                }
            }
            else -> {
                view = LayoutInflater.from(onContext()).inflate(onLayout(), parent, false)
                view?.let {
                    it.tag = -1
                }
            }
        }
        return BaseViewHolder(view!!)
    }

    var headerHeight = 0
    var fooderHeight = 0
    var itemHeight = 0
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        var newIndex = position
        when(holder.convertView.tag ){
            typeRefresh ->{
               holder.convertView.tv_top_time.text = "本次请求：$time"
                if(firstTime == null ){
                    holder.convertView.tv_bottom_time.visibility = View.GONE
                }else{
                    holder.convertView.tv_bottom_time.visibility = View.VISIBLE
                    holder.convertView.tv_bottom_time.text = "上次请求：$firstTime"
                }
                firstTime = time
                headerHeight = holder.convertView.height
            }
            typeFooder ->{
                fooderHeight = holder.convertView.height
            }
            -1 ->{
                itemHeight = holder.convertView.height
            }


        }
        if (showRefresh) {
            newIndex--
        }
        if (showEmpty && list.size == 0) {
            newIndex--
        }
        if (showHeader) {
            newIndex--
        }

        if (holder.itemView.tag == -1) {
            if (newIndex < list.size) {
                onBindView(holder.itemView, newIndex, list[newIndex])
                holder.convertView.setOnClickListener(object :
                    OnItemListener<T>(list[newIndex], newIndex) {
                    override fun onItemClick(t: T, position: Int, v: View) {
                        listener?.let {
                            it.onItem(t, position, v)
                        }
                    }
                })

            }
        }
    }

    open fun addChildClick(position: Int,vararg vies: View?){
        for (view in vies){
                view?.let {
                    it.setOnClickListener(object : OnPositionListener(position){
                        override fun onChildItem(view: View, position: Int) {
                            if(childItemListener!=null){
                                childItemListener!!.onChildItem(view.id,position)
                            }
                        }
                    })
                }
        }
    }

    abstract fun onBindView(view: View, position: Int, t: T)


    override fun getItemCount(): Int {
        showEmpty = getEmptyLayout() != null
        var sum = 0
        if (showRefresh) {
            sum++
        }
        if (showHeader) {
            sum++
        }

        if (showEmpty && list.size == 0) {
            sum++
        }
        if (showFooder) {
            sum++
        }
        if (showMoreLoader) {
            sum++
        }
        return sum + list.size
    }

    /**
     * item layout
     */
    abstract fun onLayout(): Int

    abstract fun onContext(): Context

    /**
     * 增加更多数据
     */
    fun setAddData(newList: ArrayList<T>) {
        for (t in newList) {
            list.add(t)
        }
        notifyDataSetChanged()
    }

    /**
     * 更换新数据
     */
    fun setNewData(newList: ArrayList<T>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    var showHeader: Boolean = false
    var showFooder: Boolean = false
    var showMoreLoader: Boolean = false
    var showRefresh: Boolean = false
    var showEmpty: Boolean = false

    companion object {
        var typeEmpty = 0x01
        var typeHeader = 0x02
        var typeRefresh = 0x03
        var typeMoreLoader = 0x04
        var typeFooder = 0x05
    }

    override fun getItemViewType(position: Int): Int {

        when (position) {
            0 -> {
                if (showRefresh) {
                    return typeRefresh
                }
                if (showEmpty && list.size == 0) {
                    return typeEmpty
                }
                if (showHeader) {
                    return typeHeader
                }
            }
            1 -> {
                if (showRefresh && showHeader) {
                    return typeHeader
                }

                if (showRefresh && showEmpty && list.size == 0) {
                    return typeEmpty
                }

                if (showHeader && showEmpty && list.size == 0) {
                    return typeEmpty
                }
            }
            2 -> {
                if (showRefresh && showEmpty && list.size == 0 && showHeader) {
                    return typeEmpty
                }
            }
            (itemCount - 2) -> {
                if (showMoreLoader && showFooder) {
                    return typeFooder
                }
            }
            (itemCount - 1) -> {
                if (showMoreLoader) {
                    return typeMoreLoader
                }

                if (showFooder) {
                    return typeFooder
                }

            }
        }
        return -1
    }

    /**
     * 空界面
     */
    abstract fun getEmptyLayout(): Int?

    var fooderLayout: Int? = null
    var headerLayout: Int? = null

    /**
     * 添加HeaderView
     */
    fun addHeaderView(view: Int) {
        this.headerLayout = view
        showHeader = true
    }

    /**
     * 添加FooderView
     */
    fun addFooderView(view: Int) {
        this.fooderLayout = view
        showFooder = true
    }


    var mRefreshEnable = false
    var mMoreLoaderEnable = false

    /**
     * 刷新开关
     */
    fun setRefreshEnable(boolean: Boolean) {
        mRefreshEnable = boolean
    }

    /**
     * 加载开关
     */
    fun setMoreLoaderEnable(boolean: Boolean) {
        mMoreLoaderEnable = boolean
    }

    /***
     * 刷新和加载的开关
     */
    fun setRefreshAndMoreLoaderEnable(boolean: Boolean) {
        mMoreLoaderEnable = boolean
        mRefreshEnable = boolean
    }


    var listener: OnParentItemListener<T>? = null
    /**
     * item点击事件
     */
    fun setOnItemListener(openListener: OnParentItemListener<T>) {
        this.listener = openListener
    }

    interface OnParentItemListener<T> {
        fun onItem(t: T, position: Int, v: View)
    }


    private var mRecyclerView: RecyclerView? = null

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                index = newState
            }
        })

        var pointDown: Float = -1.0f
        recyclerView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    Log.e(
                        "byz_recycle_touch",
                        "$pointDown              ${event.y}            ${mRecyclerView == null}"
                    )
                    mRecyclerView?.let {
                        if (pointDown == -1.0f) {
                            pointDown = event.y
                        } else {
                            moveRefreshMoreData(
                                it, when {
                                    event.y - pointDown > 0 -> {
                                        RecycleState.REFRESH
                                    }
                                    event.y - pointDown < 0 -> {
                                        RecycleState.MORELOAD
                                    }
                                    else -> {
                                        RecycleState.TRANSIT
                                    }
                                }
                            )
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    pointDown = -1.0f
                    Log.e("byz_recycle_touch", "up              ${event.y} ")
                }
            }
            return@setOnTouchListener false
        }

    }


    var index = 0

    fun getManagerPosition(recyclerView: RecyclerView) {
        recyclerView.layoutManager?.let {
            var manager = it as LinearLayoutManager
            var one = manager.findLastVisibleItemPosition()
            var two = manager.findFirstVisibleItemPosition()
            var three = manager.findLastCompletelyVisibleItemPosition()
            var four = manager.findFirstCompletelyVisibleItemPosition()
            Log.e(
                "byz_check_manager", "最后一个可见位置:$one            \n第一个可见位置:$two            " +
                        "\n最后一个完全可见位置:$three            \n第一个完全可见位置:$four         "
            )
        }
    }


    private fun getTop(recyclerView: RecyclerView): Int {
        recyclerView.layoutManager?.let {
            var manager = it as LinearLayoutManager
            return manager.findFirstCompletelyVisibleItemPosition()
        }
        return -1
    }

    private fun getBottom(recyclerView: RecyclerView): Int {
        recyclerView.layoutManager?.let {
            var manager = it as LinearLayoutManager
            return manager.findLastCompletelyVisibleItemPosition()
        }
        return -1
    }

    /**
     * 停止刷新和加载 View
     */
    fun closeRefreshAndMoreLoader() {
        showRefresh = false
        showMoreLoader = false
        indexRefresh = 0
        notifyDataSetChanged()
    }


    var mRefreshMoreListener: OnRefreshAndMoreListener? = null
    /**
     * 停止刷新和加载的监听
     */
    fun setOnRefreshAndMoreListener(listener: OnRefreshAndMoreListener) {
        mRefreshMoreListener = listener
    }


    enum class RecycleState {
        REFRESH, MORELOAD, TRANSIT
    }

    var time: String? =null
    var indexRefresh = 0
    private fun moveRefreshMoreData(recyclerView: RecyclerView, state: RecycleState) {

        when (state) {
            RecycleState.REFRESH -> {

                Log.e("check_refresh","$showRefresh")
                    if (getTop(recyclerView) == 0 && mRefreshEnable && !showRefresh  ) {
                        showRefresh = true
                        notifyDataSetChanged()
                        mRefreshMoreListener?.let {
                            indexRefresh++
                            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                              time = dateFormat.format(Calendar.getInstance().timeInMillis )
                            it.onRefresh(recyclerView, this@ManagerRecycleAdapter)
                        }
                    }
            }
            RecycleState.MORELOAD -> {
                if (!showMoreLoader) {


                    var layoutManager:LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    Log.e("check_height","${layoutManager.findFirstCompletelyVisibleItemPosition()}           \n" +
                            "\n${layoutManager.findLastCompletelyVisibleItemPosition()}\n" +
                            "${(recyclerView.childCount-1)}")
                    if(layoutManager.findFirstCompletelyVisibleItemPosition() != 0 && layoutManager.findLastCompletelyVisibleItemPosition() >= (recyclerView.childCount-1)){
                        if (getBottom(recyclerView) == (itemCount - 1) && mMoreLoaderEnable&&!showMoreLoader) {
                            showMoreLoader = true
                            notifyDataSetChanged()
                            mRefreshMoreListener?.let {
                                it.onMoreData(recyclerView, this@ManagerRecycleAdapter)
                            }
                        }
                    }
                }
            }
            RecycleState.TRANSIT -> {

            }
        }
    }

    var firstTime:String? = null



   var childItemListener: OnChildItemListener? = null

    fun setOnChildItemListener(mOnChildItemListener:OnChildItemListener){
        this.childItemListener = mOnChildItemListener
    }


}