package com.xtechcn.cloud.payment.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Payment Server 启动类注解
 *
 * @author Alay
 * @since 2025-07-30 13:48
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnablePaymentServer.Importer.class)
public @interface EnablePaymentServer {

    @MapperScan(value = {"com.xtechcn.cloud.payment.mapper"})
    @ComponentScan(value = {"com.xtechcn.cloud.payment"})
    class Importer {

    }

}
