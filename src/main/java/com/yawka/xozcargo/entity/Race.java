package com.yawka.xozcargo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Race {
    @Id
    private String id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "race", cascade = CascadeType.ALL)

    private List<Item> item;

    private String status;

    //--------------------
    // GETTERS AND SETTERS
    //--------------------

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Race{" +
                "id=" + id +
                ", item=" + item +
                ", status='" + status + '\'' +
                '}';
    }
}
