package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "website_inf")
public class WebsiteInfoEntity {
    @Id
    private int id;
    @Column(name = "last_update_date_time ")
    private OffsetDateTime lastCheckUpdate;
    @JoinColumn(name = "type_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private WebsiteInfoTypeEntity typeOfWebsite;
}
