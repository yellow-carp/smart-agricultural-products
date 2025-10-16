package com.xtechcn.cloud.payment.core.cpcn;

import cn.hutool.core.util.ReUtil;
import com.xtechcn.cloud.payment.constants.AttributeKeyNames;
import com.xtechcn.cloud.payment.core.resolver.CpcnPayResultResolver;
import com.xtechcn.cloud.payment.model.dto.cpcn.CpcnPayResult;
import com.xtechcn.commom.payment.api.AbstractXPayService;
import com.xtechcn.commom.payment.channel.XPayChannelRepository;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.handler.XPayFailureHandler;
import com.xtechcn.commom.payment.handler.XPaySuccessHandler;
import com.xtechcn.commom.payment.model.*;
import com.xtechcn.common.core.utils.IParamUtil;
import com.xtechcn.common.cpcn.api.CpcnTxRequestExecutor;
import com.xtechcn.common.cpcn.api.builder.*;
import com.xtechcn.common.cpcn.constants.CpcnStatus;
import com.xtechcn.common.serialization.tools.IJsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import payment.api.system.PaymentEnvironment;
import payment.api.tx.TxBaseResponse;
import payment.api.tx.aggregate.*;
import payment.api.util.GUIDGenerator;

/**
 * 中金支付
 *
 * @author Alay
 * @since 2023-10-09 17:46
 */
public class XPayCpcnPayService extends AbstractXPayService {
    /**
     * 商品名称校验正则表达式
     */
    private static final String REGEX = "[<>#“”\"“‘’~^*、;；$+&%@/]";
    private static final String PAYER_USER_ID = "JHKL15151";
    /**
     * CPCN 小程序标识: 10-普通小程序
     */
    private static final String PLUGIN_FLAG_ORDINARY = "10";

    private final CpcnTxRequestExecutor cpcnTxRequestExecutor;
    private final CpcnPayResultResolver cpcnPayResultResolver;

    public XPayCpcnPayService(String channel, XPayChannelRepository channelRepository, XPayFailureHandler payFailureHandler,
                              XPaySuccessHandler paySuccessHandler, CpcnTxRequestExecutor cpcnTxRequestExecutor,
                              CpcnPayResultResolver cpcnPayResultResolver) {
        super(channel, paySuccessHandler, payFailureHandler, channelRepository);
        this.cpcnTxRequestExecutor = cpcnTxRequestExecutor;
        this.cpcnPayResultResolver = cpcnPayResultResolver;
    }

    @Override
    public Object executePay(XPayTradeModel tradeModel, HttpServletRequest request, HttpServletResponse response) throws XPayException {
        // 租户ID
        String payeeUserID = "用户在中金账户的ID";

        // 过滤掉特殊字符
        String goodsName = ReUtil.replaceAll(tradeModel.body(), REGEX, "");

        // 中金支付请求体
        Tx5011Request tx5011Request = Tx5011Builder.of()
                // 机构编码
                .institutionID(PaymentEnvironment.institutionID)
                // 我们的交易单号作为中金的订单号
                .orderNo(tradeModel.tradeNo())
                // 交易流水号(18-32)
                .txSN(tradeModel.batchNo())
                .payerUserID(PAYER_USER_ID)
                .payeeUserID(payeeUserID)
                .amount(String.valueOf(tradeModel.payAmount().intValue()))
                // 商品名称超过31位时，截取31位
                .goodsName(goodsName.length() > 31 ? goodsName.substring(0, 31) : goodsName)
                // 后台通知地址
                .noticeURL(tradeModel.notifyUrl().callbackUrl())
                // .noticeURL(super.notifyFormat(tradeModel.notifyUrl().callbackUrl(), tradeModel.channelKey()))
                // 用户 IP
                .clientIP(tradeModel.attribute("clientIp"))
                .platformName("新腾商贸")
                // 延迟分账
                .hasSubsequentSplit("2")
                // 支付方式：80 = 跳转支付
                .redirectPay()
                .payWay(IParamUtil.ofStrValue(tradeModel.attribute("payWay"), "51"))
                .payType(IParamUtil.ofStrValue(tradeModel.attribute("payType"), "31"))
                .redirectSource(IParamUtil.ofStrValue(tradeModel.attribute("redirectSource"), "40"))
                // CPCN 小程序标识：10-普通小程序,20-插件模式,30=半屏小程序
                .pluginFlag(tradeModel.attribute(AttributeKeyNames.PLUGIN_FLAG))
                // pluginFlag = 10 时，subAppID必填
                .subAppID(tradeModel.attribute(AttributeKeyNames.PLUGIN_FLAG).equals(PLUGIN_FLAG_ORDINARY) ? tradeModel.attribute(AttributeKeyNames.APP_ID) : null)
                // pluginFlag = 10 时，subOpenID必填
                .subOpenID(tradeModel.attribute(AttributeKeyNames.PLUGIN_FLAG).equals(PLUGIN_FLAG_ORDINARY) ? tradeModel.attribute(AttributeKeyNames.WX_OPEN_ID) : null)
                .installmentType("10")
                .end()
                .build();
        try {
            return cpcnTxRequestExecutor.request(tx5011Request);
        } catch (Exception ex) {
            logger.error("cpcn pay execute-pay failed,message:[{}]", ex.getMessage());
            throw new XPayException(ex);
        }
    }

