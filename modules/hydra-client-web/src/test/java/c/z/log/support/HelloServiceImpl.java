package c.z.log.support;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import c.z.log.HelloService;
import c.z.log.MethodLog;
import c.z.log.model.Hello;

@Service
public class HelloServiceImpl implements HelloService {

    @Override
    @MethodLog
    public int sayHello(String str) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str.hashCode();
    }

    @Override   @MethodLog
    public String sayHello2(Hello user) {
        // TODO Auto-generated method stub
        return user.getHelloContent();
    }

}
