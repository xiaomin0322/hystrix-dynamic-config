package com.netflix.hystrix.util;

import com.netflix.hystrix.vo.HystrixCollapserVo;
import com.netflix.hystrix.vo.HystrixCommandVo;
import com.netflix.hystrix.vo.HystrixPropertyVo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * User: Clement
 * PACKAGE_NAME: util
 * MONTH_NAME_SHORT: 十二月
 * DAY_NAME_SHORT: 星期三
 * PROJECT_NAME: demo-hystrix-confg
 * TIME: 21:39
 */
public class ZkUtils {

    private static Logger logger = LoggerFactory.getLogger(ZkUtils.class);

    /**
     * 通用公共类
     * @param propertyVoList
     * @param commandVo
     * @return
     */
    public static boolean configZk(List<HystrixPropertyVo> propertyVoList, HystrixCommandVo commandVo){
        System.err.println("size  : " +propertyVoList.size());
        String commandZkName = "hystrix.command.";
        String commandKeyZkName = commandZkName + commandVo.getCommandKey();
        if (CollectionUtils.isEmpty(propertyVoList)){
            return  false;
        }
        try {
            for(HystrixPropertyVo p: propertyVoList){
                String name = p.getName();
                String value = p.getValue();
                String commandZkStoreKey = commandKeyZkName+"."+name;
                commandZkStoreKey  = ZookeeperConfig.zkConfigRootPath + "/"
                        + commandZkStoreKey;
                System.out.println(commandZkStoreKey);
                HystrixZKClient.appendEphemeralNode(commandZkStoreKey,value);
            }
        } catch (Exception e) {
            logger.info( "error : code error "+ e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 注册ThreadPoolProperties
     * @param propertyVoList
     * @param commandVo
     * @return
     */
    public static  boolean configThreadPoolPropertiesZk(List<HystrixPropertyVo> propertyVoList, HystrixCommandVo commandVo){
        String threadPoolZkName = "hystrix.thread.";

        String threadPoolZkKey = threadPoolZkName + commandVo.getCommandKey();
        if (CollectionUtils.sizeIsEmpty(propertyVoList)){
            return  false;
        }
        propertyVoList.stream().forEach(hystrixPropertyVo -> {
            String name = hystrixPropertyVo.getName();
            String value = hystrixPropertyVo.getValue();
            String commandZkStoreKey = threadPoolZkKey+"."+name;
            String zkKey = ZookeeperConfig.zkConfigRootPath + "/"
                    + commandZkStoreKey;
            try {
                HystrixZKClient.appendEphemeralNode(zkKey,value);
            } catch (Exception e) {
                logger.info( "error : code error "+ e.getMessage());
                e.printStackTrace();
            }
        });
        return true;
    }


    /**
     *
     * @param propertyVoList
     * @param collapserVo
     * @return
     */
    public static  boolean configHystrixCollapserZk(List<HystrixPropertyVo> propertyVoList, HystrixCollapserVo collapserVo){
        String collapserZkName = "hystrix.collapser.";
        String collapserZKKey = collapserZkName + collapserVo.getBatchMethod();
        if (CollectionUtils.sizeIsEmpty(propertyVoList)){
            return  false;
        }

        propertyVoList.stream().forEach(hystrixPropertyVo -> {
            String name = hystrixPropertyVo.getName();
            String value = hystrixPropertyVo.getValue();
            String collapserZkStoreKey = collapserZKKey+"."+name;
            String zkKey = ZookeeperConfig.zkConfigRootPath + "/"
                    + collapserZkStoreKey;
            try {
                HystrixZKClient.appendEphemeralNode(zkKey,value);
            } catch (Exception e) {
                logger.info( "error : code error "+ e.getMessage());
                e.printStackTrace();
            }
        });

        return true;
    }
}
