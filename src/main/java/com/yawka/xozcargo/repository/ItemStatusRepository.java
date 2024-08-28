package com.yawka.xozcargo.repository;

import com.yawka.xozcargo.entity.ItemStatus;
import org.springframework.data.repository.CrudRepository;

public interface ItemStatusRepository extends CrudRepository<ItemStatus, Integer> {
    ItemStatus findById(int id);
}
