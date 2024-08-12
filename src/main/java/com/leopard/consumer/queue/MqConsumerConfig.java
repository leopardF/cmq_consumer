package com.leopard.consumer.queue;

import com.leopard.consumer.service.InterfaceLogService;
import com.qcloud.cmq.Account;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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

    /**
     * 账号是否开放标识
     */
    public static boolean OPEN_ACCOUNT = false;
    static Account account = null;

    @Value("${mq.tencent.nameServerAddress}")
    private String nameServerAddress;

    @Value("${mq.tencent.secretId}")
    private String secretId;

    @Value("${mq.tencent.secretKey}")
    private String secretKey;

    @Autowired
    private InterfaceLogService interfaceLogService;

    @PostConstruct
    public void init() {

        if (StringUtils.isNotBlank(nameServerAddress)
                && StringUtils.isNotBlank(secretId)
                && StringUtils.isNotBlank(secretKey)) {
            OPEN_ACCOUNT = true;
        }
        if (!OPEN_ACCOUNT) {
            log.error(" account info not config ! ");
            return;
        }
        //启动链接配置
        account = new Account(nameServerAddress, secretId, secretKey);
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
//    @Deprecated
    /*public void deleteMsg(long receiptHandle) {

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
    }*/
}
