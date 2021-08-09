package com.accenture.user.service;

import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SQSService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQSService.class);
    private final AmazonSQS amazonSQS;
    private final String endpoint;

    @Autowired
    public SQSService(AmazonSQS amazonSQS,
                      @Value("${sqs.end-point.url}") String endpoint) {
        this.amazonSQS = amazonSQS;
        this.endpoint = endpoint;
    }

    public void pushToSQS(String messageBody, String name) {
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("Name",
                new MessageAttributeValue()
                        .withStringValue(name)
                        .withDataType("String"));
        SendMessageRequest message = new SendMessageRequest()
                .withQueueUrl(this.endpoint)
                .withMessageBody(messageBody)
                .withMessageAttributes(messageAttributes)
                .withDelaySeconds(5);
        SendMessageResult result = this.amazonSQS.sendMessage(message);
        LOGGER.info("message has been sent successfully");
    }
}
