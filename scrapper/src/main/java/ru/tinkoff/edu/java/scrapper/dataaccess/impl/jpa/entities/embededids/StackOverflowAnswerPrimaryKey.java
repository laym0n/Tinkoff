package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StackOverflowAnswerPrimaryKey implements Serializable {
    private int id;
    @Column(name = "website_info_id")
    @JoinColumn(table = "stack_overflow_answer",
    name = "website_info_id", referencedColumnName = "website_info_id")
    private int websiteInfoId;
}
