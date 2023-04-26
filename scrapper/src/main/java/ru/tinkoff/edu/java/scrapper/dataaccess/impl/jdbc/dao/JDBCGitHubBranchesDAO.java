package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JDBCGitHubBranchesDAO extends JDBCDAO{
    public JDBCGitHubBranchesDAO(DataSource dataSource) {
        super(dataSource);
    }
    public void addAll(Collection<GitHubBranch> newBranches, int idWebsiteInfo){
        String sql = "INSERT INTO github_branch (name, website_info_id) VALUES (?, ?)";
        List<Object[]> batchArgs = newBranches.stream()
                .map(i-> new Object[] {i.getBranchName(), idWebsiteInfo})
                .toList();
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
    public void removeAll(Collection<GitHubBranch> newBranches, int idWebsiteInfo){
        String sql = "delete from github_branch where name = ? and website_info_id = ?";
        List<Object[]> batchArgs = newBranches.stream()
                .map(i-> new Object[] {i.getBranchName(), idWebsiteInfo})
                .toList();
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
    public Collection<GitHubBranch> findAll(int idGitHubInfo){
        return jdbcTemplate.query("select * from github_branch where website_info_id = ?",
                (rs, rowNum) -> new GitHubBranch(rs.getString("name")),
                idGitHubInfo);
    }
}
