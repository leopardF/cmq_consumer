package com.leopard.consumer.queue;

import com.qcloud.cmq.client.common.ClientConfig;
import com.qcloud.cmq.client.consumer.Consumer;
import com.qcloud.cmq.client.consumer.DeleteResult;
import com.qcloud.cmq.client.exception.MQClientException;
import com.qcloud.cmq.client.exception.MQServerException;
import com.leopard.consumer.service.InterfaceLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 消费者配置类
 */
@Component
@Slf4j
public class MqConsumerConfig {

    static Consumer consumer = new Consumer();
    @Value("${mq.tencent.nameServerAddress}")
    private String nameServerAddress;
    @Value("${mq.tencent.secretId}")
    private String secretId;
    @Value("${mq.tencent.secretKey}")
    private String secretKey;
    @Value("${mq.tencent.batchPullNumber}")
    private int batchPullNumber;
    @Value("${mq.tencent.retryTimesWhenSendFailed}")
    private int retryTimesWhenSendFailed;
    @Value("${mq.tencent.pullWaitSeconds}")
    private int pollingWaitSeconds;
    @Value("${mq.tencent.requestTimeoutMs}")
    private int requestTimeoutMs;
    @Value("${mq.send.queue}")
    private String queueString;

    @Autowired
    private InterfaceLogService interfaceLogService;

    @PostConstruct
    public void init() {

        consumer.setNameServerAddress(nameServerAddress);
        consumer.setAppId(secretId);
        consumer.setUin(secretKey);
        consumer.setSignMethod(ClientConfig.SIGN_METHOD_SHA256);
        consumer.setBatchPullNumber(batchPullNumber);
        consumer.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);
        consumer.setPollingWaitSeconds(pollingWaitSeconds);
        consumer.setRequestTimeoutMS(requestTimeoutMs);

        try {
            //启动消费者
            consumer.start();
            log.info(" consumer start Success ! ");
            //启动监听者监听队列
            consumer.subscribe(queueString, new ConsumerMsgListener());
            log.info(" consumer - subscribe start Success ! ");
        } catch (MQClientException e) {
            e.printStackTrace();
            log.info(" consumer start Fail ! {}", e);
        } catch (MQServerException e) {
            e.printStackTrace();
            log.info(" consumer - subscribe start Fail ! {}", e);
        }
        MqConsumerConfig.getInstance().interfaceLogService = this.interfaceLogService;
    }

    /**
     * 实现单例 start
     */
    private static class SingletonHolder {
        private static final MqConsumerConfig INSTANCE = new MqConsumerConfig();
    }

    private MqConsumerConfig() {
    }

    public static final MqConsumerConfig getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 实现单例 end
     */
    public InterfaceLogService getInterfaceLogService() {
        return MqConsumerConfig.getInstance().interfaceLogService;
    }

    /**
     * 同步确认消息
     *
     * @param receiptHandle
     */
    public void deleteMsg(long receiptHandle) {

        try {
            // 同步确认消息
            DeleteResult delResult = consumer.deleteMsg(queueString, receiptHandle);
            if (delResult.getReturnCode() != 0) {
                log.info("delete msg error, ret:" + delResult.getReturnCode() + " ErrMsg:" + delResult.getErrorMessage());
            }
        } catch (MQClientException e) {
            e.printStackTrace();
            log.info(" DeleteResult del Fail ! {}", e);
        } catch (MQServerException e) {
            e.printStackTrace();
            log.info(" DeleteResult - del start Fail ! {}", e);
        }
    }
}
