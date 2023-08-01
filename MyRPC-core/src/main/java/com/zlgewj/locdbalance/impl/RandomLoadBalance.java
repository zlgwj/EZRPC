package com.zlgewj.locdbalance.impl;

import com.zlgewj.locdbalance.LoadBalance;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 随机算法负载均衡
 *
 * @Author zlgewj
 * @Since 2023/8/1
 */

public class RandomLoadBalance implements LoadBalance {
    @Override
    public String doSelect(List<String> list) {
        if (list.size() == 1) {
            return list.get(0);
        }
        Random random = new Random();
        random.setSeed(new Date().getTime());
        int i = random.nextInt();
        i = i % list.size();
        return list.get(i);
    }
}
