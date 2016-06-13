import com.dubbo.demo.Demo2Service;
import com.dubbo.demo.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ivan on 2016/5/24.
 */
public class TestConsumer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"remote-consumer.xml"});
        context.start();

        DemoService demoService = (DemoService)context.getBean("demoService"); // 获取远程服务代理
        Demo2Service demo2Service = (Demo2Service)context.getBean("demo2Service");
       // String hello = demoService.sayHello("world"); // 执行远程方法
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                String hello = demoService.sayHello("world" + i);
                String hi = demo2Service.sayHi("world" + i);
                System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + hello);
                System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + hi);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(2000);
        }


    }
}
