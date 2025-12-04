package com.pm.kafka;

import com.pm.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final Logger log= LoggerFactory.getLogger(KafkaProducer.class);

    @Value("${patient.topic.name}")
    private String topicName;

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
       this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Patient patient){
        PatientEvent event=new PatientEvent();
        event.setId(patient.getId().toString());
        event.setName(patient.getName());
        event.setEmail(patient.getEmail());
        event.setEventType("Patient_Created");

        try{
            kafkaTemplate.send(topicName,event);
            log.info("PatientCreated event sent -> {}",event);

        } catch (Exception e){
            log.error("Error sending kafka event", e.getMessage());
        }

    }
}
