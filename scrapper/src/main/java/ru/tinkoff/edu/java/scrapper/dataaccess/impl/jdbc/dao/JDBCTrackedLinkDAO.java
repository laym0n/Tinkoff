package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao.JDBCChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;

import javax.sql.DataSource;
import java.security.InvalidParameterException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class JDBCTrackedLinkDAO extends JDBCDAO {
    private JDBCChainWebsiteInfoDAO websiteInfoDAO;

    public JDBCTrackedLinkDAO(DataSource dataSource, JDBCChainWebsiteInfoDAO websiteInfoDAO) {
        super(dataSource);
        this.websiteInfoDAO = websiteInfoDAO;
    }

    public void add(TrackedLink newTrackedLink){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("website_info_id", newTrackedLink.getIdWebsiteInfo());
        paramMap.put("chat_id", newTrackedLink.getIdChat());
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        int idAddedTrackedLink = namedParameterJdbcTemplate.queryForObject(
                "INSERT INTO tracked_link (website_info_id, chat_id) VALUES (:website_info_id, :chat_id) RETURNING id; ",
                paramMap, Integer.class);

        newTrackedLink.setId(idAddedTrackedLink);
    }
    public Optional<TrackedLink> remove(LinkInfo trackedLink, int idChat){
        Optional<Integer> idWebsiteInfo = websiteInfoDAO.findIdByLinkInfo(trackedLink);
        if(idWebsiteInfo.isEmpty())
            return Optional.empty();
        String sql = "select id from tracked_link where chat_id = ? and website_info_id = ?;";
        Object[] params = new Object[] { idChat, idWebsiteInfo.get() };

        List<Integer> idsDeleted = jdbcTemplate.query(sql, (rs, rowNum) -> {
            return rs.getInt("id");
        }, params);

        TrackedLink deletedEntity = null;
        if (!idsDeleted.isEmpty()) {
            deletedEntity = new TrackedLink(idsDeleted.get(0), idChat, idWebsiteInfo.get(), trackedLink);
            jdbcTemplate.update("delete from tracked_link where chat_id = ? and website_info_id = ?;", params);
        }
        return Optional.ofNullable(deletedEntity);
    }
    public boolean containsTrackedLinkWithChatIdAndLinkInfo(LinkInfo linkInfo, int idChat){
        Optional<Integer> idWebsiteInfo = websiteInfoDAO.findIdByLinkInfo(linkInfo);
        if(idWebsiteInfo.isEmpty())
            return false;
        Object[] params = new Object[] { idChat, idWebsiteInfo.get() };
        int count = jdbcTemplate.queryForObject("select count(*) from tracked_link " +
                "where chat_id = ? and website_info_id = ?", Integer.class, params);
        return count > 0;
    }
    public int[] findAllChatsWithIdWebsiteInfo(int idWebsiteInfo){
        return jdbcTemplate.query("select chat_id from tracked_link where website_info_id = ?",
                (RowMapper<Integer>) (rs, rowNum) -> rs.getInt("chat_id"), idWebsiteInfo).stream()
                .flatMapToInt(integer -> IntStream.of(integer)).toArray();

    }
    public List<TrackedLink> findAllByChatId(int idChat){
        return jdbcTemplate.query("select tl.*, wit.name from tracked_link tl " +
                        "join website_info wi on tl.website_info_id = wi.id " +
                        "join website_info_type wit on wi.type_id = wit.id  " +
                        "where chat_id = ?",
                (rs, rowNum) -> {
                    int id = rs.getInt("id");
                    int idWebsiteInfo = rs.getInt("website_info_id");
                    LinkInfo linkInfo = websiteInfoDAO.loadLinkInfoForWebsiteById(idWebsiteInfo, rs.getString("name"));
                    return new TrackedLink(id, idChat, idWebsiteInfo, linkInfo);
                }, idChat);
    }
}
