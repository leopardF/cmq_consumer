package com.leopard.consumer.queue;

import com.alibaba.fastjson.JSONObject;
import com.qcloud.cmq.client.consumer.DeleteResult;
import com.qcloud.cmq.client.consumer.Message;
import com.qcloud.cmq.client.consumer.MessageListener;
import com.qcloud.cmq.client.exception.MQClientException;
import com.qcloud.cmq.client.exception.MQServerException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 消费者消息监听
 */
@Slf4j
public class ConsumerMsgListener implements MessageListener {


    @Override
    public List<Long> consumeMessage(String queue, List<Message> list) {

        for (Message message : list) {

            String logMsg = null;
            try {
                logMsg = JSONObject.parseObject(message.getData(), String.class);
                log.info("message data  ：" + logMsg);
                MqConsumerConfig.getInstance().getInterfaceLogService().addInterfaceLogInfo(logMsg);
            } catch (Exception e) {
                e.printStackTrace();
                log.info(" consume dispose message data format don't fit ！ data: {}", message.getData());
            }
            try {
                // 同步确认消息
                DeleteResult delResult = MqConsumerConfig.consumer.deleteMsg(queue, message.getReceiptHandle());
                if (delResult.getReturnCode() != 0) {
                    log.info("delete msg error, ret:" + delResult.getReturnCode() + " ErrMsg:" + delResult.getErrorMessage());
                }
            } catch (MQClientException e) {
                e.printStackTrace();
                log.info(" DeleteResult del Fail ! {}", e);
            } catch (MQServerException e) {
                e.printStackTrace();
                log.info(" DeleteResult - del start Fail ! {}", e);
            } catch (Exception e) {
                e.printStackTrace();
                log.info(" DeleteResult - A;;;;;;;;;;;;; start Fail ! {}", e);
            }
        }

        return null;
    }
}
