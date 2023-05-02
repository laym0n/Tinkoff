package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao.JDBCChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JDBCTrackedLinkDAO extends JDBCDAO {
    private static final String COLUMN_NAME_FOR_ID_TRACKED_LINK = "id";
    private static final String COLUMN_NAME_FOR_WEBSITE_ID = "website_info_id";
    private static final String COLUMN_NAME_FOR_CHAT_ID = "chat_id";
    private static final String COLUMN_NAME_FOR_NAME_TYPE_OF_WEBSITE = "name";
    private JDBCChainWebsiteInfoDAO websiteInfoDAO;

    public JDBCTrackedLinkDAO(DataSource dataSource, JDBCChainWebsiteInfoDAO websiteInfoDAO) {
        super(dataSource);
        this.websiteInfoDAO = websiteInfoDAO;
    }

    public void add(TrackedLink newTrackedLink) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(COLUMN_NAME_FOR_WEBSITE_ID, newTrackedLink.getIdWebsiteInfo());
        paramMap.put(COLUMN_NAME_FOR_CHAT_ID, newTrackedLink.getIdChat());
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        int idAddedTrackedLink = namedParameterJdbcTemplate
            .queryForObject(
                "INSERT INTO tracked_link (website_info_id, chat_id) "
                    + "VALUES (:website_info_id, :chat_id) RETURNING id; ",
                paramMap,
                Integer.class);

        newTrackedLink.setId(idAddedTrackedLink);
    }

    public Optional<TrackedLink> remove(LinkInfo trackedLink, int idChat) {
        Optional<Integer> idWebsiteInfo = websiteInfoDAO.findIdByLinkInfo(trackedLink);
        if (idWebsiteInfo.isEmpty()) {
            return Optional.empty();
        }
        String sql = "select id from tracked_link where chat_id = ? and website_info_id = ?;";
        Object[] params = new Object[] {idChat, idWebsiteInfo.get() };

        List<Integer> idsDeleted = jdbcTemplate.query(sql,
            (rs, rowNum) -> rs.getInt(COLUMN_NAME_FOR_ID_TRACKED_LINK),
            params);

        TrackedLink deletedEntity = null;
        if (!idsDeleted.isEmpty()) {
            deletedEntity = new TrackedLink(idsDeleted.get(0), idChat, idWebsiteInfo.get(), trackedLink);
            jdbcTemplate.update("delete from tracked_link where chat_id = ? and website_info_id = ?;", params);
        }
        return Optional.ofNullable(deletedEntity);
    }

    public boolean containsTrackedLinkWithChatIdAndLinkInfo(LinkInfo linkInfo, int idChat) {
        Optional<Integer> idWebsiteInfo = websiteInfoDAO.findIdByLinkInfo(linkInfo);
        if (idWebsiteInfo.isEmpty()) {
            return false;
        }
        Object[] params = new Object[] {idChat, idWebsiteInfo.get() };
        int count = jdbcTemplate.queryForObject("select count(*) from tracked_link "
            + "where chat_id = ? and website_info_id = ?", Integer.class, params);
        return count > 0;
    }

    public int[] findAllChatsWithIdWebsiteInfo(int idWebsiteInfo) {
        return jdbcTemplate.query("select chat_id from tracked_link where website_info_id = ?",
                (rs, rowNum) -> rs.getInt(COLUMN_NAME_FOR_CHAT_ID), idWebsiteInfo).stream()
                .flatMapToInt(IntStream::of).toArray();

    }

    public List<TrackedLink> findAllByChatId(int idChat) {
        return jdbcTemplate
            .query(
                "select tl.*, wit.name from tracked_link tl "
                    + "join website_info wi on tl.website_info_id = wi.id "
                    + "join website_info_type wit on wi.type_id = wit.id  "
                    + "where chat_id = ?",
                (rs, rowNum) -> {
                    int id = rs.getInt(COLUMN_NAME_FOR_ID_TRACKED_LINK);
                    int idWebsiteInfo = rs.getInt(COLUMN_NAME_FOR_WEBSITE_ID);
                    LinkInfo linkInfo =
                        websiteInfoDAO.loadLinkInfoForWebsiteById(idWebsiteInfo,
                            rs.getString(COLUMN_NAME_FOR_NAME_TYPE_OF_WEBSITE)
                        );
                    return new TrackedLink(id, idChat, idWebsiteInfo, linkInfo);
                }, idChat);
    }
}
