package com.yawka.xozcargo.repository;

import com.yawka.xozcargo.entity.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {

    List<Item> findByUserPhoneNumber(String user_number);
    List<Item> findByUser_Id(Long userId);
    List<Item> removeItemByRaceId(String raceId);
    List<Item> findAllByRaceId(String raceId);
    List<Item> findAllByRaceIdAndAndCity(String raceId, String city);
}
