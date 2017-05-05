package pw.androidthanatos.routerboot;

import android.content.Intent;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import pw.androidthanatos.library.kotlin.RouterBoot;
import pw.androidthanatos.library.kotlin.annotation.Alias;
import pw.androidthanatos.library.kotlin.annotation.Receiver;

@Alias("main")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void go(View view){
        /**
         * 需要通过路由进行跳转 使用如下：
         * RouterBoot 支持两种方式  get  post
         * 第一个参数是 配置的Baseurl + 控制器接口名 例如： ／main   等等。。
         * 第二个参数是 header
         * 第三个参数是 body
         * 执行此方法 生效的前提条件是：
         * 0。application 中进行过注册
         * 1。控制层包含  ／hello1  接口
         * 2。当前activity 使用了 @Alias("xx") 注解
         */
        RouterBoot.Companion.post("http://androidthanatos.pw/hello1",new JSONObject(),new JSONObject());
    }
}
