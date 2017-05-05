package pw.androidthanatos.library.kotlin

import android.content.Context
import org.jetbrains.annotations.NotNull
import org.json.JSONObject
import pw.androidthanatos.library.kotlin.request.MethodInfo
import pw.androidthanatos.library.kotlin.request.Request
import pw.androidthanatos.library.kotlin.request.RequestMethod
import pw.androidthanatos.library.kotlin.exception.*
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

/**
 * Created  on 2017/5/2.
 * @author thanatos
 * RouterBoot框架
 */
class RouterBoot {

    /**
     * 初始上下文对象
     */
    private var mContext: Context? = null


    private var mBaseUrl:String? = null

    /**
     * 创建activityManager 用来管理所有有别名的activity
     */
    private val am:ConcurrentHashMap<String,Class<Any>> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
        ConcurrentHashMap<String,Class<Any>>()
    }

    /**
     * 控制器里面的方法存储器
     */
    private val ci:ConcurrentHashMap<String, MethodInfo> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
        ConcurrentHashMap<String, MethodInfo>()
    }

    /**
     * 对外暴露的初始化方法和销毁方法
     */
    companion object{
        private var rb:RouterBoot? =null
        /**
         * 初始化
         */
        fun init(@NotNull context: Context, @NotNull pkgs: MutableList<String>,
                 @NotNull classList: MutableList<Class<*>>, @NotNull baseUrl:String) :RouterBoot{
            Ctx.init=context
            rb=RouterBoot()
            rb?.creat(context,pkgs,classList,baseUrl)
            return rb!!
        }

        /**
         * 通过get方式访问控制层的接口
         */
        fun get(@NotNull url: String, @NotNull header:JSONObject = JSONObject()){
            rb?.get(url,header)
        }


        /**
         * 通过post方式访问控制层的接口
         */
        fun post(@NotNull url: String, @NotNull header: JSONObject = JSONObject(), @NotNull body: JSONObject = JSONObject()){
            rb?.post(url,header,body)
        }

        /**
         * 销毁
         */
        fun onDestory(){
            if (rb != null){
                rb?.destory()
            }
        }

    }


    /**
     * 初始化方法，对与框架内部的初始化
     */
    private fun creat(context: Context, pkgs: List<String>, classList: MutableList<Class<*>>, baseUrl: String){
        this.mContext=context
        this.mBaseUrl=baseUrl
        saveActivities(context, pkgs)
        configController(classList,ci)
    }

    /**
     * get请求发送url给控制层
     */
    private fun get(url: String, header: JSONObject) {
        if (!url.contains(mBaseUrl as CharSequence)) throw RouterBootBaseUrlException("基础链接不同 当前url：${url}  基础url：${mBaseUrl}")
        val api=url.split(mBaseUrl!!)
        var path=api[1]
        val request: Request
        if (path.contains("?")){
            val arr=path.split("?")
            path=arr[0]
            request= Request(url, RequestMethod.GET,parseQuery(arr[1]),header,JSONObject())
        }else{
            request= Request(url, RequestMethod.GET, JSONObject(),header, JSONObject())
        }
        val s=ci.filterNot { it.key !=path }.size
        if (s >1) throw RouterBootMethodException("有<${path}>表示的方法不唯一")
        if (s == 0) throw RouterBootMethodException("没有<${path}>表示的方法")
        ci.filterNot { it.key !=path }.forEach {
            invokeMethod(it,request,0)
        }

    }


    private fun post(url: String, header: JSONObject, body: JSONObject) {
        if (!url.contains(mBaseUrl as CharSequence)) throw RouterBootBaseUrlException("基础链接不同 当前url：${url}  基础url：${mBaseUrl}")
        val api=url.split(mBaseUrl!!)
        var path=api[1]
        val request: Request
        if (path.contains("?")){
            throw RouterBootRequestURLException("当前请求方式为POST，但是请求的url包含查询条件")
        }else{
            request= Request(url, RequestMethod.POST, JSONObject(),header, body)
        }
        val s=ci.filterNot { it.key !=path }.size
        if (s >1) throw RouterBootMethodException("有<${path}>表示的方法不唯一")
        if (s == 0) throw RouterBootMethodException("没有<${path}>表示的方法")
        ci.filterNot { it.key !=path }.forEach {
            invokeMethod(it,request,1)
        }

    }

    /**
     * 调起控制层相对应的方法
     */
    private fun invokeMethod(it: Map.Entry<String, MethodInfo>, request: Request, t:Int) {
        val methodInfo=it.value
        val instance=methodInfo.clazz.newInstance()
        val len=methodInfo.method.parameterTypes.size
        val method: Method=methodInfo.method
        if ( t == 0 && methodInfo.type != RequestMethod.GET) throw RouterBootRequestMappingArgumentException("请求的方式 是 GET 但是RequestMapping 参数确实POST")
        if (t == 1 && methodInfo.type != RequestMethod.POST) throw RouterBootRequestMappingArgumentException("请求的方式 是 POST 但是RequestMapping 参数确实GET")
        if (len >1){
            throw RouterBootMethodArgumentSizeException("方法${method}的形參个数不能超过一个")
        }else if (len == 1){
            if (method.parameterTypes[0] == request.javaClass){
                method.invoke(instance,request)
            }else{
                throw RouterBootMethodArgumentException("方法${method}的参数不是${request.javaClass}")
            }
        }else{
            method.invoke(instance)
        }
    }

    /**
     * 解析查询条件
     */
    private fun parseQuery(s: String): JSONObject {
        val json:JSONObject= JSONObject()
        if (s.contains("&")){
            val arr=s.split("&")
            arr.filterNot { !it.contains("=") }.forEach {
                val keyAndValue =it.split("=")
                json.put(keyAndValue[0],keyAndValue[1])
            }
            return json
        }else{
            if (s.contains("=")){
                val keyAndValue =s.split("=")
                json.put(keyAndValue[0],keyAndValue[1])
                return json
            }else{
                return JSONObject()
            }
        }
    }


    /**
     * 对于框架内部的销毁
     */
    private fun destory(){
        if (mContext!=null){
            mContext=null
        }
    }
}