package org.tiger.demohystrixconfig;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.Application;

import com.netflix.config.DynamicPropertyFactory;

public class Test {
	 private static final Logger logger = LoggerFactory.getLogger(Test.class);
	
	public static void main(String[] args)throws Exception {
		Application.startZookeeper();
		int i = 0;
		for(;;){
			test(i);
			i++;
			Thread.sleep(5000);
		}
		
	}
	
	
	public static void test(int i){
		
		try{
		//HelloCommand command = new HelloCommand(""+i);
		  //节点信息
      String nodeProperty = DynamicPropertyFactory.getInstance()
              .getStringProperty("config", "<none>")
              .get();
      
      System.out.println("nodeProperty:"+nodeProperty);

      // before this set hystrix.command.HystrixCommandKey.execution.isolation.thread.timeoutInMilliseconds filed
      // ExampleKey is HystrixCommandKey singleton
      String dynamicProperty = DynamicPropertyFactory.getInstance()
              .getStringProperty("hystrix.command.ExampleKey.execution.isolation.thread.timeoutInMilliseconds",
                      "<none>")
              .get();
      System.out.println("dynamicProperty:"+dynamicProperty);

      logger.info(" config node property:{},dynamicProperty:{}" ,nodeProperty,dynamicProperty);


      Future<String> future = new HelloCommand(dynamicProperty).queue();
      future.get();
      
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
