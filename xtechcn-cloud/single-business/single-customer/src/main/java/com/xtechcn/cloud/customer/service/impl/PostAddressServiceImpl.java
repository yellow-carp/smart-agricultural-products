package com.xtechcn.cloud.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtechcn.cloud.customer.entity.Area;
import com.xtechcn.cloud.customer.entity.PostAddress;
import com.xtechcn.cloud.customer.entity.Unit;
import com.xtechcn.cloud.customer.mapper.PostAddressMapper;
import com.xtechcn.cloud.customer.model.po.PostAddressParam;
import com.xtechcn.cloud.customer.model.vo.AreaView;
import com.xtechcn.cloud.customer.model.vo.PostAddressView;
import com.xtechcn.cloud.customer.service.PostAddressService;
import com.xtechcn.common.core.exceptions.UniqueException;
import com.xtechcn.common.core.result.FailedResult;
import com.xtechcn.common.core.utils.ICollUtil;
import com.xtechcn.common.security.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
* 邮递地址信息表
*
* @author hanjie
* @since 2025-09-11 17:59:28
*/
@Service
public class PostAddressServiceImpl extends ServiceImpl<PostAddressMapper, PostAddress> implements PostAddressService {
    @Override
    public List<PostAddressView> returnViewHandler(List<PostAddress> postAddressList) {
        List<PostAddressView> postAddressViewList = new ArrayList<>();
        for(PostAddress postAddress : postAddressList){
            PostAddressView postAddressView = new PostAddressView();
            postAddressView.setId(postAddress.getId());
            postAddressView.setUserId(postAddress.getUserId());
            postAddressView.setRegionCode(postAddress.getRegionCode());
            postAddressView.setRegionFullPath(postAddress.getRegionFullPath());
            postAddressView.setRegionFullName(postAddress.getRegionFullName());
            postAddressView.setZip(postAddress.getZip());
            postAddressView.setDetail(postAddress.getDetail());
            postAddressView.setComName(postAddress.getComName());
            postAddressView.setContact(postAddress.getContact());
            postAddressView.setContactTel(postAddress.getContactTel());
            postAddressView.setIsDefault(postAddress.getIsDefault());
            postAddressViewList.add(postAddressView);
        }
        return postAddressViewList;
    }

    @Override
    public PostAddress conventEntity(PostAddressParam postAddressParam) {
        // 获取当前登录用户id
        String userId = SecurityUtil.userId();
        postAddressParam.setUserId(userId);

        PostAddress postAddress = new PostAddress();
        postAddress.setId(postAddressParam.getId());
        postAddress.setUserId(postAddressParam.getUserId());
        postAddress.setRegionCode(postAddressParam.getRegionCode());
        postAddress.setRegionFullPath(postAddressParam.getRegionFullPath());
        postAddress.setRegionFullName(postAddressParam.getRegionFullName());
        postAddress.setZip(postAddressParam.getZip());
        postAddress.setDetail(postAddressParam.getDetail());
        postAddress.setComName(postAddressParam.getComName());
        postAddress.setContact(postAddressParam.getContact());
        postAddress.setContactTel(postAddressParam.getContactTel());
        postAddress.setIsDefault(postAddressParam.getIsDefault());
        return postAddress;
    }

    @Override
    public void uniqueCheck(PostAddress postAddress) {
        List<PostAddress> postAddressList = this.list();
        if (postAddressList.isEmpty()) return;
        for (PostAddress ps : postAddressList) {
            if (ps.equalsAndExcludeSelf(postAddress)) {
                throw new UniqueException("已存在相同邮递地址信息");
            }
        }
    }

    @Override
    public void exist(PostAddress postAddress) {
        PostAddress address = this.getById(postAddress.getId());
        if(address==null) throw new UniqueException(FailedResult.NOT_FOUNT);
    }
}
