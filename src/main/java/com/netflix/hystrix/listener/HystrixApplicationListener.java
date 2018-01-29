package com.netflix.hystrix.listener;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.vo.HystrixCollapserVo;
import com.netflix.hystrix.util.AopTargetUtils;
import com.netflix.hystrix.util.ZkUtils;
import com.netflix.hystrix.vo.HystrixCommandVo;
import com.netflix.hystrix.vo.HystrixPropertyVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesFactory;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;


@Component
public class HystrixApplicationListener implements
        ApplicationListener<ContextRefreshedEvent> {

    Logger logger = LoggerFactory.getLogger(HystrixApplicationListener.class);
    private static ConcurrentHashMap<Class<?>, String> defaultNameCache = new ConcurrentHashMap<Class<?>, String>();

    /* package */
    static String getDefaultNameFromClass(Class<?> cls) {
        String fromCache = defaultNameCache.get(cls);
        if (fromCache != null) {
            return fromCache;
        }
        // generate the default
        // default HystrixCommandKey to use if the method is not overridden
        String name = cls.getSimpleName();
        if (name.equals("")) {
            // we don't have a SimpleName (anonymous inner class) so use the full class name
            name = cls.getName();
            name = name.substring(name.lastIndexOf('.') + 1, name.length());
        }
        defaultNameCache.put(cls, name);
        return name;
    }

    private static HystrixCommandKey initCommandKey(final HystrixCommandKey fromConstructor, Class<?> clazz) {
        if (fromConstructor == null || fromConstructor.name().trim().equals("")) {
            final String keyName = getDefaultNameFromClass(clazz);
            return HystrixCommandKey.Factory.asKey(keyName);
        } else {
            return fromConstructor;
        }
    }

    private static HystrixCommandProperties initCommandProperties(HystrixCommandKey commandKey, HystrixPropertiesStrategy propertiesStrategy, HystrixCommandProperties.Setter commandPropertiesDefaults) {
        if (propertiesStrategy == null) {
            return HystrixPropertiesFactory.getCommandProperties(commandKey, commandPropertiesDefaults);
        } else {
            // used for unit testing
            return propertiesStrategy.getCommandProperties(commandKey, commandPropertiesDefaults);
        }
    }


    /**
     * 表示所属的group，threadKey 一个group共用线程池 默认类名(简称) HystrixCommandServiceImpl，线程池用的是类名，(最好自定义)group，threadKey以免有冲突
     * 类名最好是，全包名+类名，这样就不会有冲突，可以在动态配置archaius包一层，将每个类的配置做一个文件夹存放早zk，方便管理，不会冲突
     *
     * @param
     * @param cb
     * @return
     */

    private String getHystrixGroupKey(Class<?> objClass,
                                      com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand cb) {
        String name = cb.groupKey().length() == 0 ? cb.commandKey() : cb.groupKey();
        //return name.length() == 0 ? joinPoint.getSignature().toShortString() : name;
        return name.length() == 0 ? objClass.getSimpleName() : name;
    }

    /**
     * 默认值：当前执行方法名简称 如:get,信号量是以方法名简称为单位。。(最好自定义)commandKey以免有冲突
     * 方法名称最好是，全包名+类名+方法名称，这样就不会有冲突，可以在动态配置archaius包一层，将每个类的配置做一个文件夹存放早zk，方便管理，不会冲突
     *
     * @param
     * @param cb
     * @return
     */
    private String getHystrixcommandKey(Method method,
                                        com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand cb) {
        String name = method.getName();
        return name;
    }

    /**
     *
     * @param method
     * @param collapser
     * @return
     */
    private String getHystrixCollapserKey(Method method,HystrixCollapser collapser){
        String name = method.getName();
        return name;
    }


    public void onApplicationEvent(ContextRefreshedEvent event) {
        // logger.debug("------初始化执行----");
        System.out.println("------初始化执行----");
        try {
            // 获取上下文
            ApplicationContext context = event.getApplicationContext();
            // 获取所有beanNames
            String[] beanNames = context.getBeanNamesForType(Object.class);
            for (String beanName : beanNames) {
                Class<?> objClass = AopTargetUtils.getTarget(
                        context.getBean(beanName)).getClass();
                Method[] methods = objClass.getDeclaredMethods();
                for (Method method : methods) {
                    HystrixCommand hystrixCommand = method
                            .getAnnotation(HystrixCommand.class);

                    HystrixCollapser hystrixCollapser
                            = method.getAnnotation(HystrixCollapser.class);

                    if (hystrixCommand == null) {
                        continue;
                    }
                    if (hystrixCollapser == null) {
                        continue;
                    }
                    HystrixCollapserVo collapserVo =
                            new HystrixCollapserVo(hystrixCollapser);

                    HystrixCommandVo commandVo = new HystrixCommandVo(
                            hystrixCommand);
                    commandVo.setMethodName(method.getName());
                    collapserVo.setMethodName(method.getName());
                    commandVo.setClassName(objClass.getSimpleName());
                    collapserVo.setClassName(objClass.getSimpleName());
                    commandVo.setPackageName(objClass.getPackage().getName());
                    collapserVo.setPackageName(objClass.getPackage().getName());
                    commandVo.setProjectName(Class.class.getClass()
                            .getResource("/").getPath());
                    collapserVo.setPackageName(Class.class.getClass()
                            .getResource("/").getPath());
                    commandVo.setServiceIp(InetAddress.getLocalHost()
                            .getHostAddress());
                    collapserVo.setServiceIp(InetAddress.getLocalHost()
                            .getHostAddress());
                    commandVo.setClassNameAll(objClass.getName());
                    collapserVo.setClassNameAll(objClass.getName());

                    String  batchMethod = getHystrixCollapserKey(method,hystrixCollapser);
                    if (StringUtils.isBlank(batchMethod)){
                        collapserVo.setBatchMethod(batchMethod);
                    }

                    String commandKey = getHystrixcommandKey(method, hystrixCommand);
                    if (StringUtils.isBlank(commandVo.getCommandKey())) {
                        commandVo.setCommandKey(commandKey);
                    }

                    String gropyKey = getHystrixGroupKey(objClass, hystrixCommand);
                    if (StringUtils.isBlank(commandVo.getGroupKey())) {
                        commandVo.setGroupKey(gropyKey);
                    }

                    String threadKey = getHystrixGroupKey(objClass, hystrixCommand);
                    if (StringUtils.isBlank(commandVo.getThreadPoolKey())) {
                        commandVo.setThreadPoolKey(threadKey);
                    }
                    List<HystrixPropertyVo> commadnProperties
                            = commandVo.getCommandProperties();
                    if (!ZkUtils.configZk(commadnProperties, commandVo)) {
                        logger.info("error: code error run config commandProperties");
                    }

                    List<HystrixPropertyVo> threadProperties = commandVo.getThreadPoolProperties();
                    if (!ZkUtils.configThreadPoolPropertiesZk(threadProperties, commandVo)) {
                        logger.info("error: code error run config threadProperties");
                    }
                    List<HystrixPropertyVo> collapserProperties  = collapserVo.getCollapserProperties();
                    if (!ZkUtils.configHystrixCollapserZk(collapserProperties, collapserVo)) {
                        logger.info("error: code error run config HystrixCollapser");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}