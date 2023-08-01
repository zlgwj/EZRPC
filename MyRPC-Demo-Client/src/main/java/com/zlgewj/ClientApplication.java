package com.zlgewj;

import com.zlgewj.annotation.RpcScan;
import com.zlgewj.controller.TestController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/31 16:58
 */
@RpcScan(packages = {"com.zlgewj"})
public class ClientApplication {

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext annotationContext = new AnnotationConfigApplicationContext(ClientApplication.class);
        TestController controller = (TestController) annotationContext.getBean("testController");
        controller.test2();
        System.out.println(controller);
    }
}
