package com.yawka.xozcargo.repository;

import com.yawka.xozcargo.entity.Race;
import org.springframework.data.repository.CrudRepository;

public interface RaceRepository extends CrudRepository<Race, String> {
    Race findRaceById(String race_id);
}
