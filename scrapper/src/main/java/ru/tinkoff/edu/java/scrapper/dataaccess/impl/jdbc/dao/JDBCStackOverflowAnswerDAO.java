package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao;

import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;

public class JDBCStackOverflowAnswerDAO extends JDBCDAO{
    public JDBCStackOverflowAnswerDAO(DataSource dataSource) {
        super(dataSource);
    }
    public void addAll(Collection<StackOverflowAnswer> newAnswers, int idWebsiteInfo){
        String sql = "INSERT INTO stack_overflow_answer (id, user_name, last_edit_date_time,  website_info_id) "+
                "VALUES (?, ?, ?, ?)";
        List<Object[]> batchArgs = newAnswers.stream()
                .map(i-> new Object[] {i.getIdAnswer(), i.getUserName(), i.getLastEditDate(), idWebsiteInfo})
                .toList();
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
    public void removeAll(Collection<StackOverflowAnswer> answersForRemove, int idWebsiteInfo){
        String sql = "delete from stack_overflow_answer where id = ? and website_info_id = ?";
        List<Object[]> batchArgs = answersForRemove.stream()
                .map(i-> new Object[] {i.getIdAnswer(), idWebsiteInfo})
                .toList();
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
}
