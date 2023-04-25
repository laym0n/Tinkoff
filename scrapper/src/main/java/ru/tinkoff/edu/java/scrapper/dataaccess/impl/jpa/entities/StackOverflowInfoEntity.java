package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "stackoverflow_info")
public class StackOverflowInfoEntity {
    @Id
    @Column(name = "website_info_id")
    private int id;
    private int questionId;
}
