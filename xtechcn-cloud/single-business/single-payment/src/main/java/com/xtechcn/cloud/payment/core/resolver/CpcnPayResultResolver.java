package com.xtechcn.cloud.payment.core.resolver;

import com.xtechcn.cloud.payment.core.resolver.decrypt.CpcnDataDecrypt;
import com.xtechcn.cloud.payment.model.dto.cpcn.CpcnPayData;
import com.xtechcn.cloud.payment.model.dto.cpcn.CpcnPayResult;
import com.xtechcn.commom.payment.model.XPayResult;
import com.xtechcn.commom.payment.resolver.XPayResultResolver;
import com.xtechcn.common.core.utils.IMapUtil;
import com.xtechcn.common.cpcn.constants.CpcnStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import payment.api.notice.*;
import payment.api.tx.TxBaseResponse;
import payment.api.tx.aggregate.Tx5016Response;
import payment.api.tx.aggregate.Tx5026Response;
import payment.api4cb.notice.Notice2048Request;
import payment.api4cb.notice.Notice5568Request;
import payment.api4cb.notice.Notice5598Request;
import payment.api4cb.notice.Notice5658Request;

import java.util.HashMap;
import java.util.Map;

/**
 * 中金支付参数解析
 *
 * @author Alay
 * @since 2023-10-09 13:35
 */
