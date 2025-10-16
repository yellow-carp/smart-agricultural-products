package com.xtechcn.cloud.payment.core.wx;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.AuthTypeEnum;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.BasePayApiEnum;
import com.ijpay.wxpay.enums.v3.TransferApiEnum;
import com.ijpay.wxpay.model.v3.BatchTransferModel;
import com.ijpay.wxpay.model.v3.RefundAmount;
import com.ijpay.wxpay.model.v3.RefundModel;
import com.ijpay.wxpay.model.v3.TransferDetailInput;
import com.xtechcn.cloud.payment.constants.AttributeKeyNames;
import com.xtechcn.cloud.payment.core.resolver.WxPayResultResolver;
import com.xtechcn.cloud.payment.model.dto.wx.WxRefundResult;
import com.xtechcn.commom.payment.api.AbstractXPayService;
import com.xtechcn.commom.payment.channel.XPayChannel;
import com.xtechcn.commom.payment.channel.XPayChannelRepository;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.handler.XPayFailureHandler;
import com.xtechcn.commom.payment.handler.XPaySuccessHandler;
import com.xtechcn.commom.payment.model.*;
import com.xtechcn.commom.payment.model.conf.WxPayConfModel;
import com.xtechcn.common.serialization.lang.JsonObj;
import com.xtechcn.common.serialization.tools.IJsonUtil;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信支付抽象类
 *
 * @author Alay
 * @since 2023-06-11 03:27
 */
public abstract class AbstractWxPayService extends AbstractXPayService {

    protected static final int OK_STATUS = 200;
    protected static final String MSG_SUCCESS = "SUCCESS";

    private final WxPayResultResolver payResultResolver;


    public AbstractWxPayService(String channel, XPayChannelRepository channelRepository, XPayFailureHandler payFailureHandler,
                                XPaySuccessHandler paySuccessHandler, WxPayResultResolver payResultResolver) {
        super(channel, paySuccessHandler, payFailureHandler, channelRepository);
        this.payResultResolver = payResultResolver;
    }


