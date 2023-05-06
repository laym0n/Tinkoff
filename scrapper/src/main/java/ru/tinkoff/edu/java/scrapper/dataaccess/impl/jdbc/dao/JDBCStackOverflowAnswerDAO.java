package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;

import javax.sql.DataSource;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
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
    public Collection<StackOverflowAnswer> findAll(int idStackOverflowInfo){
        return jdbcTemplate.query("select * from stack_overflow_answer where website_info_id = ?",
                (rs, rowNum) ->
                {
                    int id = rs.getInt("id");
                    String userName = rs.getString("user_name");
                    OffsetDateTime lastEditDateTime = rs.getObject("last_edit_date_time", OffsetDateTime.class);
                    return new StackOverflowAnswer(id, userName, lastEditDateTime);
                },
                idStackOverflowInfo);
    }
    public void updateLastEditForAll(Collection<StackOverflowAnswer> answers, int idWebsiteInfo){
        List<Object[]> batchArgs = answers.stream()
                .map(i-> new Object[] {i.getLastEditDate().toLocalDateTime(), idWebsiteInfo,i.getIdAnswer()})
                .toList();
        jdbcTemplate.batchUpdate("update stack_overflow_answer SET last_edit_date_time = ? where " +
                "website_info_id = ? and id = ?", batchArgs);
    }
}
