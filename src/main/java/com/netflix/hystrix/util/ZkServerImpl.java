package com.netflix.hystrix.util;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

/**
 * @author zhanglei
 * @date 2014-12-24
 */ 
public class ZkServerImpl implements ZkServer, Watcher{

	private CountDownLatch connectedSemaphore = new CountDownLatch( 1 );   
	
 private ZooKeeper zk=null;
 
 /**
  * 
  * 创建一个给定的目录节点 path, 并给它设置数据，
  */
 public void create(String path, byte data[], List<ACL> acl, CreateMode createMode)
   throws KeeperException, InterruptedException {
  if(zk!=null){
   zk.create(path, data, acl, createMode);  
  }  
 }
 
 
 /**
  * 创建持久化目录节点；
  */
 public void appendPresistentNode(String path, String data)
   throws KeeperException, InterruptedException {
  if(zk!=null){
   zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);  
  }
 }
 
 /**
  * 创建持久化顺序目录节点
  */
 public void appendPresistentSequentialNode(String path, String data)
   throws KeeperException, InterruptedException {
  if(zk!=null){
   zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);  
  }
 }

 /**
  * 创建临时目录节点 
  */
 public void appendEphemeralNode(String path, String data)
   throws KeeperException, InterruptedException {
  if(zk!=null){
   zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
  }  
 }
 
 /**
  * 创建临时顺序目录节点 
  */
 public void appendEphemeralSequentialNode(String path, String data)
   throws KeeperException, InterruptedException {
  if(zk!=null){
   zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
  }  
 }

 
 
 /**
  * 删除指定节点上的数据
  */
 public void delNode(String path, int version) throws KeeperException,
   InterruptedException {
  if (zk != null) {  
            zk.delete(path, version);  
        } 
 }

 /**
  * 关闭连接
  */
 public void destroy() throws InterruptedException {
  if(zk!=null){
   zk.close();
  }
 }

 /**
  * 判断指定目录节点是否存在
  */
 public boolean exist(String path) throws KeeperException,
   InterruptedException {
  if(zk!=null){
   return zk.exists(path, true)!=null;
  }
        return false;
 }
 

 /**
  * 获取某个节点下的所有子节点 
  */
 public List<String> getChildren(String path) throws KeeperException,
   InterruptedException {
  if(zk!=null){
   return zk.getChildren(path, true);
  }
  return null;
 }
 
 /**
  * 获取某个节点下的所有子节点 
  */
 public List<String> getChildren(String path,Watcher watcher) throws KeeperException,
   InterruptedException {
  if(zk!=null){
   return zk.getChildren(path, watcher);
  }
  return null;
 }
 
 /**
  * 变更设置指定节点上的数据
  */
 public void setData(String path, String data, int version) throws KeeperException,
   InterruptedException {
  if(zk!=null){
   //修改节点下的数据，第三个参数为版本，如果是-1，那会无视被修改的数据版本，直接改掉
   zk.setData(path, data.getBytes(), version);
  }
 }

 /**
  * 获取某个znode上的数据     
  */
 public String getData(String path, Stat stat) throws KeeperException,
   InterruptedException {
  if(zk!=null){
   byte[] b=zk.getData(path, true, stat);
   return new String(b);
  }
        return null;
 }
 

 /**
  * 获取某个znode上的数据     
  */
 public String getData(String path,Watcher watcher, Stat stat) throws KeeperException,
   InterruptedException {
  if(zk!=null){
   byte[] b=zk.getData(path, watcher, stat);
   return new String(b);
  }
        return null;
 }

 /**
  * 初始化zookeeper服务地址
  */
 public void init(String hosts) throws IOException {
  zk=new ZooKeeper(hosts, 50000, this);
  try {
	connectedSemaphore.await();
} catch (InterruptedException e) {
	e.printStackTrace();
}  
 }

 


/*public void process(WatchedEvent event) {
  if(event.getType()==EventType.NodeDataChanged){ //节点数据发生变化触发一下事件
   if(event.getPath()!=null){
    try{
     System.out.println("监控到["+event.getPath()+"]发生变化，value="+this.getData(event.getPath(), new Stat()));
    } catch (KeeperException e) {
     e.printStackTrace();
    } catch (InterruptedException e) {
     e.printStackTrace();
    } 
    
   }
  }
 } */
 
 
 public void process(WatchedEvent event) {
	   // TODO Auto-generated method stub
	   if (event.getState() == KeeperState.SyncConnected) {
	      System.out.println("watcher received event");
	      connectedSemaphore.countDown();
	      return;
	   }
	}
}