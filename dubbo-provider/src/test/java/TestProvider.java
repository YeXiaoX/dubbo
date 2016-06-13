import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Ivan on 2016/5/24.
 */
public class TestProvider {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"provider.xml"});
        context.start();

       System.in.read(); // 按任意键退出
    }
}
