package com.xtechcn.cloud.api.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动类注解
 *
 * @author Alay
 * @since 2025-08-11 14:43
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableFreshServer.Importer.class)
public @interface EnableFreshServer {

    @MapperScan(value = {"com.xtechcn.cloud.*.mapper"})
    @ComponentScan(value = {"com.xtechcn.cloud.product","com.xtechcn.cloud.customer","com.xtechcn.cloud.payment"})
    class Importer {

    }

}
