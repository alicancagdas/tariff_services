package com.ispark.tariff_service.controller;

import com.ispark.tariff_service.model.Tariff;
import com.ispark.tariff_service.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tariffs")
public class TariffController {

    @Autowired
    private TariffService tariffService;

    @GetMapping
    public ResponseEntity<List<Tariff>> getAllTariffs() {
        return ResponseEntity.ok(tariffService.getAllTariffs());
    }

    @GetMapping("/tarifeNo/{tarifeNo}")
    public ResponseEntity<Tariff> getTariffByTarifeNo(@PathVariable String tarifeNo) {
        Tariff tariff = tariffService.getTariffByTarifeNo(tarifeNo);
        return ResponseEntity.ok(tariff);
    }

    @PostMapping
    public ResponseEntity<Tariff> createTariff(@RequestBody Tariff tariff) {
        Tariff createdTariff = tariffService.createTariff(tariff);
        return new ResponseEntity<>(createdTariff, HttpStatus.CREATED);
    }

    @PutMapping("/tarifeNo/{tarifeNo}")
    public ResponseEntity<Tariff> updateTariff(@PathVariable String tarifeNo, @RequestBody Tariff updatedTariff) {
        Tariff tariff = tariffService.updateTariffByTarifeNo(tarifeNo, updatedTariff);
        return ResponseEntity.ok(tariff);
    }

    @DeleteMapping("/tarifeNo/{tarifeNo}")
    public ResponseEntity<Void> deleteTariff(@PathVariable String tarifeNo) {
        tariffService.deleteTariffByTarifeNo(tarifeNo);
        return ResponseEntity.noContent().build();
    }
}
