package ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StackOverflowComment {
    private StackOverflowUser user;
    private int idComment;

}
