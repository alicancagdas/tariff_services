package com.ispark.tariff_service.service;

import com.ispark.tariff_service.model.Tariff;
import com.ispark.tariff_service.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TariffService {

    @Autowired
    private TariffRepository tariffRepository;

    public List<Tariff> getAllTariffs() {
        return tariffRepository.findAll();
    }

    public Tariff getTariffByTarifeNo(String tarifeNo) {
        return tariffRepository.findByTarifeNo(tarifeNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tariff not found with TarifeNo: " + tarifeNo));
    }

    public Tariff createTariff(Tariff tariff) {
        tariff.setCreateDate(LocalDateTime.now());
        tariff.setUpdateDate(LocalDateTime.now());
        return tariffRepository.save(tariff);
    }

    public Tariff updateTariffByTarifeNo(String tarifeNo, Tariff updatedTariff) {
        Tariff tariff = tariffRepository.findByTarifeNo(tarifeNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tariff not found with TarifeNo: " + tarifeNo));
        tariff.setName(updatedTariff.getName());
        tariff.setDetails(updatedTariff.getDetails());
        tariff.setUpdateDate(LocalDateTime.now());
        return tariffRepository.save(tariff);
    }

    public void deleteTariffByTarifeNo(String tarifeNo) {
        Tariff tariff = tariffRepository.findByTarifeNo(tarifeNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tariff not found with TarifeNo: " + tarifeNo));
        tariffRepository.delete(tariff);
    }

    public Tariff createTariffFromKafka(Tariff kafkaTariff) {
        kafkaTariff.setCreateDate(LocalDateTime.now());
        kafkaTariff.setUpdateDate(LocalDateTime.now());
        return tariffRepository.save(kafkaTariff);
    }
}