    @Override
    public XPayResult<?> queryPayResult(XPayTradeModel tradeModel) throws XPayException {
        // 微信配置信息
        WxPayConfModel wxConfig = this.findConfig(tradeModel.channelKey());

        Map<String, String> params = new HashMap<>(16);
        params.put(AttributeKeyNames.WX_MCHID, wxConfig.getMchId());
        try {
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethodEnum.GET,
                    WxDomainEnum.CHINA.toString(),
                    String.format(BasePayApiEnum.ORDER_QUERY_BY_OUT_TRADE_NO.toString(), tradeModel.tradeNo()),
                    wxConfig.getMchId(),
                    WxConfigTool.serialNo(wxConfig),
                    null,
                    privateKey(wxConfig),
                    params);

            /*取消：{"amount":{"payer_currency":"CNY","total":1},"appid":"wxfc0e36204345c206","mchid":"1642979517","out_trade_no":"1654449150982348804202305051933","promotion_detail":[],"scene_info":{"device_id":""},"trade_state":"NOTPAY","trade_state_desc":"订单未支付"}
             * 成功:{"transaction_id":"4200001863202305068344507881","amount":{"currency":"CNY","payer_currency":"CNY","payer_total":1,"total":1},"mchid":"1642979517","trade_state":"SUCCESS","bank_type":"OTHERS","promotion_detail":[],"success_time":"2023-05-06T15:17:31+08:00","payer":{"openid":"oPuGI5V0_LybXP5_fmDLmW1Aiggw"},"out_trade_no":"1654747149419622403202305061517","appid":"wxfc0e36204345c206","trade_state_desc":"支付成功","trade_type":"JSAPI","attach":"5_21"}
             */
            if (response.getStatus() == OK_STATUS) {
                return payResultResolver.queryResolve(response);
            }
            // 未支付的返回结果
            return DefaultXPayResult.Pay.builder().way(XPayResult.WAY_QUERY).success(false).build();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            // 异常的返回结果
            return DefaultXPayResult.Pay.builder().way(XPayResult.WAY_QUERY).success(false).error(ex).build();
        }
    }

    @Override
    public void closeTrade(XPayTradeModel tradeModel) throws XPayException {
        // 微信配置信息
        WxPayConfModel wxConfig = this.findConfig(tradeModel.channelKey());
        Map<String, String> params = new HashMap<>(16);
        params.put(AttributeKeyNames.WX_MCHID, wxConfig.getMchId());
        try {
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethodEnum.POST,
                    WxDomainEnum.CHINA.toString(),
                    String.format(BasePayApiEnum.CLOSE_ORDER_BY_OUT_TRADE_NO.toString(), tradeModel.tradeNo()),
                    wxConfig.getMchId(),
                    WxConfigTool.serialNo(wxConfig),
                    null,
                    privateKey(wxConfig),
                    IJsonUtil.toJson(params));
            /*
             * 成功:IJPayHttpResponse{body='', status=204, headers={Keep-Alive=[timeout=8], null=[HTTP/1.1 204 No Content], Wechatpay-Timestamp=[1683604548], Server=[nginx], X-Content-Type-Options=[nosniff], Connection=[keep-alive], Wechatpay-Signature-Type=[WECHATPAY2-SHA256-RSA2048], Date=[Tue, 09 May 2023 03:55:48 GMT], Wechatpay-Serial=[3B36D2B0CCA86C814D8A3663A8B82A6EC9E5DC34], Wechatpay-Nonce=[ad88bfa5a5a347d93dc82f66b2803a00], Wechatpay-Signature=[nIWU4LgWJNMdkaW6V6QuTFAqFboMyq8FP9nU61GmTZwerWkrFSw6/+laNU4uApVBAAVbZH6LtCz6PeWaFbSpL8jkbLiWeAROZBQo4s5lUUj4OVj4hOiLIes90dcxB34UGe++ZtPlTQO9GKfQmcgzVb6N2S7ch3vl/4Dk/9JszCcvatLLthcxf/P3uKFMW3M7EJ9tBuglZpcsFPPqzf9LqbXxeyhD964Qn6SHcaCgdL7t1cL/4HnomrQULnSJA07D7uIfHEvHPv6Dot5z+pc6kbVnbEEQwvjzQjVGxN+sG+3lWm8rlW4b8874W0Qmi3UdxljhsQdOTofzt9r7W4bwLQ==], Cache-Control=[no-cache, must-revalidate], Content-Length=[0], Content-Language=[zh-CN], Request-ID=[08C488E7A206109E0318FC98E44820BEB60F28C3E105-0], Content-Type=[application/json; charset=utf-8]}}
             */
            if (response.getStatus() == 204) {
                logger.info("wx pay close trade success,response: [{}]", response.getBody());
                // 关闭执行支付失败的逻辑
                payFailureHandler.onFailure(tradeModel, new XPayException("wxpay close trade success"));
            }
        } catch (Exception ex) {
            logger.error("wx pay close trade error,message:[{}]", ex.getMessage());
            throw new XPayException(ex);
        }
    }

    /**
     * @see <a href="https://pay.weixin.qq.com/docs/merchant/apis/jsapi-payment/create.html">微信文档</a>
     */
    @Override
    public XPayResult<?> refundTrade(XPayRefundModel refundModel) throws XPayException {
        XPayNotify xPayNotify = refundModel.notifyUrl();
        // 微信配置信息
        WxPayConfModel wxConfig = this.findConfig(refundModel.channelKey());
        RefundModel model = RefundModel.builder()
                // 交易单好
                .out_trade_no(refundModel.tradeNo())
                // 商户退款单号
                .out_refund_no(refundModel.refundNo())
                // 退款原因
                .reason(refundModel.reason())
                // 回调地址
                .notify_url(this.notifyFormat(xPayNotify.callbackUrl(), refundModel.channelKey()))
                // 退款金额信息
                .amount(RefundAmount.builder()
                        // 退款金额
                        .refund(refundModel.refundAmount().intValue())
                        // 交易单总金额
                        .total(refundModel.amount().intValue())
                        // 币种
                        .currency(refundModel.currency()).build())
                .build();

        logger.warn("Refund parameters:[{}]", JSONUtil.toJsonStr(model));
        DefaultXPayResult.Builder<Object, DefaultXPayResult.Refund<Object>> refundBuilder = DefaultXPayResult.refund().way(XPayResult.WAY_QUERY).success(true);

        try {
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethodEnum.POST,
                    WxDomainEnum.CHINA.toString(),
                    BasePayApiEnum.REFUND.toString(),
                    wxConfig.getMchId(),
                    WxConfigTool.serialNo(wxConfig),
                    null,
                    privateKey(wxConfig),
                    JSONUtil.toJsonStr(model));
            /**
             * 失败：[IJPayHttpResponse{body='{"code":"NOT_ENOUGH","message":"基本账户余额不足，请充值后重新发起"}', status=403, headers={null=[HTTP/1.1 403 Forbidden], Keep-Alive=[timeout=8], Wechatpay-Timestamp=[1722415322], Server=[nginx], X-Content-Type-Options=[nosniff], Connection=[keep-alive], Date=[Wed, 31 Jul 2024 08:42:02 GMT], Wechatpay-Signature-Type=[WECHATPAY2-SHA256-RSA2048], Wechatpay-Serial=[3B36D2B0CCA86C814D8A3663A8B82A6EC9E5DC34], Wechatpay-Nonce=[362772b3de08e6f9d56e56d73fc3a913], Wechatpay-Signature=[sSoqrfU7y58xXQ6OruqWwQHxiGkvrLE2C5Wv3LtaBDi/7OV4vWWRKlhFGwsGOhrYu7JSYbNnKqMl42PcdnMozittR01gWXnC3ZfkQbM3dQLqmBN+W3ar+VJoNeIYZCdd0U/RcybJFXw/xc1nmg0WaG8mx7X4nXfXlKsL2KAuIFy1hLHnJL5BElNgnoX5eduC9cc2dYa6J7giDDSvAAIEKMfxVDnmhC1gvoSM7qDCv23CNzbeuQ6aavmKeBqGZZU3DXCJhNzNJ3jIvAz5Py965R+nKd6DYoJ3RmV4tDSPv7ZTvkOpEdWaMrUaRDrDDCy2mb/iqzqxEwI0TqjuOzsqpg==], Cache-Control=[no-cache, must-revalidate], Content-Length=[85], Request-ID=[08DAF1A7B50610FB0318FCB3C05520D5232891CE04-268892709], Content-Language=[zh-CN], Content-Type=[application/json; charset=utf-8]}}]
             * 成功:[IJPayHttpResponse{body='{"amount":{"currency":"CNY","discount_refund":0,"from":[],"payer_refund":10,"payer_total":10,"refund":10,"refund_fee":0,"settlement_refund":10,"settlement_total":10,"total":10},"channel":"ORIGINAL","create_time":"2024-07-31T16:55:39+08:00","funds_account":"AVAILABLE","out_refund_no":"1818571211644825600","out_trade_no":"1815322542292299776","promotion_detail":[],"refund_id":"50303400132024073135461150169","status":"PROCESSING","transaction_id":"4200002346202407227299244938","user_received_account":"支付用户零钱"}', status=200, headers={null=[HTTP/1.1 200 OK], Keep-Alive=[timeout=8], Wechatpay-Timestamp=[1722416139], Server=[nginx], X-Content-Type-Options=[nosniff], Connection=[keep-alive], Date=[Wed, 31 Jul 2024 08:55:39 GMT], Wechatpay-Signature-Type=[WECHATPAY2-SHA256-RSA2048], Wechatpay-Serial=[3B36D2B0CCA86C814D8A3663A8B82A6EC9E5DC34], Wechatpay-Nonce=[2dbad5dcaa6a84099a1944e26fce1e24], Wechatpay-Signature=[BRuSvnMyLBiafMNZ3+NmcKZqci9olnixxQE/fDStcPGJonKJw/0tB/yCYszA1ZL4ptdgDzqNxVT2eOuPz69ydIJhAdnvCkStSyKBgR0Y6zzMTQoKc+9+fffjk2TDpuKC9gEZPB0eRpqavu1dT/Mm+qhVEUP0OoMin7QzEgb+x5JJdUuC50zKDAmVILTnbLwJWvtY/axHL5aF183l7yTx62AzlUsFjw6c6qeafS3/ujAdnYe1D5/sO4ObCI9ynwm6Y/W2vpqz68NqeOALwAuHBnb6fILbjighc0D735anMnTXypVJlYik4WFra6VRIHNITFI/Jv0QW3Ptna+33v4ktg==], Cache-Control=[no-cache, must-revalidate], Content-Encoding=[deflate], Content-Length=[346], Request-ID=[088BF8A7B506108A0118C9ADEEA306205F28B9A904-0], Content-Language=[zh-CN], Content-Type=[application/json; charset=utf-8]}}]
             */
            JsonObj responseBody = IJsonUtil.parseObject(response.getBody(), JsonObj.class);
            if (response.getStatus() != OK_STATUS) {
                // {"code":"NOT_ENOUGH","message":"基本账户余额不足，请充值后重新发起"}
                logger.error("Refund failed:[{}]", responseBody);
                return refundBuilder.success(false).message(responseBody.getStr("message")).data(responseBody).build();
            }

            return refundBuilder.data(responseBody)
                    // SUCCESS退款成功/CLOSED退款关闭/PROCESSING退款处理中/ABNORMAL退款异常
                    .success(MSG_SUCCESS.equals(responseBody.getStr("status")))
                    .way(XPayResult.WAY_INSTANT)
                    .build();
        } catch (Exception ex) {
            logger.error("wx pay refund error,message:[{}]]", ex.getMessage());
            throw new XPayException(ex);
        }
    }


    @Override
    public XPayResult<?> queryRefundResult(XPayRefundModel refundModel) {
        // 微信配置信息
        WxPayConfModel wxConfig = this.findConfig(refundModel.channelKey());
        Map<String, String> params = new HashMap<>(16);

        DefaultXPayResult.Builder<WxRefundResult.RefundData, WxRefundResult> refundBuilder = WxRefundResult.builder()
                .success(false).way(XPayResult.WAY_QUERY);
        try {
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethodEnum.GET,
                    WxDomainEnum.CHINA.toString(),
                    String.format(BasePayApiEnum.REFUND_QUERY_BY_OUT_REFUND_NO.toString(), refundModel.refundNo()),
                    wxConfig.getMchId(),
                    WxConfigTool.serialNo(wxConfig),
                    null,
                    privateKey(wxConfig),
                    params);

            logger.info("response: {}", response.getBody());

            if (response.getStatus() == OK_STATUS) {
                // 查询结果解析
                WxRefundResult.RefundData wxpayRefundData = IJsonUtil.parseObject(response.getBody(), WxRefundResult.RefundData.class);
                refundBuilder.data(wxpayRefundData);
                // 交易状态：成功
                if (MSG_SUCCESS.equals(wxpayRefundData.getStatus())) {
                    // 退款成功
                    return refundBuilder.success(true).build();
                }
            } else {
                JsonObj jsonObj = IJsonUtil.parseObject(response.getBody(), JsonObj.class);
                refundBuilder.message(jsonObj.getStr("message", "no message"));
            }
        } catch (Exception ex) {
            logger.error("wxpay query refund result error, message:[{}]", ex.getMessage());
            throw new XPayException(ex);
        }
        return refundBuilder.success(false).build();
    }

    /**
     * @see <a href="https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_2">微信付款</a>
     */
    @Override
    public XPayResult<?> exchange(XPayExchangeModel exchangeModel) {
        // 微信配置信息
        WxPayConfModel config = this.findConfig(exchangeModel.channelKey());

        // 提现请求参数构建
        List<Map<String, Object>> details = exchangeModel.attribute("details");
        List<TransferDetailInput> detailInputs = new ArrayList<>();
        for (Map<String, Object> detailMap : details) {
            TransferDetailInput detailInput = TransferDetailInput.builder()
                    .out_detail_no(detailMap.get("detailNo").toString())
                    .transfer_amount(Integer.parseInt(detailMap.get("amount").toString()))
                    .transfer_remark(detailMap.get("remark").toString())
                    .openid(detailMap.get("openId").toString()).build();
            detailInputs.add(detailInput);
        }
        BatchTransferModel batchTransferModel = BatchTransferModel.builder()
                // 申请商户号的appid或商户号绑定的appid（企业号corpid即为此appid）
                .appid(config.getAppId())
                // 商户系统内部的商家批次单号，要求此参数只能由数字、大小写字母组成，在商户系统内部唯一
                .out_batch_no(exchangeModel.batchNo())
                // 该笔批量转账的名称
                .batch_name(exchangeModel.exchangeName())
                // 转账说明，UTF8编码，最多允许32个字符
                .batch_remark(exchangeModel.reason())
                // 转账金额单位为“分”。转账总金额必须与批次内所有明细转账金额之和保持一致，否则无法发起转账操作
                .total_amount(exchangeModel.amount().intValue())
                // 一个转账批次单最多发起一千笔转账。转账总笔数必须与批次内所有明细之和保持一致，否则无法发起转账操作
                .total_num(detailInputs.size())
                // 发起批量转账的明细列表，最多一千笔
                .transfer_detail_list(detailInputs)
                // 指定该笔转账使用的转账场景ID
                .transfer_scene_id(exchangeModel.attribute("transferSceneId").toString()).build();

        logger.info("wxpay exchange parameters:[{}]", JSONUtil.toJsonStr(batchTransferModel));
        try {
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethodEnum.POST,
                    WxDomainEnum.CHINA.toString(),
                    TransferApiEnum.TRANSFER_BATCHES.toString(),
                    config.getMchId(),
                    WxConfigTool.serialNo(config),
                    null,
                    privateKey(config),
                    JSONUtil.toJsonStr(batchTransferModel)
            );
            logger.info("发起商家转账响应 {}", response);
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(response, IoUtil.toStream(WxConfigTool.platformCert(config), StandardCharsets.UTF_8));
            logger.info("verifySignature [{}]", verifySignature);

            if (response.getStatus() == OK_STATUS) {
                XPayResult<?> exchangeResult = this.queryExchangeResult(exchangeModel);
                // 提现成功
                if (exchangeResult.success()) {
                }
                return exchangeResult;
            }

        } catch (Exception ex) {
            logger.error("wxpay query exchange error, message:[{}]", ex.getMessage());
            throw new XPayException(ex);
        }
        return DefaultXPayResult.Exchange.builder().success(false).way(XPayResult.WAY_QUERY).build();
    }

    /**
     * @see <a href="https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_3">微信查询付款</a>
     */
    @Override
    public XPayResult<?> queryExchangeResult(XPayExchangeModel exchangeModel) throws XPayException {
        return super.queryExchangeResult(exchangeModel);
    }


    protected WxPayConfModel findConfig(String channelKey) {
        // 支付配置
        XPayChannel xPayChannel = channelRepository.findOne(channelKey);
        return channelRepository.transform(xPayChannel, WxPayConfModel.class);
    }

    protected static PrivateKey privateKey(WxPayConfModel wxConfig) throws Exception {
        return PayKit.getPrivateKeyByKeyContent(wxConfig.getKeyText(), AuthTypeEnum.RSA.getCode());
    }

}
