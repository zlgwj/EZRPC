package com.zlgewj.locdbalance;

import com.zlgewj.extension.SPI;

import java.util.List;

/**
 * 负载均衡注解
 *
 * @Author zlgewj
 * @Since 2023/8/1
 */
@SPI
public interface LoadBalance {

    String doSelect(List<String> list);

}
