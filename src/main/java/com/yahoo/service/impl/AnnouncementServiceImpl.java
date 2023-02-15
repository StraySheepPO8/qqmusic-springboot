package com.yahoo.service.impl;

import com.rabbitmq.client.Channel;
import com.yahoo.pojo.vo.AnnouncementVO;
import com.yahoo.service.inter.AnnouncementService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    public RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     *
     * @param msg 消息的pojo类
     */
    @Override
    @Transactional
    public void sendMsg(AnnouncementVO msg) {
        rabbitTemplate.setChannelTransacted(true);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
        String currentTime = simpleDateFormat.format(System.currentTimeMillis());
        msg.publishTime = currentTime;
        rabbitTemplate.convertAndSend("announcement_fanout_x", "", msg);
    }


    /**
     * 手动消费消息
     */
    @Override
    @Transactional
    public AnnouncementVO getMsg() {
        rabbitTemplate.setChannelTransacted(true);
        AnnouncementVO msg = (AnnouncementVO) rabbitTemplate.receiveAndConvert("announcement.q");
//        String msg = (String) rabbitTemplate.receiveAndConvert("announcement_common");
        assert msg != null;
        System.out.println("--------接受的的消息是:" + msg.toString());
        return msg;
    }

    /**
     * 自动消费，手动应答
     */
//    @RabbitListener(queues = {"announcement.q"})
    public void manualAck(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag ) throws Exception {
        System.out.println("---==========---------message:" + message);
        try {

            /*
              无异常就确认消息
              basicAck(long deliveryTag, boolean multiple)
              deliveryTag:取出来当前消息在队列中的的索引;
              multiple:为true的话就是批量确认,如果当前deliveryTag为5,那么就会确认
              deliveryTag为5及其以下的消息;一般设置为false
             */
            int i = 1 / 0;
            channel.basicAck(tag, false);
        } catch (Exception e) {
            /*
              有异常就绝收消息
              basicNack(long deliveryTag, boolean multiple, boolean requeue)
              requeue:true为将消息重返当前消息队列,还可以重新发送给消费者;
                      false:将消息丢弃
             */
            System.out.println("a");
            channel.basicNack(tag, false, true);
            System.out.println("b");

        }

    }


}
