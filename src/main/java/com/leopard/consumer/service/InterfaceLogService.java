package com.leopard.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InterfaceLogService {

    /**
     * 保存接口操作日志记录
     *
     * @param recordLogMsg 日志信息
     */
    @Async("serviceTaskExecutor")
    public void addInterfaceLogInfo(String recordLogMsg) {

        //运行业务逻辑
    }

}
