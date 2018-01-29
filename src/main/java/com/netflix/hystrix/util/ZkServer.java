package com.netflix.hystrix.util;
import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;


public interface ZkServer {
 
 /**
  * @param path
  * @param byte
  * @param ACL
  * @param CreateMode 标识有四种形式的目录节点,分别是
  * PERSISTENT：持久化目录节点，这个目录节点存储的数据不会丢失；
  * PERSISTENT_SEQUENTIAL：顺序自动编号的目录节点，这种目录节点会根据当前已近存在的节点数自动加 1，然后返回给客户端已经成功创建的目录节点名；
  * EPHEMERAL：临时目录节点，一旦创建这个节点的客户端与服务器端口会话失效，这种节点会被自动删除；
  * EPHEMERAL_SEQUENTIAL：临时自动编号节点
  * @throws InterruptedException 
  * @throws KeeperException 
  */
 public void create(String path, byte data[], List<ACL> acl, CreateMode createMode) throws KeeperException, InterruptedException;
  
 /**
  * 
  * @param address
  * @description 初始化zookeeper服务地址，如hosts="192.168.131.4:2181,192.168.131.3:2181"
  * @throws IOException
  */
    public void init(String hosts) throws IOException;
    
    
    /**
     * @description 关闭连接
     * @throws InterruptedException
     */
    public void destroy() throws InterruptedException;
    
 /**
     * 
     * @param path
     * @param data
     * @description 创建持久化目录节点,直到有删除操作来主动清除这个节点；
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void appendPresistentNode(String path, String data) throws KeeperException, InterruptedException;
    
    
    /**
     * 
     * @param path
     * @param data
     * @description 创建持久化目录顺序节点,直到有删除操作来主动清除这个节点；
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void appendPresistentSequentialNode(String path, String data) throws KeeperException, InterruptedException;
    
    
    /**
     * 
     * @param path
     * @param data
     * @description 创建临时目录节点，一旦创建这个节点的客户端与服务器端口会话失效，这种节点会被自动删除；
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void appendEphemeralNode(String path, String data) throws KeeperException, InterruptedException;
    
    /**
     * 
     * @param path
     * @param data
     * @description 创建临时顺序目录节点，一旦创建这个节点的客户端与服务器端口会话失效，这种节点会被自动删除；
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void appendEphemeralSequentialNode(String path, String data) throws KeeperException, InterruptedException;
    
   
    /**
     * 
     * @param path
     * @description 获取指定节点下的所有子节点 
     * @return
     * @throws KeeperException
     */
    public List<String> getChildren(String path) throws KeeperException, InterruptedException;
    
    
    public List<String> getChildren(String path,Watcher watcher) throws KeeperException,
    InterruptedException ;
    
    /**
     * 
     * @param path
     * @description 获取指定节点上的数据     
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String getData(String path, Stat stat) throws KeeperException, InterruptedException;

    public String getData(String path,Watcher watcher, Stat stat) throws KeeperException,InterruptedException;
    /**
     *
     * @param path
     * @param data
     * @description 变更设置指定节点上的数据
     * @throws KeeperException
     */
    public void setData(String path, String data, int version) throws KeeperException, InterruptedException;
    
    
    /**
     * 
     * @param path
     * @param version -1 如果版本号与节点的版本号不一致，将无法删除，是一种乐观加锁机制；如果将版本号设置为-1，不会去检测版本，直接删除；
     * @description 删除指定目录节点
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void delNode(String path, int version) throws KeeperException, InterruptedException;
    
    /**
     * 
     * @param path
     * @return
     * @description 判断指定目录节点是否存在
     * @throws KeeperException
     * @throws InterruptedException
     */
    public boolean exist(String path) throws KeeperException,InterruptedException;
    
}

 