@Component
@RequiredArgsConstructor
public class CpcnPayResultResolver implements XPayResultResolver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CpcnDataDecrypt cpcnDataDecrypt;

    @Override
    public CpcnPayResult callbackResolve(HttpServletRequest request) {
        try {
            // 数据解密
            NoticeRequest noticeRequest = cpcnDataDecrypt.callbackDecrypt(request);
            // 解析支付数据体
            CpcnPayData cpcnPayData = this.parsePlainText(noticeRequest);

            return CpcnPayResult.builder().data(cpcnPayData).success(cpcnPayData.isSuccess())
                    .time(System.currentTimeMillis()).way(XPayResult.WAY_NOTIFY).build();
        } catch (Exception ex) {
            logger.error("回调解析失败, message: [{}]", ex.getMessage());
            return CpcnPayResult.builder().success(false).way(XPayResult.WAY_NOTIFY).time(System.currentTimeMillis()).error(ex).build();
        }
    }

    @Override
    public CpcnPayResult queryResolve(Object parameter) {
        // 这里是 Tx5016Response 对象
        CpcnPayData payData = convertPayData((TxBaseResponse) parameter);
        // 支付结果数据返回
        return CpcnPayResult.builder().success(payData.isSuccess()).way(XPayResult.WAY_QUERY)
                .data(payData).time(System.currentTimeMillis())
                .build();
    }


    private CpcnPayData parsePlainText(NoticeRequest noticeRequest) throws Exception {
        // 4 响应支付平台 特别说明：为避免重复发通知，必须要求商户给予响应，响应的内容是固定的new
        String txCode = noticeRequest.getTxCode();
        String plainText = noticeRequest.getPlainText();
        Map<String, Object> attributes;

        logger.info("支付通知明文 {}", plainText);
        CpcnPayData.CpcnPayDataBuilder dataBuilder = CpcnPayData.builder()
                .plainText(plainText).txCode(txCode).success(true);

        switch (txCode) {
            case CpcnStatus.CODE5018:
            case CpcnStatus.CODE2018:
                // 支付成功处理逻辑
                logger.trace("[TxName] = [支付成功处理逻辑]");
                Notice5018Request notice5018 = new Notice5018Request(noticeRequest.getDocument());
                attributes = IMapUtil.toMap(notice5018);
                dataBuilder
                        .txSn(notice5018.getTxSN())
                        .attributes(attributes).original(notice5018)
                        .tradeNo(notice5018.getOrderNo())
                        .status(notice5018.getStatus())
                        // 响应码为 30 表示支付成功
                        .success(CpcnStatus.STATUS30.equals(notice5018.getStatus()));
                break;
            case CpcnStatus.CODE5028:
                // 退款结果通知
                logger.trace("[TxName] = [退款结果通知]");
                Notice5028Request notice5028 = new Notice5028Request(noticeRequest.getDocument());
                IMapUtil.toMap(notice5028);
                attributes = IMapUtil.toMap(notice5028);
                dataBuilder
                        .txSn(notice5028.getTxSN())
                        .attributes(attributes).original(notice5028)
                        .tradeNo(notice5028.getOrderNo())
                        .status(notice5028.getStatus())
                        // 响应码为 20 表示退款成功
                        .success(CpcnStatus.STATUS20.equals(notice5028.getStatus()));
                break;
            case CpcnStatus.CODE2048:
                // 分账指令结算通知
                logger.trace("[TxName]       = [分账指令结算通知]");
                Notice2048Request notice2048 = new Notice2048Request(noticeRequest.getDocument());
                IMapUtil.toMap(notice2048);
                attributes = IMapUtil.toMap(notice2048);
                dataBuilder
                        .attributes(attributes)
                        // .tradeNo(notice2048.getOrderNo())
                        .original(notice2048);
                break;
            case CpcnStatus.CODE5598:
                // 交易批次状态通知
                Notice5598Request notice5598 = new Notice5598Request(noticeRequest.getDocument());
                logger.trace("[TxName]       = [交易批次状态通知]");
                IMapUtil.toMap(notice5598);
                attributes = IMapUtil.toMap(notice5598);
                dataBuilder
                        .attributes(attributes).original(notice5598)
                        // .tradeNo(notice5598.getOrderNo())
                        .status(notice5598.getStatus());
                break;
            case CpcnStatus.CODE5568:
                // 海关申报结果通知
                Notice5568Request notice5568 = new Notice5568Request(noticeRequest.getDocument());
                logger.trace("[TxName]       = [海关申报结果通知]");
                IMapUtil.toMap(notice5568);
                attributes = IMapUtil.toMap(notice5568);
                dataBuilder
                        .attributes(attributes)
                        // .tradeNo(notice5568.getOrderNo())
                        .original(notice5568);
                break;
            case CpcnStatus.CODE5658:
                // 外卡支付结果通知
                Notice5658Request notice5658 = new Notice5658Request(noticeRequest.getDocument());
                logger.trace("[TxName]       = [外卡支付结果通知]");
                IMapUtil.toMap(notice5658);
                attributes = IMapUtil.toMap(notice5658);
                dataBuilder
                        .attributes(attributes).original(notice5658)
                        .tradeNo(notice5658.getOrderNo())
                        .status(notice5658.getStatus());
                break;
            case CpcnStatus.CODE4618:
                // 开户 / 绑卡结果通知
                logger.trace("[TxName]       = [开户 / 绑卡结果通知]");
                Notice4618Request notice4618 = new Notice4618Request(noticeRequest.getDocument());
                IMapUtil.toMap(notice4618);
                attributes = IMapUtil.toMap(notice4618);
                dataBuilder
                        .attributes(attributes).original(notice4618)
                        .tradeNo(notice4618.getSourceTxSN())
                        .status(notice4618.getStatus());
                break;
            case CpcnStatus.CODE7709:
                // 签约结果通知
                logger.trace("[TxName]       = [用户入网签约通知]");
                Notice7709Request notice7709 = new Notice7709Request(noticeRequest.getDocument());
                IMapUtil.toMap(notice7709);
                attributes = IMapUtil.toMap(notice7709);
                dataBuilder
                        .attributes(attributes).original(notice7709)
                        .tradeNo(notice7709.getApplyNo())
                        .status(notice7709.getStatus());
                break;
            case CpcnStatus.CODE5038:
                // 延迟分账结果通知
                logger.trace("[TxName]       = [延迟分账通知]");
                Notice5038Request notice5038 = new Notice5038Request(noticeRequest.getDocument());
                attributes = new HashMap<>();
                attributes.put(CpcnStatus.CODE5038, notice5038);
                dataBuilder
                        .attributes(attributes).original(notice5038)
                        .tradeNo(notice5038.getTxSN())
                        .status(notice5038.getStatus());
                break;
            case CpcnStatus.CODE4658:
                // 充值/提现/代付结果通知
                logger.trace("[TxName]       = [充值/提现/代付结果通知]");
                Notice4658Request notice4658 = new Notice4658Request(noticeRequest.getDocument());
                attributes = new HashMap<>();
                attributes.put(CpcnStatus.CODE4658, notice4658);
                dataBuilder
                        .attributes(attributes).original(notice4658)
                        .tradeNo(notice4658.getSourceTxSN())
                        .status(notice4658.getStatus());
                break;
            default:
                logger.error("错误的通知>>> plainText:[{}]", plainText);
        }
        return dataBuilder.build();
    }


    private static CpcnPayData convertPayData(TxBaseResponse txResponse) {
        CpcnPayData.CpcnPayDataBuilder dataBuilder = CpcnPayData.builder().txCode(txResponse.getCode())
                // 原始数据
                .original(txResponse)
                // 原始数据转 Map
                .attributes(IMapUtil.toMap(txResponse));
        // status = 30 时表示支付成功
        if (txResponse instanceof Tx5016Response) {
            Tx5016Response tx5016Response = (Tx5016Response) txResponse;
            dataBuilder.success(CpcnStatus.STATUS30.equals(tx5016Response.getStatus()))
                    .status(tx5016Response.getStatus())
                    .tradeNo(tx5016Response.getOrderNo());
        }
        if (txResponse instanceof Tx5026Response) {
            Tx5026Response tx5026Response = (Tx5026Response) txResponse;
            dataBuilder.success(CpcnStatus.STATUS20.equals(tx5026Response.getStatus()))
                    .status(tx5026Response.getStatus())
                    .tradeNo(tx5026Response.getOrderNo());
        }
        return dataBuilder.build();
    }
}
