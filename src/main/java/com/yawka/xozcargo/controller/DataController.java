package com.yawka.xozcargo.controller;

import com.yawka.xozcargo.entity.Item;
import com.yawka.xozcargo.entity.ItemStatus;
import com.yawka.xozcargo.repository.ItemRepository;
import com.yawka.xozcargo.repository.ItemStatusRepository;
import com.yawka.xozcargo.service.GoogleSheetsService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private GoogleSheetsService googleSheetsService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemStatusRepository itemStatusRepository;

    @PostMapping("/data/save-race")
    public ResponseEntity<String> saveDataFromSheets(@RequestParam String spreadsheetId, String race) throws IOException, GeneralSecurityException {
        String message = googleSheetsService.saveDataFromSheet(spreadsheetId, race);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/data/items")
    public ResponseEntity<List<Item>> getItemsByUserId(@RequestParam Long userId) {
        List<Item> items = itemRepository.findByUser_Id(userId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/data/items-by-phone-number")
    public ResponseEntity<List<Item>> getItemsByPhoneNumber(@RequestParam String phoneNumber) {
        List<Item> items = itemRepository.findByUserPhoneNumber(phoneNumber);
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/data/delete-by-race")
    @Transactional
    public ResponseEntity<List<Item>> deleteByRaceId(@RequestParam String raceId) {
        List<Item> items = itemRepository.removeItemByRaceId(raceId);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/data/change-status-of-items")
    public ResponseEntity<List<Item>> changeStatusOfItems(@RequestParam String raceId, @RequestParam String statusId) {
        List<Item> items = itemRepository.findAllByRaceId(raceId);
        ItemStatus itemStatus = itemStatusRepository.findById(Integer.parseInt(statusId));
        for(Item item : items) {
            item.setStatus(itemStatus);
            itemRepository.save(item);
        }
        return ResponseEntity.ok(items);
    }

    @PostMapping("/data/change-status-of-items-in-bishkek")
    public ResponseEntity<List<Item>> changeStatusOfItemsInBishkek(@RequestParam String raceId, @RequestParam String city, @RequestParam String statusId) {
        List<Item> items = itemRepository.findAllByRaceIdAndAndCity(raceId, city);
        ItemStatus itemStatus = itemStatusRepository.findById(Integer.parseInt(statusId));
        for(Item item : items) {
            item.setStatus(itemStatus);
            itemRepository.save(item);
        }
        return ResponseEntity.ok(items);
    }
}

