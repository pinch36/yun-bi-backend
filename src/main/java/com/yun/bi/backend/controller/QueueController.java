package com.yun.bi.backend.controller;

import cn.hutool.json.JSONUtil;
import io.reactivex.rxjava3.internal.operators.completable.CompletableAmb;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池接口，用于测试
 */
@RestController
@RequestMapping("/queue")
@Slf4j
@Profile({"dev","local"})
public class QueueController {
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    @GetMapping("/add")
    public void add(String name){
        CompletableFuture.runAsync(()->{
           log.info("执行任务:"+name+",执行人:"+Thread.currentThread().getName());
            try {
                Thread.sleep(600000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },threadPoolExecutor);
    }
    @GetMapping("/get")
    public String get(){
        Map<String, Object> map = new HashMap<>();
        int size = threadPoolExecutor.getQueue().size();
        map.put("队列长度",size);
        long taskCount = threadPoolExecutor.getTaskCount();
        map.put("任务数量",taskCount);
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        map.put("已完成任务数量",completedTaskCount);
        int activeCount = threadPoolExecutor.getActiveCount();
        map.put("正在工作的线程数量",activeCount);
        return JSONUtil.toJsonStr(map);
    }
}
