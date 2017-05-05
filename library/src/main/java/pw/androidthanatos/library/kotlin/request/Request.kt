package pw.androidthanatos.library.kotlin.request

import org.json.JSONObject
import java.lang.reflect.Method

/**
 * Created  on 2017/5/3.
 * RouterBoot请求体
 */
data class Request(var url:String,
                   var method: RequestMethod,
                   var query: JSONObject,
                   var header: JSONObject,
                   var body: JSONObject)


data class MethodInfo(var clazz: Class<Any>, var method: Method, var type: RequestMethod)