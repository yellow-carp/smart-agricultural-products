package com.xtechcn.cloud.product.utils;

/**
 * 商品私有工具类
 *
 * @author Weijixiao
 * @since 2025-09-17 11:51
 */
public class ProductUtil {

    public static String toBinaryStr(Integer toMarket) {
        StringBuilder sb = new StringBuilder().append(toMarket).reverse();
        for (int i = 1; i <= 3; i++) {
            if (sb.length() < i) {
                sb.append(0);
            }
        }
        return sb.reverse().toString();
    }


    public static String markBit2Zero(String binaryStr, int bitIndex) {
        StringBuilder sb = new StringBuilder().append(binaryStr).reverse();
        for (int i = 0; i <= 3; i++) {
            if (i == bitIndex && sb.length() > i) {
                sb.setCharAt(i, '0');
            }
            if (sb.length() < i) {
                sb.append(0);
            }
        }
        return sb.reverse().toString();
    }


    public static String markBit2One(String binaryStr, int bitIndex) {
        StringBuilder sb = new StringBuilder().append(binaryStr).reverse();
        for (int i = 0; i <= 3; i++) {
            if (i == bitIndex && sb.length() > i) {
                sb.setCharAt(i, '1');
            }
            if (sb.length() < i) {
                sb.append(0);
            }
        }
        return sb.reverse().toString();
    }

}
