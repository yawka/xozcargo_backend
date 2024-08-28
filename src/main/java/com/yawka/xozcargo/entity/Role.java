package com.yawka.xozcargo.entity;


import com.yawka.xozcargo.entity.enums.Roles;
import jakarta.persistence.*;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Roles name;

    public Roles getName() {
        return name;
    }

    public void setName(Roles name) {
        this.name = name;
    }
}
