package com.xtechcn.cloud.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtechcn.cloud.customer.entity.PostAddress;
import com.xtechcn.cloud.customer.model.po.PostAddressParam;
import com.xtechcn.cloud.customer.model.vo.PostAddressView;

import java.util.List;

/**
* 邮递地址信息表
*
* @author hanjie
* @since 2025-09-11 17:59:28
*/
public interface PostAddressService extends IService<PostAddress> {
    /**
     * 封装数据
     * @param postAddressList
     * @return
     */
    List<PostAddressView> returnViewHandler(List<PostAddress> postAddressList);

    /**
     * 参数转实体
     * @param postAddressParam
     * @return
     */
    PostAddress conventEntity(PostAddressParam postAddressParam);

    /**
     * 唯一性校验
     * @param postAddress
     */
    void uniqueCheck(PostAddress postAddress);

    /**
     * 判断是否存在
     * @param postAddress
     */
    void exist(PostAddress postAddress);
}