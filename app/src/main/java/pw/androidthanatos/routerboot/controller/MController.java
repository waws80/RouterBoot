package pw.androidthanatos.routerboot.controller;

import android.util.Log;

import pw.androidthanatos.library.kotlin.annotation.Controller;
import pw.androidthanatos.library.kotlin.modelandview.ModelAndView;
import pw.androidthanatos.library.kotlin.request.Request;
import pw.androidthanatos.library.kotlin.annotation.RequestMapping;
import pw.androidthanatos.library.kotlin.request.RequestMethod;

/**
 * Created  on 2017/5/2.
 *
 * @author liuxiongfei
 */
@Controller
public class MController {
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public void hello(Request request){
        Log.w("hehe","jinlaile");
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addModelObject("hello","我是RouterBoot").setView("second").go();
    }
    /**
     * @param request 包含了 请求的 url  query header body  参数
     *                当请求方式 为get时 body 为空  query 可能为空 header 可能为空
     *                当请求方式为post时 query 为空 header 可能为空  body 可能为空
     */
    @RequestMapping(value = "/hello1",method = RequestMethod.POST)
    public void hello1(Request request){
        /**
         * 跳转界面步骤：
         * 1。创建 ModelAndView
         * 2。添加 需要给目标界面的数据
         * 3。添加 目标界面（ 目标界面 必须使用@Alias(""xx) 注解 目标界面配置的别名 Alias）
         */
        ModelAndView modelAndView=new ModelAndView();
        /**
         * 启动的目标界面是 second 即 SecondActivity
         * 需要给目标界面 传的值的key 是 hello   值是 "我是RouterBoot"
         */
        modelAndView.addModelObject("hello","我是RouterBoot").setView("second").go();
    }
}
