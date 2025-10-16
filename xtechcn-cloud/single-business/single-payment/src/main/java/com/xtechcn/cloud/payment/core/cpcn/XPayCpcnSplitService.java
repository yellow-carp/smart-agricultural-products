package com.xtechcn.cloud.payment.core.cpcn;

import com.xtechcn.commom.payment.api.XPaySplitApi;
import com.xtechcn.commom.payment.config.properties.XPayConfigProperties;
import com.xtechcn.commom.payment.constants.XPayChannelEnum;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.model.DefaultSplitItemModel;
import com.xtechcn.commom.payment.model.XPaySplitModel;
import com.xtechcn.common.cpcn.api.CpcnTxRequestExecutor;
import com.xtechcn.common.cpcn.api.builder.Tx5031Builder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import payment.api.system.PaymentEnvironment;
import payment.api.tx.aggregate.Tx5031Request;
import payment.api.vo.SplitItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 中金支付分账
 *
 * @author Alay
 * @since 2024-09-26 10:52
 */
@RequiredArgsConstructor
public class XPayCpcnSplitService implements XPaySplitApi {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final XPayConfigProperties xPayConfigProperties;
    private final CpcnTxRequestExecutor cpcnTxRequestExecutor;

    @Override
    public boolean supports(String channel) {
        return XPayChannelEnum.CPCN_PAY.channel().equals(channel);
    }

    @Override
    public Object split(XPaySplitModel splitModel) throws XPayException {
        XPayConfigProperties.CpcnPay cpcnPay = xPayConfigProperties.getCpcnPay();
        List<SplitItem> splitItems = new ArrayList<>();
        // 租户ID
        String tenantId = null;
        for (XPaySplitModel.SplitItem item : splitModel.items()) {
            DefaultSplitItemModel defaultSplitItem = (DefaultSplitItemModel) item;
            // 若分账金额为0，则跳过该分账方
            if (BigDecimal.ZERO.intValue() >= defaultSplitItem.splitAmount().intValue()) continue;
            // 进行部分分账交易时,分账域中不能上传分账对象为收款人的明细

            // 去除收款方分账明细
            if (defaultSplitItem.ownerId().equals(tenantId)) continue;

            SplitItem splitItem = new SplitItem();
            splitItem.setSplitTxSN(defaultSplitItem.getId());
            splitItem.setSplitUserID(defaultSplitItem.ownerId());
            splitItem.setSplitAmount(String.valueOf(defaultSplitItem.splitAmount().intValue()));

            splitItems.add(splitItem);
        }

        Tx5031Request tx5031Request = Tx5031Builder.of()
                // 机构编码
                .institutionID(PaymentEnvironment.institutionID)
                // 延迟分账交易流水号
                .txSN(splitModel.splitNo())
                // 原支付交易流水号
                .paymentTxSN(splitModel.batchNo())
                // 本次分账后剩余资金处理方式：1=结算给收款人，2=等待后续分账
                .remainFundsProcess("1")
                // 通知地址
                .noticeURL(cpcnPay.getNotifyUrl().getSplitNotify())
                // 分账结算域
                .splits(splitItems)
                .build();

        try {
            return cpcnTxRequestExecutor.request(tx5031Request);
        } catch (Exception ex) {
            logger.error("cpcn pay execute-split failed,message:[{}]", ex.getMessage());
            throw new XPayException(ex);
        }
    }

    @Override
    public Object rollback(XPaySplitModel splitModel) throws XPayException {
        return null;
    }

    @Override
    public Object rollbackQuery(XPaySplitModel splitModel) throws XPayException {
        return null;
    }

}
