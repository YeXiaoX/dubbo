
import org.apache.zookeeper.*;

import java.util.List;

/**
 * Created by Ivan on 2016/6/2.
 */
public class TestZookeeper {
    public static void main(String args[]) {
        ZooKeeper zk = null;

        try {
            // 创建一个与服务器的连接
            zk = new ZooKeeper("127.0.0.1:2181",
                    30000, new Watcher() {
                // 监控所有被触发的事件
                public void process(WatchedEvent event) {
                    System.out.println("已经触发了" + event.getType() + "事件！");
                }
            });

            List<String> list = zk.getChildren("/dubbo", true);
            byte[] data = zk.getData("/dubbo/com.dubbo.demo.DemoService",false,null);
            System.out.println(data.length);
            for(String s:list){
                System.out.println("元素："+s);
            }
            // 创建一个目录节点
            zk.create("/testRootPath", "testRootData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
            // 创建一个子目录节点
            zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println(new String(zk.getData("/testRootPath", false, null)));
            // 取出子目录节点列表
            System.out.println(zk.getChildren("/testRootPath", true));
            // 修改子目录节点数据
            zk.setData("/testRootPath/testChildPathOne", "modifyChildDataOne".getBytes(), -1);
            System.out.println("目录节点状态：[" + zk.exists("/testRootPath", true) + "]");
            // 创建另外一个子目录节点
            zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println(new String(zk.getData("/testRootPath/testChildPathTwo", true, null)));
            // 删除子目录节点
            zk.delete("/testRootPath/testChildPathTwo", -1);
            zk.delete("/testRootPath/testChildPathOne", -1);
            // 删除父目录节点
            zk.delete("/testRootPath", -1);
            // 关闭连接
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
