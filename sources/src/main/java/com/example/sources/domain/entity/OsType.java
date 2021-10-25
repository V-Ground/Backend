package com.example.sources.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OsType {
    @Id
    private Long id;
    private String type;
}
