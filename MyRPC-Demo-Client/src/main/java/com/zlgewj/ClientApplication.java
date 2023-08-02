package com.zlgewj;

import com.zlgewj.annotation.RpcScan;
import com.zlgewj.controller.TestController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zlgewj
 * @version 1.0

 */
@RpcScan(packages = {"com.zlgewj"})
public class ClientApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationContext = new AnnotationConfigApplicationContext(ClientApplication.class);
        TestController controller = (TestController) annotationContext.getBean("testController");
        for (int i = 0; i < 10; i++) {
            controller.test();
        }
    }
}
