package com.zlgewj.locdbalance.impl;

import com.zlgewj.locdbalance.LoadBalance;

import java.util.List;

/**
 * 轮询算法
 *
 * @Author zlgewj
 * @Since 2023/8/1
 */

public class RotateLoadBalance implements LoadBalance {
    private static int idx = 0;
    @Override
    public String doSelect(List<String> list) {
        if (list.size() == 1) return list.get(0);
        int rst = idx % list.size();
        idx ++;
        if (idx == Integer.MAX_VALUE) {
            idx = 0;
        }
        return list.get(rst);
    }
}
