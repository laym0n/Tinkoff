package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;

import javax.sql.DataSource;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JDBCStackOverflowCommentDAO extends JDBCDAO{
    public JDBCStackOverflowCommentDAO(DataSource dataSource) {
        super(dataSource);
    }
    public void addAll(Collection<StackOverflowComment> newComments, int idWebsiteInfo){
        String sql = "INSERT INTO stack_overflow_comment (id,  website_info_id) "+
                "VALUES (?, ?)";
        List<Object[]> batchArgs = newComments.stream()
                .map(i-> new Object[] {i.getIdComment(), idWebsiteInfo})
                .toList();
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
    public void removeAll(Collection<StackOverflowComment> commentsForRemove, int idWebsiteInfo){
        String sql = "delete from stack_overflow_comment where id = ? and website_info_id = ?";
        List<Object[]> batchArgs = commentsForRemove.stream()
                .map(i-> new Object[] {i.getIdComment(), idWebsiteInfo})
                .toList();
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
    public Collection<StackOverflowComment> findAll(int idStackOverflowInfo){
        return jdbcTemplate.query("select * from stack_overflow_comment where website_info_id = ?",
                (rs, rowNum) -> new StackOverflowComment(rs.getInt("id")),
                idStackOverflowInfo);
    }
}
