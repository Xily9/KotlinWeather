package com.xily.kotlinweather.base

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

/**
 * RecycleView通用基类
 * 极度简化了RecycleView的使用
 *
 * @param <T> 子类的ViewHolder
 * @param <U> List容器的类型
 * @author Xily
 */

abstract class BaseAdapter<T : BaseAdapter.BaseViewHolder, U>(var list: List<U>?) : RecyclerView.Adapter<T>() {
    var context: Context? = null
        private set
    private var onItemClickListener: ((position: Int) -> Unit?)? = null
    private var onItemLongClickListener: ((position: Int) -> Boolean?)? = null

    @get:LayoutRes
    protected abstract val layoutId: Int

    fun setOnItemClickListener(onItemClickListener: (position: Int) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: (position: Int) -> Boolean) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        context = parent.context
        val holder = createViewHolder(LayoutInflater.from(context).inflate(layoutId, parent, false))
        onItemClickListener?.let {
            holder.itemView.setOnClickListener({
                val position = holder.adapterPosition
                onItemClickListener?.invoke(position)
            })
        }
        onItemLongClickListener?.let {
            holder.itemView.setOnLongClickListener({
                val position = holder.adapterPosition
                onItemLongClickListener?.invoke(position) ?: false
            })
        }
        return holder
    }

    /**
     * 利用反射获取子类的ViewHolder实例
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewHolder(view: View): T {
        val type = javaClass.genericSuperclass//获取父类Type
        if (type is ParameterizedType) {//判断是不是泛型参数列表
            val types = type.actualTypeArguments//获取泛型参数列表
            val clazz = types[0] as Class<*>//获取第一个Class
            try {
                val cons: Constructor<*>
                return if (clazz.isMemberClass && !Modifier.isStatic(clazz.modifiers)) {//是内部类且不是静态类
                    cons = clazz.getDeclaredConstructor(javaClass, View::class.java)//获取内部类的构造函数,要注意必须要传入外部类的Class
                    cons.isAccessible = true//设置为可访问
                    cons.newInstance(this, view) as T//实例化
                } else {
                    cons = clazz.getDeclaredConstructor(View::class.java)
                    cons.isAccessible = true
                    cons.newInstance(view) as T
                }
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }

        }
        return BaseViewHolder(view) as T
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        onBindViewHolder(holder, position, list!![position])
    }

    protected abstract fun onBindViewHolder(holder: T, position: Int, value: U)

    open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}
