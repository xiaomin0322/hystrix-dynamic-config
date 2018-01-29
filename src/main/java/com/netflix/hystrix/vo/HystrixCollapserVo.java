package com.netflix.hystrix.vo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * PACKAGE_NAME: org.tiger.demohystrixconfig.test
 * USER :  Administrator
 * DATE :  2018/1/29
 */

public class HystrixCollapserVo {

    //方法名称
    private String methodName = null;
    //类名称
    private String className = null;
    //包名称
    private String packageName = null;
    //项目名称
    private String projectName = null;
    //服务ip
    private String serviceIp = null;
    //class全包名
    private String classNameAll = null;

    private String batchMethod = null;

    private String collapserKey = null;



    private List<HystrixPropertyVo> collapserProperties = null;

    public HystrixCollapserVo(){

    }

    public HystrixCollapserVo(HystrixCollapser hystrixCollapser){
        String collapserKey = hystrixCollapser.collapserKey();
        this.collapserKey = collapserKey ;
        String batchMethod = hystrixCollapser.batchMethod();
        this.batchMethod = batchMethod;
        HystrixProperty[] collapserProperties = hystrixCollapser.collapserProperties();

        if (collapserProperties!=null){
            this.collapserProperties = new ArrayList<HystrixPropertyVo>();
            for(HystrixProperty h:collapserProperties){
                HystrixPropertyVo hystrixProperty = new HystrixPropertyVo();
                hystrixProperty.setName(h.name());
                hystrixProperty.setValue(h.value());
                this.collapserProperties.add(hystrixProperty);
            }
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getServiceIp() {
        return serviceIp;
    }

    public void setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp;
    }

    public String getClassNameAll() {
        return classNameAll;
    }

    public void setClassNameAll(String classNameAll) {
        this.classNameAll = classNameAll;
    }

    public String getBatchMethod() {
        return batchMethod;
    }

    public void setBatchMethod(String batchMethod) {
        this.batchMethod = batchMethod;
    }

    public List<HystrixPropertyVo> getCollapserProperties() {
        return collapserProperties;
    }

    public void setCollapserProperties(List<HystrixPropertyVo> collapserProperties) {
        this.collapserProperties = collapserProperties;
    }
    public String getCollapserKey() {
        return collapserKey;
    }

    public void setCollapserKey(String collapserKey) {
        this.collapserKey = collapserKey;
    }
}
