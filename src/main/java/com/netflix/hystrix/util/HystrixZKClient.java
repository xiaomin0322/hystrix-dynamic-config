package com.netflix.hystrix.util;


public class HystrixZKClient {

	public static final String ROOTPATH = ZookeeperConfig.zkConfigRootPath;
	
	public static ZkServer zkServer = new ZkServerImpl();
	private static final String hosts = "localhost:2181";
	static{
		try {
			zkServer.init(hosts);
			Thread.sleep(3000);
			appendPresistentNode(ROOTPATH, ROOTPATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void appendEphemeralNode(String path, String date) throws Exception {

		if (!zkServer.exist(path)) {
			zkServer.appendEphemeralNode(path, date);
			System.out.println("成功创建[" + path + "]节点!");
		}
		
	}
	
	public static void appendPresistentNode(String path, String date) throws Exception {
		ZkServer zkServer = new ZkServerImpl();
		zkServer.init(hosts);
		
		if (!zkServer.exist(path)) {
			zkServer.appendPresistentNode(path, date);
			System.out.println("成功创建[" + path + "]节点!");
		}
		
	}

}