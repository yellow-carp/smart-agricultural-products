package com.xtechcn.cloud.customer.constants;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 客户模块常量
 *
 * @author Alay
 * @since 2025-08-31 16:34
 */
public interface CustomerConstants {
    /**
     * 供应商密码前缀
     */
    String SUPPLIER_PASSWORD_KEY = "Supplier";
    /**
     * 默认密前缀
     */
    String DEFAULT_PASSWORD_PREFIX = "Supp";

    /**
     * 采购商入驻订单标题
     */
    String PURCHASER_ORDER_TITLE = "采购商入驻订单";

    /*
     * 采购商入驻方式：1-邀请码入驻
     */
    Integer PURCHASER_MODEL_INVITE = 1;
    /**
     * 采购商入驻方式：2-支付入驻
     */
    Integer PURCHASER_MODEL_PAY = 2;
    /**
     * 采购商入驻方式：3-后台添加
     */
    Integer PURCHASER_MODEL_ADMIN = 3;


    /**
     * 采购商入驻交易单验证 todo
     */
    String PURCHASER_ORDER_VERIFY_TRADE = "http://xtechcn-mall-order/member/pc/trade/verify";
}
