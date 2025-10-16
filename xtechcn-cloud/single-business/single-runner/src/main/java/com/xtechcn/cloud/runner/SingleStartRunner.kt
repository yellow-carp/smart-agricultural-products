package com.xtechcn.cloud.runner;

import com.xtechcn.cloud.api.config.EnableFreshServer;
import com.xtechcn.cloud.auth.config.EnableAuthServer;
import com.xtechcn.cloud.base.config.EnableBaseServer;
import com.xtechcn.cloud.job.config.EnableJobServer;
import com.xtechcn.cloud.upms.config.EnableUpmsServer;
import com.xtechcn.common.wechat.annotation.EnableWechatAuth;
import com.xtechcn.common.wechat.annotation.EnableWechatQrCodeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 启动器
 * 单体项目按模块分组管理,所以如下使用@MapperScans{@link SingleStartRunner} 和 @ComponentScans{@link SingleStartRunner}选择性进行注入服务
 *
 * @author Alay
 * @since 2024-08-29 16:09
 */
@EnableJobServer
@EnableAuthServer
@EnableUpmsServer
@EnableBaseServer
@EnableWechatAuth
@EnableFreshServer
@SpringBootApplication
@EnableWechatQrCodeService
open class SingleStartRunner {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(SingleStartRunner::class.java, *args)
        }
    }
}
