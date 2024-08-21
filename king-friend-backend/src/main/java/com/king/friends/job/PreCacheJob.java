package com.king.friends.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.king.friends.model.domain.User;
import com.king.friends.service.UserService;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ClassName:PreCacheJob
 * Description:缓存预热
 *
 * @Author:kinglearn
 * @Create2024/7/13 17:17
 * @version1.0
 */
@Component
@Slf4j
public class PreCacheJob {
    @Resource
    private UserService userService;
    //    重点用户
    public List<Long> mainUserList = Arrays.asList(1L);
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private RedissonClient redissonClient;

    //    每天执行，预热推荐用户
    @Scheduled(cron = "0 35 19 * * *")
    public void doCacheRecommendUser() throws InterruptedException {
         RLock lock = redissonClient.getLock("user:preJob:doCache:lock");
        try {
            if (lock.tryLock(0,-1,TimeUnit.MILLISECONDS)){
                System.out.println("getLock"+Thread.currentThread().getId());
                for (Long userId : mainUserList) {
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1,20 ) ,queryWrapper);
                    String redisKey = String.format("partner:user:recommend:%s", userId);
                    //如果有缓存，直接查缓存
                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
                    try {
                        valueOperations.set(redisKey,userPage,100000, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        log.error("redis set key error");
                    }
                }
            }

        } catch (InterruptedException e) {
                throw new RuntimeException(e);
        }finally {
            if (lock.isHeldByCurrentThread()){
                System.out.println("unlock"+Thread.currentThread().getId());
                    lock.unlock();
            }
        }

    }
}