    @Override
    public XPayResult<?> queryPayResult(XPayTradeModel tradeModel) throws XPayException {
        Tx5016Request txRequest = Tx5016Builder.of()
                // 机构编码
                .institutionID(PaymentEnvironment.institutionID)
                // 支付流水号
                .txSN(tradeModel.batchNo())
                // 原支付交易时间(8)，格式：yyyyMMdd；不上送只支持一年之内的支付易查询
                .sourceTxTime("").build();
        try {
            // 查询支付结果
            TxBaseResponse response = cpcnTxRequestExecutor.request(txRequest);
            // 解析查询结果
            return cpcnPayResultResolver.queryResolve(response);
        } catch (Exception ex) {
            // 中金支付结果查询出现异常了
            logger.error("cpcn pay query pay-result failed,message:[{}]", ex.getMessage());
            return CpcnPayResult.builder().success(false).way(XPayResult.WAY_QUERY).error(ex).build();
        }
    }


    @Override
    public XPayResult<Tx5021Response> refundTrade(XPayRefundModel refundModel) throws XPayException {
        DefaultRefundModel refund = (DefaultRefundModel) refundModel;
        // 退款-true，撤销-false
        boolean isRefund = refund.attribute(AttributeKeyNames.REFUND_TYPE_KEY);

        Tx5021Request tx5021Request = Tx5021Builder.of()
                // 机构编码
                .institutionID(PaymentEnvironment.institutionID)
                // 退款交易流水号
                .txSN(refundModel.refundNo())
                // 原支付流水号
                .paymentTxSN(refund.serialNo())
                // 退款方式: 20-原路退款
                .refundWay("20")
                // 退款金额
                .amount(isRefund ? String.valueOf(refundModel.refundAmount().intValue()) : null)
                // 撤销金额
                .cancelAmount(isRefund ? null : String.valueOf(refundModel.refundAmount().intValue()))
                // 后台通知地址 https://payment.xtechcn.com/payment/wxpay/notify/租户编码/所属应用/3-1
                .noticeURL(super.notifyFormat(refundModel.notifyUrl().callbackUrl(), refundModel.channelKey()))
                // 原支付交易时间
                .sourceTxTime(refundModel.attribute("successTime"))
                .remark(refund.reason()).build();
        // 备注
        try {
            // 执行查询中金 API
            Tx5021Response txResponse = (Tx5021Response) cpcnTxRequestExecutor.request(tx5021Request);
            logger.trace("cpcn pay refund-trade  response[{}]", IJsonUtil.toJson(txResponse));
            /*
             * 退款状态
             * 10=已受理
             * 11=受理成功
             * 20=退款成功
             * 30=退款失败
             * 40=退票
             */
            return DefaultXPayResult.Refund.<Tx5021Response>builder().success(CpcnStatus.STATUS10.equals(txResponse.getStatus())).data(txResponse).build();
        } catch (Exception ex) {
            logger.trace("cpcn pay refund-trade failed,message [{}]", ex.getMessage());
            throw new XPayException(ex);
        }
    }

    @Override
    public XPayResult<?> queryRefundResult(XPayRefundModel refundModel) throws XPayException {
        Tx5026Request tx5026Request = Tx5026Builder.of()
                // 机构编码
                .institutionID(PaymentEnvironment.institutionID)
                // 退款交易流水号
                .txSN(refundModel.refundNo()).build();
        try {
            Tx5026Response response = (Tx5026Response) cpcnTxRequestExecutor.request(tx5026Request);
            /*
             * 退款状态
             * 10=已受理
             * 11=受理成功
             * 20=退款成功
             * 30=退款失败
             * 40=退票
             */
            return cpcnPayResultResolver.queryResolve(response);
        } catch (Exception ex) {
            logger.error("cpcn pay query-refund failed,message:[{}]", ex.getMessage());
            throw new XPayException(ex);
        }
    }

    @Override
    public void closeTrade(XPayTradeModel tradeModel) throws XPayException {
        try {
            Tx5014Request txRequest = Tx5014Builder.of()
                    .institutionID(PaymentEnvironment.institutionID)
                    // 订单关闭交易流水号
                    .txSN(GUIDGenerator.genGUID())
                    // 原支付交易流水号
                    .paymentTxSN(tradeModel.batchNo()).build();
            // 执行中金Api调用
            Tx5014Response response = (Tx5014Response) cpcnTxRequestExecutor.request(txRequest);
            logger.info("**** 中金支付关闭交易结果[{}],数据体[{}]：", CpcnStatus.STATUS10.equals(response.getStatus()) ? "成功" : "失败", IJsonUtil.toJson(response));
            // 执行未支付的逻辑
            payFailureHandler.onFailure(tradeModel, new XPayException("user not paid"));
        } catch (Exception ex) {
            logger.error("cpcn pay close-trade failed,message:[{}]", ex.getMessage());
            // 执行未支付的逻辑
            payFailureHandler.onFailure(tradeModel, new XPayException("user not paid", ex));
            throw new XPayException(ex);
        }
    }

}
