package com.netflix.hystrix.vo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HystrixCommandVo {
	
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
	
	private String commandKey = null;
	
	private String groupKey = null;
	
	private String fallbackMethod = null;
	
	private String threadPoolKey = null;
	  
	private List<HystrixPropertyVo> threadPoolProperties = null;  
	
	private List<HystrixPropertyVo> commandProperties = null;
	
	private List<Class<? extends Throwable>> ignoreExceptions = null;
	
	public HystrixCommandVo(){}
	
	
	public HystrixCommandVo(com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand hystrixCommand){
		String commandKey = hystrixCommand.commandKey();
		this.commandKey = commandKey;
		
		String groupKey = hystrixCommand.groupKey();
		this.groupKey = groupKey;
		
		String fallbackMethod = hystrixCommand.fallbackMethod();
		this.fallbackMethod = fallbackMethod;
		
		String threadPoolKey = hystrixCommand.threadPoolKey();
		this.threadPoolKey = threadPoolKey;
		  
		HystrixProperty[] threadPoolProperties = hystrixCommand.threadPoolProperties();  
		if(threadPoolProperties!=null){
			this.threadPoolProperties = new ArrayList<HystrixPropertyVo>();
			for(HystrixProperty h:threadPoolProperties){
				HystrixPropertyVo hystrixProperty = new HystrixPropertyVo();
				hystrixProperty.setName(h.name());
				hystrixProperty.setValue(h.value());
				this.threadPoolProperties.add(hystrixProperty);
			}
		}
		HystrixProperty[] commandProperties =hystrixCommand.commandProperties();
		if(commandProperties!=null){
			this.commandProperties = new ArrayList<HystrixPropertyVo>();
			for(HystrixProperty h:commandProperties){
				HystrixPropertyVo hystrixProperty = new HystrixPropertyVo();
				hystrixProperty.setName(h.name());
				hystrixProperty.setValue(h.value());
				this.commandProperties.add(hystrixProperty);
			}
		}

	    Class<? extends Throwable>[] ignoreExceptions = hystrixCommand.ignoreExceptions();
	    if(ignoreExceptions!=null){
	    	this.ignoreExceptions = Arrays.asList(ignoreExceptions);
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


	public String getCommandKey() {
		return commandKey;
	}


	public void setCommandKey(String commandKey) {
		this.commandKey = commandKey;
	}


	public String getGroupKey() {
		return groupKey;
	}


	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}


	public String getFallbackMethod() {
		return fallbackMethod;
	}


	public void setFallbackMethod(String fallbackMethod) {
		this.fallbackMethod = fallbackMethod;
	}


	public String getThreadPoolKey() {
		return threadPoolKey;
	}


	public void setThreadPoolKey(String threadPoolKey) {
		this.threadPoolKey = threadPoolKey;
	}


	public List<HystrixPropertyVo> getThreadPoolProperties() {
		return threadPoolProperties;
	}


	public void setThreadPoolProperties(
			List<HystrixPropertyVo> threadPoolProperties) {
		this.threadPoolProperties = threadPoolProperties;
	}


	public List<HystrixPropertyVo> getCommandProperties() {
		return commandProperties;
	}


	public void setCommandProperties(
			List<HystrixPropertyVo> commandProperties) {
		this.commandProperties = commandProperties;
	}


	public List<Class<? extends Throwable>> getIgnoreExceptions() {
		return ignoreExceptions;
	}


	public void setIgnoreExceptions(
			List<Class<? extends Throwable>> ignoreExceptions) {
		this.ignoreExceptions = ignoreExceptions;
	}


	public String getClassNameAll() {
		return classNameAll;
	}


	public void setClassNameAll(String classNameAll) {
		this.classNameAll = classNameAll;
	}


	@Override
	public String toString() {
		return "HystrixCommandVo [methodName=" + methodName + ", className="
				+ className + ", packageName=" + packageName + ", projectName="
				+ projectName + ", serviceIp=" + serviceIp + ", classNameAll="
				+ classNameAll + ", commandKey=" + commandKey + ", groupKey="
				+ groupKey + ", fallbackMethod=" + fallbackMethod
				+ ", threadPoolKey=" + threadPoolKey
				+ ", threadPoolProperties=" + threadPoolProperties
				+ ", commandProperties=" + commandProperties
				+ ", ignoreExceptions=" + ignoreExceptions + "]";
	}



	

}
