package com.tushang.study.controller;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SettableListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static java.time.temporal.ChronoField.MILLI_OF_SECOND;

@RestController
public class KafkaController {
    private final static Logger logger = LoggerFactory.getLogger(KafkaController.class);

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @RequestMapping(value = "/send/{topic}/{value}",method = RequestMethod.GET)
    public void sendMeessageTotopic1(@PathVariable String topic, @PathVariable String value) {
        logger.info("start send message to {}",topic);
        long aLong = LocalDateTime.now().getLong(MILLI_OF_SECOND);
        ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(topic, value);
        send.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(SendResult<String, String> stringStringSendResult) {
                RecordMetadata recordMetadata = stringStringSendResult.getRecordMetadata();
                System.out.println("partition:"+recordMetadata.partition()+".offset:"+recordMetadata.offset()+".time:"+(LocalDateTime.now().getLong(MILLI_OF_SECOND)-aLong));
            }
        });
    }
}
