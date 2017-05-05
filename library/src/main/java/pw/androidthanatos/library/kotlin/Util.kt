package pw.androidthanatos.library.kotlin

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import pw.androidthanatos.library.kotlin.annotation.Alias
import pw.androidthanatos.library.kotlin.annotation.Controller
import pw.androidthanatos.library.kotlin.request.MethodInfo
import pw.androidthanatos.library.kotlin.annotation.RequestMapping
import pw.androidthanatos.library.kotlin.exception.RouterBootAliasNameIsEmptyException
import java.util.concurrent.ConcurrentHashMap

/**
 * Created  on 2017/5/2.
 * 工具类封装
 */


/**
 * 日志的封装方法
 */
internal fun log(msg:String,tag:String="RouterBoot"){
    Log.d(tag, msg)
}



internal  object AM{
    @JvmStatic val am:ConcurrentHashMap<String,Class<Any>> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
        ConcurrentHashMap<String,Class<Any>>()
    }
}

internal object Ctx{
    @JvmStatic var init:Context? =null
}


/**
 * 将所有有别名的activity保存到activityManager里面去，方便后期统一维护和管理
 * @param context 上下文对象
 * @param am 存放activity和其别名的map
 * @param pkgs 需要注册的activity所在的包的集合
 */
internal fun saveActivities(context: Context,pkgs: List<String>){
    pkgs.forEach {
        val packageInfo = context.applicationContext.packageManager.getPackageInfo(it, PackageManager.GET_ACTIVITIES)
        packageInfo.activities.forEachIndexed { index, activityInfo ->
            log("---------->注册的activity：${activityInfo.name}")
            val clazz:Class<Any> = Class.forName(activityInfo.name) as Class<Any>
            AM.am.put(getAlias(clazz),clazz)

        }
    }

}

/**
 * 通过activity类获取activity的别名
 * @param clazz activity类名
 */
internal fun getAlias(clazz:Class<Any>): String{
    if (clazz.isAnnotationPresent(Alias::class.java)){
        val alias = clazz.getAnnotation(Alias::class.java)
        if (alias.value.isEmpty()) throw RouterBootAliasNameIsEmptyException("activity 的别名不能为空。所属activity为：${clazz.simpleName}")
        return alias.value
    }else{
        return ""
    }
}

internal fun configController(clssList: MutableList<Class<*>>, ci: ConcurrentHashMap<String, MethodInfo>) {
    val classes =clssList.filterNot {
        !it.isAnnotationPresent(Controller::class.java)
    }
    var claz:Class<Any>
    classes.forEach {
        claz=it as Class<Any>
        it.declaredMethods.filterNot {
            !it.isAnnotationPresent(RequestMapping::class.java)
        }.forEach {
                    val requestMapping=it.getAnnotation(RequestMapping::class.java)
                    ci.put(requestMapping.value, MethodInfo(claz,it,requestMapping.method))
                }
    }
}

