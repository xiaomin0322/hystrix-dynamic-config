package org.tiger.demohystrixconfig.test;

import java.util.concurrent.Future;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import org.tiger.demohystrixconfig.MyException;
import org.tiger.demohystrixconfig.MyRuntimeException;
import rx.Observable;
import rx.Subscriber;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

@org.springframework.stereotype.Service("hystrixCommandServiceImpl")
public class HystrixCommandServiceImpl implements Service {

	public static final int TEST_TIMEOUT = 300;

	@HystrixCommand(
			commandProperties={ @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
					@HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "2")
					}
			)
	public String get(String str) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("str====="+str);
		return str;
	}
	
	@HystrixCommand(
			commandProperties={  @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD") },
			
			threadPoolProperties = { @HystrixProperty(name = "coreSize", value = "5"),
			@HystrixProperty(name = "maxQueueSize", value = "20") })
	public String get2(String str) {
		return str;
	}

	@HystrixCommand
	public String throwException() throws MyException {
		throw new MyException();
	}

	/**
	 * execution.isolation.thread.timeoutInMilliseconds
	 * 命令执行超时时间，默认1000ms
	 * @param str
	 * @return
	 */
	@HystrixCommand(commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = TEST_TIMEOUT + "") })
	public String withTimeout(String str) {
		try {
			//Thread.sleep(2 * TEST_TIMEOUT);
			System.out.println("==========="+str);
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * execution.isolation.thread.interruptOnTimeout
	 * 执行是否启用超时，默认启用true
	 * @param
	 * @return
	 */

	@HystrixCommand(commandProperties = {
		@HystrixProperty(name="execution.isolation.thread.interruptOnTimeout",value="false")
	})
	public String interruptOnTimeout(String  string) {
		try{
			System.err.println("String :" + string);
			Thread.sleep(2000);
		}catch (InterruptedException e){
			e.printStackTrace();
		}
		return string;
	}

	/**
	 * execution.isolation.thread.interruptOnTimeout
	 * 不启用超时
	 * false
	 * @return
	 */
	@HystrixCommand(commandProperties = {
			@HystrixProperty(name="execution.isolation.thread.interruptOnTimeout",value = "false")
	})
	public String interruptOnTimeoutFalse(String string){
		try{
			System.err.println("String  :" +string);
			Thread.sleep(2000);
		}catch (InterruptedException e){
			e.printStackTrace();
		}
		return string;
	}

	/**
	 *
	 * @param string
	 * @return
	 */
	@HystrixCommand(fallbackMethod = "fallbackMethod",
			commandProperties =
			{ @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests",
					value = "50") })
	public String maxConcurrentRequests(String string) {
		return string;
	}

	/**
	 * 如果并发数达到该设置值，请求会被拒绝和抛出异常并且fallback不会被调用。默认10
	 * @param string
	 * @return
	 */
	@HystrixCommand(fallbackMethod = "fallbackMethod",
		commandProperties = {
			@HystrixProperty(name = "fallback.isolation.semaphore.maxConcurrentRequests",
			value="10")
		})
	public String fallbackMaxConcurrentRequests(String string) {
		return string;
	}

	/**
	 * 当执行失败或者请求被拒绝，是否会尝试调用hystrixCommand.getFallback() 。默认true
	 * @param string
	 * @return
	 */
	@HystrixCommand(commandProperties =
			{@HystrixProperty(name = "fallback.enabled",value="true")})
	public String enabled(String string) {
		return string;
	}

	/**
	 * 设置统计的时间窗口值的，毫秒值，circuit break 的打开会根据1个rolling window的统计来计算。
	 * 若rolling window被设为10000毫秒，
	 * 则rolling window会被分成n个buckets，每个bucket包含success，
	 * failure，timeout，rejection的次数的统计信息。默认10000
	 * @param string
	 * @return
	 */
	@HystrixCommand(commandProperties ={
			@HystrixProperty(name = "metrics.rollingStats.numBuckets",value = "10")
	})
	public String timeInMilliseconds(String string) {
		return string;
	}

	/**
	 * 用来跟踪circuit的健康性，如果未达标则让request短路。默认true
	 * @param string
	 * @return
	 */
	@HystrixCommand(commandProperties = {
			@HystrixProperty(name = "circuitBreaker.enabled",value = "true")
	})
	public String circuitBreakerEnable(String string) {
		return string;
	}

	/**
	 *	默认20
	 * @param string
	 * @return
	 */
	@HystrixCommand(commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "20")
	})
	public String requestVolumeThreshold(String string) {
		return string;
	}

    /**
     * 触发短路的时间值
     * @param string
     * @return
     */
	@HystrixCommand(commandProperties = {
	        @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value = "5000")
    })
    public String sleepWindowInMilliseconds(String string) {
        return string ;
    }

    /**
     * 强制打开熔断器，如果打开这个开关，那么拒绝所有request，默认false
     * @param string
     * @return
     */
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "circuitBreaker.forceOpen",value = "false")
    })
    public String forceOpen(String string) {
        return string;
    }

    /**
     * 强制关闭熔断器 如果这个开关打开，
     * circuit将一直关闭且忽略circuitBreaker.
     * errorThresholdPercentage
     * @param string
     * @return
     */
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "circuitBreaker.forceClosed",value = "false")
    })
    public String forceClosed(String string) {
        return string;
    }

    /**
     * @param string
     * @return
     */
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "metrics.rollingStats.numBuckets",value = "10")
    })
    public String numBuckets(String string) {
        return string;
    }

    /**
     * 执行时是否enable指标的计算和跟踪，默认true
     * @param string
     * @return
     */
    @HystrixCommand(
            commandProperties = {
                    @HystrixProperty(name = "metrics.rollingPercentile.enabled ",value = "10")
            }
    )
    public String rollingPercentileEabled(String string) {
        return string;
    }

    /**
     * 设置rolling percentile window的时间，默认60000
     * @param string
     * @return
     */
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name ="metrics.rollingPercentile.timeInMilliseconds",value = "60000")
    })
    public String rollingPercentileTimeInMilliseconds(String string) {
        return string;
    }

    /**
     * 设置rolling percentile window的numberBuckets。
     * @param string
     * @return
     */
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "metrics.rollingPercentile.numBuckets",value ="60000")
    })
    public String rollingPercentileNumBuckets(String string) {
        return string;
    }

    /**
     * 如果bucket size＝100，window＝10s，若这10s里有500次执行，
     * 只有最后100次执行会被统计到bucket里去。增加该值会增加内存开销以及排序的开销
     * @param string
     * @return
     */
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "metrics.rollingPercentile.bucketSize ",value = "100")
    })
    public String bucketSize(String string) {
        return string;
    }

	/**
	 *  单次批处理的最大请求数，
	 *  达到该数量触发批处理，
	 *  默认Integer.MAX_VALUE
     *  返回执行的方法就是本方法
     *  默认执行数量是Integer的最大数控
	 * @param string
	 * @return
	 */
	@HystrixCollapser(batchMethod = "maxRequestsInBatch",
    collapserProperties = {
	        @HystrixProperty(name = "maxRequestsInBatch",value=""+Integer.MAX_VALUE)
    })
	public String maxRequestsInBatch(String string) {
		return string ;
	}

    /**
     * 触发批处理的延迟，也可以为创建批处理的时间＋该值，默认10
     * @param string
     * @return
     */
    @HystrixCollapser(batchMethod = "timerDelayInMilliseconds",
    collapserProperties = {
            @HystrixProperty(name="timerDelayInMilliseconds",value="10")
    })
    public String timerDelayInMilliseconds(String string) {
        return string;
    }

    /**
     *
     * @param string
     * @return
     */
    @HystrixCollapser(batchMethod = "requestCacheEnabled",
    collapserProperties = {
            @HystrixProperty(name="requestCache.enabled",value="true")
    })
    public String requestCacheEnabled(String string) {
        return string;
    }

    /**
     * 线程池最大数量
     * @param string
     * @return
     */
    @HystrixCommand(threadPoolProperties = {
            @HystrixProperty(name = "coreSize",value = "10")
    })
    public String coreSize(String string) {
        return string;
    }

    /**
     * BlockingQueue的最大队列数
     * @param string
     * @return
     */
    @HystrixCommand(
            threadPoolProperties = {
                    @HystrixProperty(name = "maxQueueSize",
                    value="−1")
            }
    )
    public String blockingQueueSize(String string) {
        return string;
    }

    /**
     * maxQueueSize
     * @param string
     * @return
     */
    @HystrixCommand(threadPoolProperties = {
            @HystrixProperty(name = "queueSizeRejectionThreshold"
            ,value = "5")
    })
    public String queueSizeRejectionThreshold(String string) {
        return string;
    }

    /**
     * 果corePoolSize和maxPoolSize设成一样（默认实现）该设置无效
     * @param string
     * @return
     */
    @HystrixCommand(threadPoolProperties = {
            @HystrixProperty(name = "keepAliveTimeMinutes ",value="1")
    })
    public String keepAliveTimeMinutes(String string) {
        return string;
    }

    /**
     *
     * @param string
     * @return
     */
    @HystrixCommand(threadPoolProperties = {
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds",
            value="10000")

    })
    public String threadTimeInMilliseconds(String string) {
        return string;
    }

    /**
     *
     * @param string
     * @return
     */
    @HystrixCommand(threadPoolProperties = {
            @HystrixProperty(name = "metrics.rollingStats.numBuckets"
            ,value = "10")
    })
    public String threadNumBuckets(String string) {
        return null;
    }


    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "metrics.healthSnapshot.intervalInMilliseconds",value = "500")
    })
    public String intervalInMilliseconds(String string) {
        return string;
    }


    /**
	 * execution.isolation.thread.timeoutInMilliseconds
	 * @param str
	 * @return
	 */
	@HystrixCommand(commandProperties = { @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "0") })
	public String withZeroTimeout(String str) {
		try {
			Thread.sleep(2 * TEST_TIMEOUT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * execution.isolation.strategy
	 * 隔离策略，默认是Thread, 可选Thread｜Semaphore
	 *
	 * @return
	 */
	 
	// executionIsolationStrategy
	@HystrixCommand(commandProperties = { @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD") })
	public int getThreadId() {
		return Thread.currentThread().hashCode();
	}

	/**
	 *
	 * 隔离策略，默认是Thread, 可选Thread｜Semaphore
	 * @return
	 */
	 
	@HystrixCommand(commandProperties = { @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE") })
	public int getNonThreadedThreadThreadId() {
		return Thread.currentThread().hashCode();
	}

	 
	//@HystrixCommand(/*fallbackMethod = "fallback"*//*,ignoreExceptions={MyRuntimeException.class}*/)
	@HystrixCommand(
			fallbackMethod = "fallback",
			commandProperties={
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "4"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")})
	public String exceptionWithFallback(String s) {
		System.out.println("exceptionWithFallback>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		throw new MyRuntimeException();
	}

	public String fallback(String s) {
		return s;
	}

	 
	@HystrixCommand(fallbackMethod = "fallbackWithException"/*,ignoreExceptions={MyRuntimeException.class}*/)
	public Throwable exceptionWithFallbackIncludingException(String testStr) {
		throw new MyRuntimeException();
	}

	public Throwable fallbackWithException(String testStr, Throwable t) {
		return t;
	}

	 
	@HystrixCommand
	public Future<String> getFuture(final String str) {
		return new AsyncResult<String>() {
			 
			public String invoke() {
				return str;
			}
		};
	}

	 
	@HystrixCommand
	public Observable<String> getObservable(final String str) {
		 return Observable.create(new Observable.OnSubscribe<String>() {
			 
			public void call(Subscriber<? super String> observer) {
				 try {
                     if (!observer.isUnsubscribed()) {
                         observer.onNext(str);
                         observer.onCompleted();
                     }		
                 } catch (Exception e) {
                     observer.onError(e);
                 }
				
			}
         }); 
	}
}
