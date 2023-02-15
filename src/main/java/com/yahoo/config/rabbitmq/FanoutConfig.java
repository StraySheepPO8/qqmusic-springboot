package com.yahoo.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfig {

    /**
     * 消息转换器
     * */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    /**
     * rabbitmq的事务管理器
     * */
    @Bean
    public RabbitTransactionManager rabbitTransactionManager(ConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }


//    ================交换机=================

    /**
     * @return FanoutExchange fanout_exchange_announcement
     * */
    @Bean
    public FanoutExchange announcement_fanout_x() {
        return new FanoutExchange("announcement_fanout_x", true, false);
    }


//    ================队列==================

    /**
     * @return Queue announcement_komorebi
     * */
    @Bean
    public Queue announcement_q(){
        return new Queue("announcement.q", true);
    }


//    ================绑定关系================

    @Bean
    public Binding q_x(){
        return BindingBuilder.bind(announcement_q()).to(announcement_fanout_x());
    }


}
