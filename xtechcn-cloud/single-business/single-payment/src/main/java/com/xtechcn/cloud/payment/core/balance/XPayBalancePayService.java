package com.xtechcn.cloud.payment.core.balance;

import com.xtechcn.cloud.payment.constants.AttributeKeyNames;
import com.xtechcn.cloud.payment.constants.PaymentResult;
import com.xtechcn.commom.payment.api.AbstractXPayService;
import com.xtechcn.commom.payment.channel.XPayChannelRepository;
import com.xtechcn.commom.payment.exceptions.XPayException;
import com.xtechcn.commom.payment.handler.XPayFailureHandler;
import com.xtechcn.commom.payment.handler.XPaySuccessHandler;
import com.xtechcn.commom.payment.model.*;
import com.xtechcn.commom.payment.repository.XPayWalletRepository;
import com.xtechcn.common.core.lang.MoneyPenny;
import com.xtechcn.common.core.utils.ICryptoUtil;
import com.xtechcn.common.core.utils.IStrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 站内余额支付
 *
 * @author Alay
 * @since 2023-06-11 03:32
 */
public class XPayBalancePayService extends AbstractXPayService {

    private final PasswordEncoder passwordEncoder;
    private final XPayWalletRepository walletRepository;

    public XPayBalancePayService(String channel, XPayChannelRepository channelRepository, XPayFailureHandler payFailureHandler,
                                 XPaySuccessHandler paySuccessHandler, PasswordEncoder passwordEncoder, XPayWalletRepository walletRepository) {
        super(channel, paySuccessHandler, payFailureHandler, channelRepository);
        this.passwordEncoder = passwordEncoder;
        this.walletRepository = walletRepository;
    }


    @Override
    public Object executePay(XPayTradeModel tradeModel, HttpServletRequest request, HttpServletResponse response) throws XPayException {
        // 钱包数据
        XPayWalletOption walletOption = (XPayWalletOption) walletRepository.findOne(tradeModel.payer());
        String rawPassword = tradeModel.attribute(AttributeKeyNames.PASSWORD);
        if (IStrUtil.isBlank(rawPassword)) {
            throw new XPayException(PaymentResult.CHANGE_PASSWORD_NOT_SET.getMessage());
        }
        // 验证支付密码
        boolean checkPassword = passwordEncoder.matches(ICryptoUtil.aesDecrypt(rawPassword), walletOption.password());
        // 密码不匹配
        if (!checkPassword) throw new XPayException("密码错误");

        // 实际支付金额
        MoneyPenny payAmount = tradeModel.payAmount();
        boolean abundant = walletOption.sufficient(payAmount);
        if (!abundant) {
            // 余额不足
            throw new XPayException("Account Insufficient  Balance");
        }
        // 余额费用足够
        walletRepository.paying(walletOption, payAmount);
        return true;
    }


    @Override
    public XPayResult<?> queryPayResult(XPayTradeModel tradeModel) throws XPayException {
        // 支付成功处理相关逻辑(余额支付只要走到这一步说明都已经支付成功了)
        return DefaultXPayResult.pay().success(true).build();
    }


    @Override
    public Object transfer(XPayTransferModel transferModel) throws XPayException {
        // 付款方钱包
        XPayWalletOption walletOption = (XPayWalletOption) walletRepository.findOne(transferModel.payer());
        // 实际支付金额
        MoneyPenny transferAmount = transferModel.amount();
        boolean abundant = walletOption.sufficient(transferAmount);
        if (!abundant) {
            // 余额不足
            throw new XPayException("account Insufficient  Balance");
        }
        // 收款方钱包
        XPayWalletModel payeeWallet = walletRepository.findOne(transferModel.payee());

        // 执行转账
        boolean isOk = walletRepository.transfer(walletOption, payeeWallet, transferAmount);
        if (isOk) {
            // 转账成功之后需要做的事情
        }
        return isOk;
    }


    @Override
    public XPayResult<?> refundTrade(XPayRefundModel refundModel) throws XPayException {
        // 付款方钱包
        XPayWalletModel walletModel = walletRepository.findOne(refundModel.payer());
        boolean refunded = walletRepository.refund(walletModel, refundModel.refundAmount());
        if (!refunded) {
            // 退款失败
            throw new XPayException("refund amount failed");
        }
        // 退款成功相关逻辑(余额退款属于内部服务退款逻辑,直接成功)
        return DefaultXPayResult.refund().success(true).build();
    }

    @Override
    public XPayResult<?> queryRefundResult(XPayRefundModel refundModel) throws XPayException {
        // 退款成功相关逻辑(余额退款属于内部服务退款逻辑,直接成功)
        return DefaultXPayResult.refund().success(true).build();
    }

}
