package com.zlgewj;

import com.alibaba.nacos.api.exception.NacosException;
import com.zlgewj.config.RpcConfiguration;
import org.junit.Test;

/**
 * test
 *
 * @Author zlgewj
 * @Since 2023/8/2
 */

public class NacosTest {
    @Test
    public void testNacos() throws NacosException {
        System.out.println(RpcConfiguration.getLoadbalance());
    }
}
