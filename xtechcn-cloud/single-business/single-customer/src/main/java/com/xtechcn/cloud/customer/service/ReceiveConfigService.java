package com.xtechcn.cloud.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.customer.entity.Area;
import com.xtechcn.cloud.customer.entity.ReceiveConfig;
import com.xtechcn.cloud.customer.model.po.ReceiveConfigParam;
import com.xtechcn.cloud.customer.model.vo.ReceiveConfigView;

import java.util.List;

/**
* 自动收货配置
*
* @author hanjie
* @since 2025-09-11 17:59:28
*/
public interface ReceiveConfigService extends IService<ReceiveConfig> {

    /**
     * 添加数据插入到自动收货配置表
     * @param area
     */
    void init(Area area);

    /**
     * 封装数据返回
     * @param receiveConfigList
     * @return
     */
    List<ReceiveConfigView> returnViewHandler(List<ReceiveConfig> receiveConfigList);

    /**
     * 参数转实体
     * @param receiveConfigParam
     * @return
     */
    ReceiveConfig conventEntity(ReceiveConfigParam receiveConfigParam);

    /**
     * 重复性验证
     * @param receiveConfig
     */
    void uniqueCheck(ReceiveConfig receiveConfig);

    /**
     * 缓存查询
     * @param id
     * @return
     */
    ReceiveConfig findAndCache(String id);

    /**
     * 缓存删除
     * @param id
     */
    void removeCache(String id);
}