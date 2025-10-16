package com.xtechcn.cloud.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.api.system.model.UserProfile;
import com.xtechcn.cloud.customer.entity.PurchaserConfig;
import com.xtechcn.cloud.customer.entity.PurchaserOrder;
import com.xtechcn.cloud.customer.model.TradeCheckModel;
import com.xtechcn.cloud.customer.model.po.PrOrderParam;
import com.xtechcn.cloud.customer.model.vo.OrderVerifyView;
import jakarta.validation.Valid;

import java.util.List;
import java.util.SequencedCollection;

/**
* 采购商入驻订单
*
* @author hanjie
* @since 2025-09-18 16:58:25
*/
public interface PurchaserOrderService extends IService<PurchaserOrder> {

    /**
     * 创建采购商入驻订单
     */
    OrderVerifyView applyPurchaserOrder(PrOrderParam prOrderParam, UserProfile profile, PurchaserConfig config);

    /**
     * 交易单验证
     */
    TradeCheckModel checkDataMsg(TradeCheckModel tradeCheck);

    /**
     * 根据交易单查询
     */
    List<PurchaserOrder> listByTradeNo(String tradeNo);
}