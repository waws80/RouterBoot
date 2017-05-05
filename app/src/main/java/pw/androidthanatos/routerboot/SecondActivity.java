package pw.androidthanatos.routerboot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ConcurrentHashMap;

import pw.androidthanatos.library.kotlin.annotation.Alias;
import pw.androidthanatos.library.kotlin.annotation.Receiver;

@Alias("second")
public class SecondActivity extends AppCompatActivity {

    private static String hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ((TextView)findViewById(R.id.tv)).setText(hello);

    }


    /**
     * 要接收控制器发到当前页面的数据 方法 必须使用 @Receiver 注解
     * 方法限制：  只能有一个 ConcurrentHashMap 参数 或者无參
     * init 方法总是早于activity的生命周期方法，所以在init方法中不可以使用ui操作
     * @param map  通过控制器传过来的值
     */
    @Receiver
    private void init(ConcurrentHashMap map){
        String hello1 = (String) map.get("hello");
        hello=hello1;
        Log.w("secondActivity", "init: "+hello1 );

    }
}
