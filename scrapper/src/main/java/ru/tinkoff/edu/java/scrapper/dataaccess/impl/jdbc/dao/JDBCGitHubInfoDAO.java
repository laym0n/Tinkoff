package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareGitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class JDBCGitHubInfoDAO extends JDBCDAO {
    private JDBCGitHubCommitDAO commitDAO;
    private JDBCGitHubBranchesDAO branchesDAO;

    public JDBCGitHubInfoDAO(DataSource dataSource, JDBCGitHubCommitDAO commitDAO, JDBCGitHubBranchesDAO branchesDAO) {
        super(dataSource);
        this.commitDAO = commitDAO;
        this.branchesDAO = branchesDAO;
    }

    public Optional<Integer> findIdByLinkInfo(GitHubLinkInfo linkInfo){
        return Optional.ofNullable(jdbcTemplate.queryForObject("select website_info_id from github_info " +
                "where user_name = ? and repository_name = ?", new Object[]{linkInfo.userName(), linkInfo.repositoryName()},
                Integer.class));
    }
    public void add(GitHubInfo newGitHubInfo){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("last_update_date_time", OffsetDateTime.now());
        paramMap.put("user_name", newGitHubInfo.getLinkInfo().userName());
        paramMap.put("repository_name", newGitHubInfo.getLinkInfo().repositoryName());
        paramMap.put("last_activity_date_time", newGitHubInfo.getLastActiveTime());
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        int websiteInfoId = namedParameterJdbcTemplate.queryForObject(
                "INSERT INTO website_info (type_id, last_update_date_time) " +
                "VALUES ((select id from website_info_type where name = 'GitHub'), :last_update_date_time) RETURNING id; ",
                paramMap, Integer.class);
        paramMap.put("website_info_id", websiteInfoId);
        namedParameterJdbcTemplate.update(
                "INSERT INTO github_info (website_info_id, user_name, repository_name, last_activity_date_time) " +
                "VALUES (:website_info_id, :user_name, :repository_name, :last_activity_date_time);", paramMap);
        commitDAO.addAll(newGitHubInfo.getCommits().values(), websiteInfoId);
        branchesDAO.addAll(newGitHubInfo.getBranches().values(), websiteInfoId);
        newGitHubInfo.setId(websiteInfoId);
    }
    public void remove(int idWebsiteInfo){
        jdbcTemplate.update("delete from website_info where id = ?", idWebsiteInfo);
    }
    public GitHubInfo getById(int idGitHubInfo){
        Map<String, GitHubBranch> branches = branchesDAO.findAll(idGitHubInfo).stream()
                .collect(Collectors.toMap(i->i.getBranchName(), i->i));
        Map<String, GitHubCommit> commits = commitDAO.findAll(idGitHubInfo).stream()
                .collect(Collectors.toMap(i->i.getSha(), i->i));
        GitHubInfo result = jdbcTemplate.queryForObject(
                "select * from website_info " +
                " join github_info ON github_info.website_info_id = website_info.id " +
                        "where website_info.id = ? ",
                new Object[]{idGitHubInfo},
                (rs, rowNum) -> {
            int id = rs.getInt("website_info_id");
            OffsetDateTime lastActiveTime = rs.getObject("last_activity_date_time", OffsetDateTime.class);
            OffsetDateTime lastUpdateTime = rs.getObject("last_update_date_time", OffsetDateTime.class);
            String userName = rs.getString("user_name");
            String repositoryName = rs.getString("repository_name");
            GitHubLinkInfo linkInfo = new GitHubLinkInfo(userName, repositoryName);
            GitHubInfo resultOfMapRows = new GitHubInfo(id, lastUpdateTime, linkInfo, lastActiveTime);
            return resultOfMapRows;
                });
        result.setBranches(branches);
        result.setCommits(commits);
        return result;
    }
    public void applyChanges(ResultOfCompareGitHubInfo changes){
        jdbcTemplate.update("UPDATE website_info SET last_update_date_time = ? WHERE id = ?;" +
                        (changes.getLastActivityDate().isEmpty() ? "" :
                                "UPDATE github_info SET last_activity_date_time = ? WHERE website_info_id = ?"),
                new Object[] {OffsetDateTime.now(), changes.getIdWebsiteInfo(),
                        changes.getLastActivityDate().get(), changes.getIdWebsiteInfo()});
        commitDAO.removeAll(Arrays.asList(changes.getDroppedCommits()), changes.getIdWebsiteInfo());
        branchesDAO.removeAll(Arrays.asList(changes.getDroppedBranches()), changes.getIdWebsiteInfo());
        commitDAO.addAll(Arrays.stream(changes.getPushedCommits())
                .map(GitHubCommitResponse::getGitHubCommit).toList(), changes.getIdWebsiteInfo());
        branchesDAO.addAll(Arrays.stream(changes.getAddedBranches())
                .map(GitHubBranchResponse::getGitHubBranch).toList(), changes.getIdWebsiteInfo());
    }

    public GitHubLinkInfo getLinkInfoById(int idWebsiteInfo) {
        return jdbcTemplate.query("select ghi.user_name, ghi.repository_name from github_info ghi " +
                        "where ghi.website_info_id = ?",
                rs -> {
            rs.next();
                    return new GitHubLinkInfo(rs.getString("user_name"), rs.getString("repository_name"));
                }, idWebsiteInfo);
    }
}