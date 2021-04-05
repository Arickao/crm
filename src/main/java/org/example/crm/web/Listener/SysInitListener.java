package org.example.crm.web.Listener;

import org.example.crm.setting.domain.DicValue;
import org.example.crm.setting.service.DicService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class SysInitListener implements ApplicationListener<ContextRefreshedEvent> {

    /*@Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("==========================================================");
    }
    此段代码会在Tomcat启动时触发两次
    解决发法：
    https://www.bbsmax.com/A/VGzlBZ075b/
        以下代码来源于上面网站*/

    @Resource
    private DicService dicService;

    @Resource
    private ServletContext application;

    private volatile AtomicBoolean isInit = new AtomicBoolean(false);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //防止重复触发
        if(!isInit.compareAndSet(false,true)) {
            return;
        }
        init();
    }

    private void init() {
        // 开启任务
        Map<String, List<DicValue>> map = dicService.getAll();

        // 将map解析为全局作用域中保存的键值对
        Set<String> set = map.keySet();
        for(String key : set){
            application.setAttribute(key,map.get(key));
        }

        //---------------数据字典处理完毕后，需要解析Stage2Possibility.properties文件----------------------
        Map<String,String> pMap = new HashMap<>(16);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> keys = resourceBundle.getKeys();
        while (keys.hasMoreElements()){
            //阶段
            String key = keys.nextElement();
            //可能性
            String value = resourceBundle.getString(key);

            pMap.put(key,value);
        }

        //将pMap保存到服务器缓存中
        application.setAttribute("pMap",pMap);

    }


}
