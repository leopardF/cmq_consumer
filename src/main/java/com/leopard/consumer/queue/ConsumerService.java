package com.leopard.consumer.queue;

import com.leopard.utils.cmq.producer.config.MqProducerConfig;
import com.leopard.utils.cmq.producer.config.ProducerService;
import com.qcloud.cmq.CMQClientException;
import com.qcloud.cmq.Message;
import com.qcloud.cmq.Queue;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 生产者服务
 *
 * @author
 */
@Service
public class ConsumerService {

    private Logger logger = LoggerFactory.getLogger(ProducerService.class);

    @Value("${mq.send.queue}")
    private String queueString;

    /**
     * 发送消息（队列queue）
     *
     * @return msgId 消息ID
     */
    public String consumerMsg() {

        if (!MqProducerConfig.OPEN_ACCOUNT) {
            //没配置账号，无法接收
            logger.error(" not config account ! ");
            return null;
        }
        if (StringUtils.isBlank(queueString)) {
            //没有配置发送队列
            logger.error(" not config consumer queue ! ");
            return null;
        }

        // 同步单条发送消息
        Queue queue = MqConsumerConfig.account.getQueue(queueString);
        if (null == queue) {
            logger.error(" queue not found ! ");
            return null;
        }
        try {
            //接收单条消息
            System.out.println("---------------recv message ...---------------");
            Message msg = queue.receiveMessage();

            System.out.println("msgId:" + msg.msgId);
            System.out.println("msgBody:" + msg.msgBody);
            System.out.println("receiptHandle:" + msg.receiptHandle);
            System.out.println("enqueueTime:" + msg.enqueueTime);
            System.out.println("nextVisibleTime:" + msg.nextVisibleTime);
            System.out.println("firstDequeueTime:" + msg.firstDequeueTime);
            System.out.println("dequeueCount:" + msg.dequeueCount);

//            删除消息   （确认消费后，移除队列）
//            System.out.println("---------------delete message ...---------------");
//            queue.deleteMessage(msg.receiptHandle);
//            System.out.println("receiptHandle:" + msg.receiptHandle +" deleted");
            return msg.msgBody;

            //批量接收消息
//            ArrayList<String> vtReceiptHandle = new ArrayList<String>(); //保存服务器返回的消息句柄，用于删除消息
//            System.out.println("---------------batch recv message ...---------------");
//            List<Message> msgList = queue.batchReceiveMessage(10);
//            System.out.println("recv msg count:" + msgList.size());
//            for (int i = 0; i < msgList.size(); i++) {
//                Message msg1 = msgList.get(i);
//                System.out.println("msgId:" + msg1.msgId);
//                System.out.println("msgBody:" + msg1.msgBody);
//                System.out.println("receiptHandle:" + msg1.receiptHandle);
//                System.out.println("enqueueTime:" + msg1.enqueueTime);
//                System.out.println("nextVisibleTime:" + msg1.nextVisibleTime);
//                System.out.println("firstDequeueTime:" + msg1.firstDequeueTime);
//                System.out.println("dequeueCount:" + msg1.dequeueCount);
//                System.out.println();
//
//                vtReceiptHandle.add(msg1.receiptHandle);
//            }

//            批量删除消息
//            System.out.println("---------------batch delete message ...---------------");
//            queue.batchDeleteMessage(vtReceiptHandle);
//            for(int i=0;i<vtReceiptHandle.size();i++)
//                System.out.println("receiptHandle:" + vtReceiptHandle.get(i) + " deleted");
        } catch (CMQClientException e2) {
            logger.error("Client Exception, " + e2.toString());
            return null;
        } catch (Exception e) {
            logger.error("error..." + e.toString());
            return null;
        }
    }

}
