package pw.androidthanatos.routerboot;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import pw.androidthanatos.library.kotlin.RouterBoot;
import pw.androidthanatos.routerboot.controller.MController;

/**
 * Created by liuxiongfei on 2017/5/2.
 * @author androidthanatos
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 第一步：
         * 初始化需要此框架管理的包名
         * （框架内部会自动扫描activity是否需要管理，
         * 如果需要管理只需要在activity类上添加别名注解 @Alias("main")）
         */
         List<String> pkgs=new ArrayList<>();
        pkgs.add(this.getPackageName());

        /**
         * 第二步：
         * 初始化控制器
         * 只需要将控制器类通过集合传入即可
         */
        List<Class<?>> classList=new ArrayList<>();
        classList.add(MController.class);
        /**
         * 初始化此路由框架
         * 第一个参数 上下文 推荐使用getApplicationContext()
         * 第二个参数 需要此框架管理的包的集合
         * 第三个参数 控制器集合
         * 第四个参数 baseUrl
         */
        RouterBoot.Companion.init(getApplicationContext(),pkgs,classList,"http://androidthanatos.pw");
    }
}
