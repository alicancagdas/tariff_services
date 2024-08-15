package com.ispark.tariff_service.kafka;

import com.ispark.tariff_service.model.Tariff;
import com.ispark.tariff_service.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TariffKafkaConsumer {

    @Autowired
    private TariffService tariffService;

    @KafkaListener(topics = "tariff-topic", groupId = "tariff-group")
    public void listen(Tariff tariff) {
        // Kafka'dan gelen tarifeyi al ve işleme koy
        System.out.println("Kafka Consumer - Received tariff: " + tariff.getTarifeNo());

        // Tariff Service'e ilet ve veritabanına kaydet
        tariffService.createTariffFromKafka(tariff);
    }
}
