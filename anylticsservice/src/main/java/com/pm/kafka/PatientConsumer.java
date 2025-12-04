package com.pm.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

public class PatientConsumer {
   private static final Logger log= LoggerFactory.getLogger(PatientConsumer.class);

   @KafkaListener(topics = "patient-created",groupId = "patient-group")
    public void consumeEvent(String event){
       log.info("Received Patient event -> {}",event);
   }
}
