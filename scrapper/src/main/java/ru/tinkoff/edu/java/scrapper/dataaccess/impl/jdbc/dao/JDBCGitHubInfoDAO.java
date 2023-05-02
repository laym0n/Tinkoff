package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareGitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JDBCGitHubInfoDAO extends JDBCDAO {
    private final static String NAME_OF_COLUMN_FOR_REPOSITORY_NAME = "repository_name";
    private final static String NAME_OF_COLUMN_FOR_USER_NAME = "user_name";
    private final static String NAME_OF_COLUMN_FOR_LAST_ACTIVITY = "last_activity_date_time";
    private final static String NAME_OF_COLUMN_FOR_LAST_UPDATE = "last_update_date_time";
    private final static String NAME_OF_COLUMN_FOR_WEBSITE_INFO_ID = "website_info_id";
    private JDBCGitHubCommitDAO commitDAO;
    private JDBCGitHubBranchesDAO branchesDAO;

    public JDBCGitHubInfoDAO(DataSource dataSource,
            JDBCGitHubCommitDAO commitDAO,
            JDBCGitHubBranchesDAO branchesDAO) {
        super(dataSource);
        this.commitDAO = commitDAO;
        this.branchesDAO = branchesDAO;
    }

    public Optional<Integer> findIdByLinkInfo(GitHubLinkInfo linkInfo) {
        List<Integer> ids =
            jdbcTemplate.queryForList("select website_info_id from github_info "
                    + "where user_name = ? and repository_name = ?",
                new Object[]{linkInfo.userName(), linkInfo.repositoryName()},
                Integer.class
            );
        return Optional.ofNullable((ids.size()) == 0 ? null : ids.get(0));
    }

    public void add(GitHubInfo newGitHubInfo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(NAME_OF_COLUMN_FOR_LAST_UPDATE, OffsetDateTime.now());
        paramMap.put(NAME_OF_COLUMN_FOR_USER_NAME, newGitHubInfo.getLinkInfo().userName());
        paramMap.put(NAME_OF_COLUMN_FOR_REPOSITORY_NAME, newGitHubInfo.getLinkInfo().repositoryName());
        paramMap.put(NAME_OF_COLUMN_FOR_LAST_ACTIVITY, newGitHubInfo.getLastActiveTime());
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        int websiteInfoId =
            namedParameterJdbcTemplate.queryForObject(
                "INSERT INTO website_info (type_id, last_update_date_time) "
                    + "VALUES ((select id from website_info_type where name = 'GitHub'), "
                    + ":last_update_date_time) RETURNING id; ",
                paramMap,
                Integer.class
            );
        paramMap.put(NAME_OF_COLUMN_FOR_WEBSITE_INFO_ID, websiteInfoId);
        namedParameterJdbcTemplate.update(
                "INSERT INTO github_info (website_info_id, user_name, repository_name, last_activity_date_time) "
                    + "VALUES (:website_info_id, :user_name, :repository_name, :last_activity_date_time);", paramMap);
        commitDAO.addAll(newGitHubInfo.getCommits().values(), websiteInfoId);
        branchesDAO.addAll(newGitHubInfo.getBranches().values(), websiteInfoId);
        newGitHubInfo.setId(websiteInfoId);
    }

    public void remove(int idWebsiteInfo) {
        jdbcTemplate.update("delete from website_info where id = ?", idWebsiteInfo);
    }

    public GitHubInfo getById(int idGitHubInfo) {
        Map<String, GitHubBranch> branches = branchesDAO.findAll(idGitHubInfo).stream()
                .collect(Collectors.toMap(GitHubBranch::getBranchName, i -> i));

        Map<String, GitHubCommit> commits = commitDAO.findAll(idGitHubInfo).stream()
                .collect(Collectors.toMap(GitHubCommit::getSha, i -> i));

        GitHubInfo result = jdbcTemplate.queryForObject(
            "select * from website_info "
                + " join github_info ON github_info.website_info_id = website_info.id "
                + "where website_info.id = ? ",
                new Object[]{idGitHubInfo},
                (rs, rowNum) -> {
                    int id = rs.getInt(NAME_OF_COLUMN_FOR_WEBSITE_INFO_ID);
                    OffsetDateTime lastActiveTime =
                        rs.getObject(NAME_OF_COLUMN_FOR_LAST_ACTIVITY, OffsetDateTime.class);
                    OffsetDateTime lastUpdateTime =
                        rs.getObject(NAME_OF_COLUMN_FOR_LAST_UPDATE, OffsetDateTime.class);
                    String userName = rs.getString(NAME_OF_COLUMN_FOR_USER_NAME);
                    String repositoryName = rs.getString(NAME_OF_COLUMN_FOR_REPOSITORY_NAME);
                    GitHubLinkInfo linkInfo = new GitHubLinkInfo(userName, repositoryName);
                    GitHubInfo resultOfMapRows = new GitHubInfo(id, lastUpdateTime, linkInfo, lastActiveTime);
                    return resultOfMapRows;
                }
                );
        result.setBranches(branches);
        result.setCommits(commits);
        return result;
    }

    public void applyChanges(ResultOfCompareGitHubInfo changes) {
        jdbcTemplate.update(
            "UPDATE website_info SET last_update_date_time = ? WHERE id = ?;"
                + (changes.getLastActivityDate().isEmpty() ? ""
                    : "UPDATE github_info SET last_activity_date_time = ? WHERE website_info_id = ?"
                ),
                new Object[] {OffsetDateTime.now(), changes.getIdWebsiteInfo(),
                    changes.getLastActivityDate().get(), changes.getIdWebsiteInfo()}
        );

        commitDAO.removeAll(Arrays.asList(changes.getDroppedCommits()), changes.getIdWebsiteInfo());

        branchesDAO.removeAll(Arrays.asList(changes.getDroppedBranches()), changes.getIdWebsiteInfo());

        commitDAO.addAll(Arrays.stream(changes.getPushedCommits())
                .map(GitHubCommitResponse::getGitHubCommit).toList(), changes.getIdWebsiteInfo());

        branchesDAO.addAll(Arrays.stream(changes.getAddedBranches())
                .map(GitHubBranchResponse::getGitHubBranch).toList(), changes.getIdWebsiteInfo());
    }

    public GitHubLinkInfo getLinkInfoById(int idWebsiteInfo) {
        return jdbcTemplate.query(
            "select ghi.user_name, ghi.repository_name from github_info ghi "
                + "where ghi.website_info_id = ?",
                rs -> {
                    rs.next();
                    return new GitHubLinkInfo(
                        rs.getString(NAME_OF_COLUMN_FOR_USER_NAME),
                        rs.getString(NAME_OF_COLUMN_FOR_REPOSITORY_NAME)
                    );
                },
                idWebsiteInfo);
    }
}
