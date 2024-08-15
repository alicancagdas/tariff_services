package com.ispark.tariff_service.service;

import com.ispark.tariff_service.model.Tariff;
import com.ispark.tariff_service.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TariffService {

    @Autowired
    private TariffRepository tariffRepository;

    @Autowired
    private LoggerService loggerService;



    public List<Tariff> getAllTariffs() {
        loggerService.logInfo("Fetching all tariffs");
        return tariffRepository.findAll();
    }

    public Tariff getTariffByTarifeNo(String tarifeNo) {
        loggerService.logInfo("Fetching tariff by TarifeNo: " + tarifeNo);
        return tariffRepository.findByTarifeNo(tarifeNo)
                .orElseThrow(() -> {
                    loggerService.logError("Tariff not found with TarifeNo: " + tarifeNo);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tariff not found with TarifeNo: " + tarifeNo);
                });
    }

    public Tariff createTariff(Tariff tariff) {
        tariff.setTarifeNo(generateTarifeNo());
        tariff.setCreateDate(LocalDateTime.now());  // Set creation date
        tariff.setUpdateDate(LocalDateTime.now());  // Set update date
        tariff.setLastUpdatedBy("Auto-generated");  // Set last updated by

        loggerService.logInfo("Creating new tariff with tarifeNo: " + tariff.getTarifeNo());
        Tariff createdTariff = tariffRepository.save(tariff);

        // Kafka'ya tarife oluşturma mesajı gönder

        return createdTariff;
    }

    public Tariff updateTariffByTarifeNo(String tarifeNo, Tariff updatedTariff) {
        loggerService.logInfo("Updating tariff with TarifeNo: " + tarifeNo);
        Tariff tariff = tariffRepository.findByTarifeNo(tarifeNo)
                .orElseThrow(() -> {
                    loggerService.logError("Tariff not found with TarifeNo: " + tarifeNo);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tariff not found with TarifeNo: " + tarifeNo);
                });
        tariff.setName(updatedTariff.getName());
        tariff.setDetails(updatedTariff.getDetails());
        tariff.setUpdateDate(LocalDateTime.now());  // Update date
        tariff.setLastUpdatedBy("Manual update on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        Tariff updated = tariffRepository.save(tariff);

        // Kafka'ya tarife güncelleme mesajı gönder

        return updated;
    }

    public void deleteTariffByTarifeNo(String tarifeNo) {
        loggerService.logInfo("Deleting tariff with TarifeNo: " + tarifeNo);
        Tariff tariff = tariffRepository.findByTarifeNo(tarifeNo)
                .orElseThrow(() -> {
                    loggerService.logError("Tariff not found with TarifeNo: " + tarifeNo);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tariff not found with TarifeNo: " + tarifeNo);
                });
        tariffRepository.delete(tariff);

        // Kafka'ya tarife silme mesajı gönder (isteğe bağlı)
    }
    /**
     * Kafka'dan gelen tarifeyi yeni bir tarife olarak oluşturur ve veritabanına kaydeder.
     *
     * @param kafkaTariff Kafka'dan gelen tarife
     * @return Oluşturulan yeni tarife
     */

    public Tariff createTariffFromKafka(Tariff kafkaTariff) {
        // Tarife numarasını oluştur ve güncelleme bilgilerini ayarla
        kafkaTariff.setTarifeNo(generateTarifeNo());
        kafkaTariff.setCreateDate(LocalDateTime.now());  // Oluşturma tarihini ayarla
        kafkaTariff.setUpdateDate(LocalDateTime.now());  // Güncelleme tarihini ayarla
        kafkaTariff.setLastUpdatedBy("Kafka Event");  // Son güncelleyen kişi olarak Kafka Event bilgisini ayarla

        loggerService.logInfo("Creating new tariff from Kafka with tarifeNo: " + kafkaTariff.getTarifeNo());

        // Tarife veritabanına kaydediliyor
        return tariffRepository.save(kafkaTariff);
    }
    private String generateTarifeNo() {
        return "TARIFE-" + System.currentTimeMillis();
    }
}
