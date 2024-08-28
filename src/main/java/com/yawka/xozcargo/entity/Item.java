package com.yawka.xozcargo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    private String whereTo;

    private Integer weight;

    private String city;

    @ManyToOne(cascade = CascadeType.ALL) // Много книг - один автор
    @JoinColumn(name = "race_id")
    @JsonIgnoreProperties("item")
    private Race race;


    @ManyToOne // Много книг - один автор
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private Client user;


    @ManyToOne
    @JoinColumn(name = "item_statuses_id")
    private ItemStatus status;

    //--------------------
    // GETTERS AND SETTERS
    //--------------------

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWhereTo() {
        return whereTo;
    }

    public void setWhereTo(String whereTo) {
        this.whereTo = whereTo;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Client getUser() {
        return user;
    }

    public void setUser(Client user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", whereTo='" + whereTo + '\'' +
                ", weight=" + weight +
                ", city='" + city + '\'' +
                ", race=" + race +
                ", user=" + user +
                '}';
    }
}
