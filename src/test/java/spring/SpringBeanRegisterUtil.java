package spring;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tiger.demohystrixconfig.Configer;
import org.tiger.demohystrixconfig.test.MyException;
import org.tiger.demohystrixconfig.test.Service;

/**
 * springApplicationContext工具
 */
public class SpringBeanRegisterUtil {
    private static ApplicationContext context=new
            ClassPathXmlApplicationContext("classpath:spring/applicationConfig.xml");
    private static ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) context;
    private static BeanDefinitionRegistry beanDefinitionRegistry = (DefaultListableBeanFactory) configurableContext.getBeanFactory();
    
    
    static{
    	System.out.println("SpringBeanRegisterUtil>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
    }

    /**
     * 注册bean
     * @param beanId 所注册bean的id
     * @param className bean的className，
     *                     三种获取方式：1、直接书写，如：com.mvc.entity.User
     *                                   2、User.class.getName
     *                                   3.user.getClass().getName()
     */
    public static void registerBean(String beanId,String className) {
        // get the BeanDefinitionBuilder
        BeanDefinitionBuilder beanDefinitionBurinilder =
        BeanDefinitionBuilder.genericBeanDefinition(className);
        // get the BeanDefinition
        BeanDefinition beanDefinition=beanDefinitionBurinilder.getBeanDefinition();
        // register the bean
        beanDefinitionRegistry.registerBeanDefinition(beanId,beanDefinition);
    }

    
    /**
     * 注册bean
     * @param beanId 所注册bean的id
     * @param className bean的className，
     *                     三种获取方式：1、直接书写，如：com.mvc.entity.User
     *                                   2、User.class.getName
     *                                   3.user.getClass().getName()
     */
    public static void registerBean(BeanDefinition beanDefinition,String beanId) {
        // get the BeanDefinition
        // register the bean
        beanDefinitionRegistry.registerBeanDefinition(beanId,beanDefinition);
    }

    
    /**
     * 移除bean
     * @param beanId bean的id
     */
    public static void unregisterBean(String beanId){
        beanDefinitionRegistry.removeBeanDefinition(beanId);
    }

    /**
     * 获取bean
     * @param name bean的id
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
    }
    
    public static void main(String[] args) {
    	System.out.println("===============================================");
    	
    	Service service = SpringBeanRegisterUtil.getBean("hystrixCommandServiceImpl");
		System.out.println("===================="+service);
		
		registerBean("MyException", "org.tiger.demohystrixconfig.test.MyException");
		
		MyException myException = SpringBeanRegisterUtil.getBean("MyException");
		System.out.println("===================="+myException.hashCode()+"==========="+myException.config);

	
		 myException = SpringBeanRegisterUtil.getBean("MyException");
		System.out.println("===================="+myException.hashCode()+"==========="+myException.config);

		
		Configer.val = "1231aaaa231231231233";
		
		
		
		//unregisterBean("MyException");
		registerBean("MyException", "org.tiger.demohystrixconfig.test.MyException");
		
		
		myException = SpringBeanRegisterUtil.getBean("MyException");
		System.out.println("===================="+myException.hashCode()+"==========="+myException.config);
		
		
		
		
	}
}