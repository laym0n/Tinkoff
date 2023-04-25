package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;

import java.io.Serializable;

@Embeddable
public class StackOverflowCommentPrimaryKey implements Serializable {
    private int id;
    @Column(name = "website_info_id")
    @JoinColumn(table = "stack_overflow_answer",
    name = "website_info_id", referencedColumnName = "website_info_id")
    private int websiteInfoId;
}
