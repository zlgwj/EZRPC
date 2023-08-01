package com.zlgewj.spring;

import com.zlgewj.annotation.RpcScan;
import com.zlgewj.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.stereotype.Component;


/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/30 21:45
 */
@Slf4j
public class CustomScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    private static final String BASE_PACKAGE_ATTRIBUTE_NAME = "packages";
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(RpcScan.class.getName()));
        String[] basePackages = new String[0];
        if (annotationAttributes != null ) {
            basePackages = annotationAttributes.getStringArray(BASE_PACKAGE_ATTRIBUTE_NAME);
        }
        if (basePackages.length == 0 ) {
            basePackages = new String[] {((StandardAnnotationMetadata) importingClassMetadata).getIntrospectedClass().getPackage().getName()};
        }

        CustomScanner customScanner = new CustomScanner(registry, RpcService.class);
        CustomScanner springScanner = new CustomScanner(registry, Component.class);
        if (resourceLoader != null ) {
            customScanner.setResourceLoader(resourceLoader);
            springScanner.setResourceLoader(resourceLoader);
        }
        int scan = customScanner.scan(basePackages);
        log.info("扫描到services【{}】个",scan);
        int scan1 = springScanner.scan("com.zlgewj");
        log.info("扫描到component【{}】个",scan1);
        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
    }
}
