package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao.JDBCChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public class JDBCTrackedLinkDAO extends JDBCDAO {
    private JDBCChainWebsiteInfoDAO websiteInfoDAO;
    public JDBCTrackedLinkDAO(DataSource dataSource) {
        super(dataSource);
    }
    public void add(TrackedLink newTrackedLink){
        String sql = "INSERT INTO tracked_link (website_info_id, chat_id) VALUES (?, ?)";

        PreparedStatementCreator psc = connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
            ps.setString(newTrackedLink.getIdWebsiteInfo(), "website_info_id");
            ps.setString(newTrackedLink.getIdChat(), "chat_id");
            return ps;
        };

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        int id = (int) keyHolder.getKey();
        newTrackedLink.setId(id);
    }
    public Optional<TrackedLink> remove(LinkInfo trackedLink, int idChat){
        String sqlQueryForFind = websiteInfoDAO.getQueryForFindIdByLinkInfo(trackedLink);
        String sql = "delete from tracked_link where chat_id = ? and website_info_id IN(" + sqlQueryForFind + ");";
        Object[] params = new Object[] { idChat };

        RowMapper<TrackedLink> rowMapper = new BeanPropertyRowMapper<>(TrackedLink.class);
        List<TrackedLink> entities = jdbcTemplate.query(sql, params, rowMapper);

        TrackedLink deletedEntity = null;
        if (!entities.isEmpty()) {
            deletedEntity = entities.get(0);
        }
        return Optional.ofNullable(deletedEntity);
    }
    public boolean containsTrackedLinkWithChatIdAndLinkInfo(LinkInfo linkInfo, int idChat){
        String sqlQueryForFind = websiteInfoDAO.getQueryForFindIdByLinkInfo(linkInfo);
        String sql = "select count(*) from tracked_link where chat_id = ? and website_info_id IN(" + sqlQueryForFind + ");";
        Object[] params = new Object[] { idChat };
        int count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count > 0;
    }
    public int[] findAllChatsWithIdWebsiteInfo(int idWebsiteInfo){
        return jdbcTemplate.queryForList("select chat_id from tracked_link where website_info_id = ?",
                int[].class, idWebsiteInfo).get(0);
    }
    public List<TrackedLink> findAllByChatId(int idChat){
        return jdbcTemplate.queryForList("select chat_id from tracked_link where chat_id = ?",
                TrackedLink.class, idChat);
    }
}
