package com.xtechcn.cloud.customer.typehandler;

import com.xtechcn.cloud.customer.model.dto.RegionDto;
import com.xtechcn.common.mybatis.typehandler.ListTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hanjie
 * @since 2025-9-18 14:24:50
 */

@Component
@MappedTypes(value = {List.class})
@MappedJdbcTypes(value = {JdbcType.VARCHAR})
public class RegionTypeHandler implements ListTypeHandler<RegionDto> {

}
