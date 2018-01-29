package org.tiger.demohystrixconfig.test;
import java.io.IOException;

import com.netflix.hystrix.util.ZkServer;
import com.netflix.hystrix.util.ZkServerImpl;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;


public class ZkTest{
private static final String hosts="172.26.11.151:2181";
private static final String path = "/test/test2";
 
 public static void main(String args[]){
  ZkTest zkTest = new ZkTest();   
  zkTest.testCreatePersistentNodes();
  //zkTest.testGetNodeData();
  //zkTest.testUpdateNodes();
  //zkTest.testDelNodes();
  
 }


 /**
  * 创建持久化节点
  */
 public void testCreatePersistentNodes(){
  ZkServer zkServer = new ZkServerImpl();
  try{
   zkServer.init(hosts);
   if(!zkServer.exist(path)){
    zkServer.appendPresistentNode(path, "Node test is success");
    System.out.println("成功创建["+path+"]节点!");
   }   

     } catch (IOException e) {
      e.printStackTrace();
     } catch (KeeperException e) {
      e.printStackTrace();
     } catch (InterruptedException e) {
      e.printStackTrace();
     }
 }
 
 
 /**
  * 获取节点的数据
  */
 public void testGetNodeData(){
  ZkServer zkServer = new ZkServerImpl();
  try{
   zkServer.init(hosts);
   
   if(zkServer.exist(path)){
    String temp = zkServer.getData(path, new Stat());
    System.out.println("节点["+path+"]的数据为["+temp+"]");
   }   
     } catch (IOException e) {
      e.printStackTrace();
     } catch (KeeperException e) {
      e.printStackTrace();
     } catch (InterruptedException e) {
      e.printStackTrace();
     }
 }


 
 /**
  * 修改节点的数据
  */
 public void testUpdateNodes(){
  ZkServer zkServer = new ZkServerImpl();
  try{
   zkServer.init(hosts);   
   if(zkServer.exist(path)){
    String path = "/test1";
    zkServer.setData(path, "Node test1 is success Changed", -1);
   }   
     } catch (IOException e) {
      e.printStackTrace();
     } catch (KeeperException e) {
      e.printStackTrace();
     } catch (InterruptedException e) {
      e.printStackTrace();
     }
 }
 


 /**
  * 删除节点
  */
 public void testDelNodes(){
  ZkServer zkServer = new ZkServerImpl();
  try{
   zkServer.init(hosts);   
   if(zkServer.exist(path)){
    zkServer.delNode(path, -1);
   }   
     } catch (IOException e) {
      e.printStackTrace();
     } catch (KeeperException e) {
      e.printStackTrace();
     } catch (InterruptedException e) {
      e.printStackTrace();
     }
 }
 
}
 