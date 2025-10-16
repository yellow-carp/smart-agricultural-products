package com.xtechcn.cloud.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.api.system.model.UserProfile;
import com.xtechcn.cloud.customer.entity.Purchaser;
import com.xtechcn.cloud.customer.entity.PurchaserConfig;
import com.xtechcn.cloud.customer.model.TradeCheckModel;
import com.xtechcn.cloud.customer.model.po.PrCheckCodeParam;
import com.xtechcn.cloud.customer.model.po.PurchaserMaParam;
import com.xtechcn.cloud.customer.model.po.PurchaserParam;
import com.xtechcn.cloud.customer.model.vo.PurchaserView;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

/**
* 采购商管理
*
* @author hanjie
* @since 2025-09-16 09:36:46
*/
public interface PurchaserService extends IService<Purchaser> {

    /**
     * 参数转换
    */
    Purchaser conventEntity(PurchaserParam purchaserParam);

    /**
     * 唯一性校验
     */
    void uniqueCheck(Purchaser purchaser);

    /**
     * 新增/修改初始化
     */
    void upsertInit(Purchaser purchaser);

    /**
     * 根据用户id查询
     */
    Purchaser findUserId(String userId);

    /**
     * 检验邀请码
     */
    boolean checkCode(Purchaser purchaser, PrCheckCodeParam prCheckCodeParam, PurchaserConfig config);

    /**
     * 删除缓存
     */
    void removeCache(String userId);

    /**
     * 数据处理
     */
    List<PurchaserView> dataHandle(List<Purchaser> records, Map<String, UserProfile> userProfileMap);

    /**
     * 参数转实体
     * @param purchaserMaParam
     * @return
     */
    Purchaser conventMaEntity(PurchaserMaParam purchaserMaParam);

    /**
     * 修改采购商状态
     */
    boolean updateState(String userId, Integer code, Integer registerModel);
}