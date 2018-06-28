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

    // ���ڸ�ʽ��
    private final java.text.SimpleDateFormat clockDateFormat
            = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Http("/repairCheckState")
    public void repairCheckState() {

        moduleEventWatcher.watch(

                // ƥ�䵽Clock$BrokenClock#checkState()
                new NameRegexFilter("cn\\.shopee\\.qa\\.BrokenClock", "checkState"),

                // ����THROWS�¼����Ҹı�ԭ�з����׳��쳣Ϊ��������
                new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Throwable {

                        // ��������
                        ProcessControlException.throwReturnImmediately(null);
                    }
                },

                // ָ���������¼�Ϊ�׳��쳣
                Event.Type.THROWS
        );

    }

    @Http("/getArgumentCheckState")
    public void getArgumentCheckState() {

        moduleEventWatcher.watch(

                // ƥ�䵽Clock$BrokenClock#checkState()
                new NameRegexFilter("cn\\.shopee\\.qa\\.BrokenClock", "checkState"),

                // ����THROWS�¼����Ҹı�ԭ�з����׳��쳣Ϊ��������
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
                        // ��������
//                        ProcessControlException.throwReturnImmediately(null);
                    }
                },

                // ָ���������¼�Ϊ�׳��쳣
                Event.Type.BEFORE
        );

    }

    @Http("/setReturnreport")
    public void setReturnreport() {

        moduleEventWatcher.watch(

                // ƥ�䵽Clock$BrokenClock#checkState()
                new NameRegexFilter("cn\\.shopee\\.qa\\.Clock", "report"),

                // ����THROWS�¼����Ҹı�ԭ�з����׳��쳣Ϊ��������
                new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Throwable {


//                        ReturnEvent returnRes = (ReturnEvent)event;


                        String res =  new String("hello wolrd "+clockDateFormat.format(new java.util.Date()));
                        // ��������

                        ProcessControlException.throwReturnImmediately( res);
                    }
                },

                // ָ���������¼�Ϊ
                Event.Type.RETURN
        );

    }

    @Http("/repairDelay")
    public void repairDelay() {

        moduleEventWatcher.watch(

                // ƥ�䵽Clock$BrokenClock#checkState()
                new NameRegexFilter("cn\\.shopee\\.qa\\.BrokenClock", "delay"),

                // ����THROWS�¼����Ҹı�ԭ�з����׳��쳣Ϊ��������
                new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Throwable {

//                        BeforeEvent beforeEvent = (BeforeEvent)event;
//                        Object[] argumentArray = beforeEvent.argumentArray;
//                        System.out.println(argumentArray.length);
//                        for(int i=0;i<argumentArray.length;i++) {
//                            System.out.println("hello"+argumentArray[i].toString());
//                        }

                        // ��������ʱ1s
                        Thread.sleep(1000L);

                        // Ȼ���������أ���Ϊ��������BEFORE�¼������Դ�ʱ�������أ������彫���ᱻִ��
                        ProcessControlException.throwReturnImmediately(null);
                    }
                },

                // ָ���������¼�Ϊ����ִ��ǰ
                Event.Type.BEFORE

        );

    }

}
