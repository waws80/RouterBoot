package pw.androidthanatos.library.kotlin.modelandview

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import pw.androidthanatos.library.kotlin.AM
import pw.androidthanatos.library.kotlin.Ctx
import pw.androidthanatos.library.kotlin.annotation.Receiver
import pw.androidthanatos.library.kotlin.exception.RouterBootAliasNameIsEmptyException
import pw.androidthanatos.library.kotlin.exception.RouterBootInitException
import pw.androidthanatos.library.kotlin.exception.RouterBootReceiverMethodParemeterException
import pw.androidthanatos.library.kotlin.exception.RouterBootReceiverMethodSizeException
import pw.androidthanatos.library.kotlin.log
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap

/**
 *  on 2017/5/3.
 */
class ModelAndView {

    private val model:ConcurrentHashMap<String,Any> = ConcurrentHashMap()

    private var view:String? =null

    fun addModelObject(key: String ,value: Any) :ModelAndView{
        model.put(key, value)
        return this
    }

    fun setView(view: String) :ModelAndView{
        this.view=view
        return this
    }

    fun go(){
        //判断当前显示在用户面前的activity进行显示，否则不显示
        val filterNot = AM.am.filterNot { it.key != view }
        if (filterNot.isEmpty()) throw  RouterBootAliasNameIsEmptyException("当前所访问的别名指向的界面不存在，当前的界面别名：${view}")
        val clazz = filterNot[view]
        val nowActivity=clazz?.newInstance() as Activity
        if (Ctx.init == null) throw RouterBootInitException("请先初始化RouterBoot 在Application 中")
        val intent:Intent = Intent(Ctx.init,clazz)
        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
        Ctx.init?.startActivity(intent)
        val methodNot = clazz.declaredMethods.filterNot { !it.isAnnotationPresent(Receiver::class.java) }
        if (!methodNot.isEmpty()){
            if (methodNot.size == 1){
                val method = methodNot[0]
                if (method.parameterTypes.size != 1) throw RouterBootReceiverMethodParemeterException("接收值的方法没有参数或者参数的个数大于一个")
                if (method.parameterTypes[0] != ConcurrentHashMap::class.java) throw RouterBootReceiverMethodParemeterException("接收值的方法的参数不是ConcurrentHashMap<String,Any>类型")
                method.isAccessible=true
                method.invoke(nowActivity,model)
            }else{
                throw RouterBootReceiverMethodSizeException("接收值的方法存在多个，但是一个Activity中只允许有一个存在")
            }
        }
    }






}