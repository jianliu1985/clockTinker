package com.alibaba.jvm.sandbox.clock;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ProcessControlException;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.filter.NameRegexFilter;
import com.alibaba.jvm.sandbox.api.http.Http;
import com.alibaba.jvm.sandbox.api.listener.EventListener;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;

import javax.annotation.Resource;

/**
 * Created by liujian.lj on 2017/12/7.
 */
@Information(id = "broken-clock-tinker" ,version = "0.0.1", author = "liujian.lj@alibaba-inc.com")
public class BrokenClockTinkerModule implements Module {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    // 日期格式化
    private final java.text.SimpleDateFormat clockDateFormat
            = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Http("/repairCheckState")
    public void repairCheckState() {

        moduleEventWatcher.watch(

                // 匹配到Clock$BrokenClock#checkState()
                new NameRegexFilter("cn\\.shopee\\.qa\\.BrokenClock", "checkState"),

                // 监听THROWS事件并且改变原有方法抛出异常为正常返回
                new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Throwable {

                        // 立即返回
                        ProcessControlException.throwReturnImmediately(null);
                    }
                },

                // 指定监听的事件为抛出异常
                Event.Type.THROWS
        );

    }

    @Http("/getArgumentCheckState")
    public void getArgumentCheckState() {

        moduleEventWatcher.watch(

                // 匹配到Clock$BrokenClock#checkState()
                new NameRegexFilter("cn\\.shopee\\.qa\\.BrokenClock", "checkState"),

                // 监听THROWS事件并且改变原有方法抛出异常为正常返回
                new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Throwable {
                        BeforeEvent beforeEvent = (BeforeEvent)event;
                        Object[] argumentArray = beforeEvent.argumentArray;
                        int j=(Integer)argumentArray[0];
                        if(j%2==0){
                            j=j*100;
                        }
                        argumentArray[0]=j;
                        // 立即返回
//                        ProcessControlException.throwReturnImmediately(null);
                    }
                },

                // 指定监听的事件为抛出异常
                Event.Type.BEFORE
        );

    }

    @Http("/setReturnreport")
    public void setReturnreport() {

        moduleEventWatcher.watch(

                // 匹配到Clock$BrokenClock#checkState()
                new NameRegexFilter("cn\\.shopee\\.qa\\.Clock", "report"),

                // 监听THROWS事件并且改变原有方法抛出异常为正常返回
                new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Throwable {


//                        ReturnEvent returnRes = (ReturnEvent)event;


                        String res =  new String("hello wolrd "+clockDateFormat.format(new java.util.Date()));
                        // 立即返回

                        ProcessControlException.throwReturnImmediately( res);
                    }
                },

                // 指定监听的事件为
                Event.Type.RETURN
        );

    }

    @Http("/repairDelay")
    public void repairDelay() {

        moduleEventWatcher.watch(

                // 匹配到Clock$BrokenClock#checkState()
                new NameRegexFilter("cn\\.shopee\\.qa\\.BrokenClock", "delay"),

                // 监听THROWS事件并且改变原有方法抛出异常为正常返回
                new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Throwable {

//                        BeforeEvent beforeEvent = (BeforeEvent)event;
//                        Object[] argumentArray = beforeEvent.argumentArray;
//                        System.out.println(argumentArray.length);
//                        for(int i=0;i<argumentArray.length;i++) {
//                            System.out.println("hello"+argumentArray[i].toString());
//                        }

                        // 在这里延时1s
                        Thread.sleep(1000L);

                        // 然后立即返回，因为监听的是BEFORE事件，所以此时立即返回，方法体将不会被执行
                        ProcessControlException.throwReturnImmediately(null);
                    }
                },

                // 指定监听的事件为方法执行前
                Event.Type.BEFORE

        );

    }

}
