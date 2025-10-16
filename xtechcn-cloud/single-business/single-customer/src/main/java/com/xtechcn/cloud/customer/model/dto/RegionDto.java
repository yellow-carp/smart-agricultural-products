package com.xtechcn.cloud.customer.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 区域
 * @author Hanjie
 * @since 2024-04-17 11:30
 */
@Getter
@Setter
public class RegionDto {

    private String code;
    private String name;

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==this) return true;
        if (obj instanceof RegionDto other) {
            return Objects.equals(this.code, other.code);
        }
        return false;
    }
}
