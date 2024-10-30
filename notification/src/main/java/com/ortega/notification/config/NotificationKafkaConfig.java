package com.ortega.notification.config;

import com.ortega.notification.event.account.AccountEvent;
import com.ortega.notification.event.customer.CustomerEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class NotificationKafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, CustomerEvent> customerConsumerFactory() {
        return consumerFactory("customer-group", CustomerEvent.class);
    }

    @Bean
    public ConsumerFactory<String, AccountEvent> accountConsumerFactory() {
        return consumerFactory("account-group", AccountEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CustomerEvent> customerEventKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory(customerConsumerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AccountEvent> accountEventKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory(accountConsumerFactory());
    }

    private <T> KafkaTemplate<String, T> kafkaTemplate(Class<T> eventType) {
        Map<String, Object> producerConfigs = new HashMap<>();
        producerConfigs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerConfigs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfigs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerConfigs));
    }

    private <T> ConsumerFactory<String, T> consumerFactory(String groupId, Class<T> eventType) {
        Map<String, Object> consumerConfigs = new HashMap<>();
        consumerConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerConfigs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfigs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerConfigs.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(consumerConfigs, new StringDeserializer(), new JsonDeserializer<>(eventType));
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerContainerFactory(ConsumerFactory<String, T> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

}
