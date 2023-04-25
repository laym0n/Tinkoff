package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;

public class JDBCStackOverflowCommentDAO extends JDBCDAO {
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
