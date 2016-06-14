import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import org.I0Itec.zkclient.*;
import org.apache.zookeeper.Watcher;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by Ivan on 2016/6/6.
 */

public class TestZkClient {

    public static void main(String args[]) {
        String path = "/dubbo1";
        ZkClient zkClient = new ZkClient("192.168.247.128:2182",5000);

        System.out.println("是否存在：" + zkClient.exists("/dubbo/com.dubbo.demo.DemoService"));

        List<String> children = zkClient.getChildren("/dubbo");
        for (String s : children) {
            try {
                s = URLDecoder.decode(s, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("元素：" + s);
        }

        System.out.println("111111111111" );



        zkClient.subscribeStateChanges(new IZkStateListener() {
            public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
                System.out.println("当前状态码：" + keeperState.getIntValue());
            }
            public void handleNewSession() throws Exception {
                System.out.println("当前状态码：");
            }
        });

        zkClient.subscribeDataChanges(path+"/c1", new IZkDataListener() {
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("the node 'dataPath'===>");
            }
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("the node 'dataPath'===>" + dataPath + ", data has changed.it's data is " + String.valueOf(data));
            }
        });
        zkClient.setCurrentState(Watcher.Event.KeeperState.SyncConnected);
        zkClient.waitUntilConnected();


        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println(parentPath + " 's child changed, currentChilds:" + currentChilds);
            }
        });

        System.out.println("3333333333333333333" );


      //  System.out.println(zkClient.getChildren(path));
        zkClient.createPersistent(path );
        zkClient.createPersistent(path +"/c1");
        zkClient.writeData(path+"/c1","test001");
        System.out.println("是否存在：" + zkClient.exists("/dubbo1/c1/d1"));
        //zkClient.createPersistent(path+"/c1/d1");
        //zkClient.createPersistent(path+"/c2");
        //zkClient.delete("/dubbo1/c2");

        //zkClient.delete("/dubbo1/c1/d1");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("55555555" );
        zkClient.delete(path + "/c1");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        zkClient.delete(path);
    }
}
