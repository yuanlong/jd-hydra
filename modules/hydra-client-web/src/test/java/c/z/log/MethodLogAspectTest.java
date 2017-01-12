/**
 * 
 */
package c.z.log;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import c.z.log.model.Hello;

/**
 * @author Administrator
 *
 */
public class MethodLogAspectTest {

    public static void main(String args[]) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/c/z/log/log-spring.xml");
        HelloService helloService = context.getBean("helloServiceImpl", HelloService.class);
        int result=helloService.sayHello("zhu");
        System.out.println(result);
        
        Hello hello=new Hello();
        hello.setHelloContent("444444444666666666666");
        hello.setId(234L);
        hello.setName("test....");
        String r=helloService.sayHello2(hello);
        System.out.println(r);
    }
}
