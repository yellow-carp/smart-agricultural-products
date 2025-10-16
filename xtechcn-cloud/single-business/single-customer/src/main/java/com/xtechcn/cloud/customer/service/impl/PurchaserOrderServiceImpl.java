package com.xtechcn.cloud.customer.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.api.system.model.UserProfile;
import com.xtechcn.cloud.customer.constants.CustomerConstants;
import com.xtechcn.cloud.customer.constants.CustomerResult;
import com.xtechcn.cloud.customer.constants.PrOrderStateEnum;
import com.xtechcn.cloud.customer.constants.PurchaserStateEnum;
import com.xtechcn.cloud.customer.entity.PurchaserConfig;
import com.xtechcn.cloud.customer.entity.PurchaserOrder;
import com.xtechcn.cloud.customer.mapper.PurchaserOrderMapper;
import com.xtechcn.cloud.customer.model.TradeCheckModel;
import com.xtechcn.cloud.customer.model.po.PrOrderParam;
import com.xtechcn.cloud.customer.model.vo.OrderVerifyView;
import com.xtechcn.cloud.customer.service.PurchaserOrderService;
import com.xtechcn.common.core.exceptions.ParamException;
import com.xtechcn.common.core.exceptions.RollBackException;
import com.xtechcn.common.core.result.FailedResult;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 采购商入驻订单
 *
 * @author hanjie
 * @since 2025-09-18 16:58:25
 */
@Service
public class PurchaserOrderServiceImpl extends ServiceImpl<PurchaserOrderMapper, PurchaserOrder> implements PurchaserOrderService {

    @Override
    public OrderVerifyView applyPurchaserOrder(PrOrderParam prOrderParam, UserProfile profile, PurchaserConfig config) {

        PurchaserOrder purchaserOrder = new PurchaserOrder();
        purchaserOrder.setTitle(CustomerConstants.PURCHASER_ORDER_TITLE);
        purchaserOrder.setOrderState(PrOrderStateEnum.TO_BE_PAID.getCode());
        purchaserOrder.setPayAmount(config.getRegisterAmount());
        purchaserOrder.setCreateName(profile.getNickname());
        purchaserOrder.setCreateNameTel(profile.getPhone());
        purchaserOrder.setRemark(prOrderParam.getRemark());
        purchaserOrder.setUserId(profile.getUserId());
        // 保存
        this.save(purchaserOrder);
        OrderVerifyView orderVerifyView = new OrderVerifyView()
                .orderNo(purchaserOrder.getOrderNo()).verifyUrl(CustomerConstants.PURCHASER_ORDER_VERIFY_TRADE);
        return orderVerifyView;
    }

    @Override
    public TradeCheckModel checkDataMsg(TradeCheckModel tradeCheck) {
        TradeCheckModel.TradeOrder tradeOrder = tradeCheck.getOrders().getFirst();
        // 根据订单号查询订单信息
        PurchaserOrder purchaserOrder = this.getById(tradeOrder.getOrderNo());
        if (purchaserOrder == null) throw new ParamException(FailedResult.NOT_FOUNT);
        // 订单类型，目前固定1：正常订单
        // tradeCheck.setOrderType(1);
        // 交易类型，目前固定1：商品交易
        tradeCheck.setTradeType(6);
        tradeCheck.setSubject(purchaserOrder.getTitle());
        tradeCheck.setBody(purchaserOrder.getTitle());
        tradeOrder.setPayee(purchaserOrder.getUserId());
        tradeOrder.setAmount(purchaserOrder.getPayAmount());
        tradeOrder.setPayAmount(purchaserOrder.getPayAmount());

        if (PrOrderStateEnum.TO_BE_PAID.getCode() != purchaserOrder.getOrderState()) {
            throw new RollBackException(CustomerResult.ORDER_PAY_ERROR);
        }

        return null;
    }

    @Override
    public List<PurchaserOrder> listByTradeNo(String tradeNo) {
        List<PurchaserOrder> purchaserOrders = this.list(Wrappers.<PurchaserOrder>lambdaQuery().eq(PurchaserOrder::getTradeNo, tradeNo));
        return purchaserOrders;
    }
}
