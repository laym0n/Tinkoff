package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;

public class JDBCGitHubCommitDAO extends JDBCDAO {
    public JDBCGitHubCommitDAO(DataSource dataSource) {
        super(dataSource);
    }
    public void addAll(Collection<GitHubCommit> newCommits, int idWebsiteInfo){
        String sql = "INSERT INTO github_commit (sha, website_info_id) VALUES (?, ?)";
        List<Object[]> batchArgs = newCommits.stream()
                .map(i-> new Object[] {i.getSha(), idWebsiteInfo})
                .toList();
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
    public void removeAll(Collection<GitHubCommit> newCommits, int idWebsiteInfo){
        String sql = "delete from github_commit where sha = ? and website_info_id = ?";
        List<Object[]> batchArgs = newCommits.stream()
                .map(i-> new Object[] {i.getSha(), idWebsiteInfo})
                .toList();
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
    public Collection<GitHubCommit> findAll(int idGitHubInfo){
        return jdbcTemplate.query("select * from github_commit where website_info_id = ?",
                (rs, rowNum) -> new GitHubCommit(rs.getString("sha")),
                idGitHubInfo);
    }
}
