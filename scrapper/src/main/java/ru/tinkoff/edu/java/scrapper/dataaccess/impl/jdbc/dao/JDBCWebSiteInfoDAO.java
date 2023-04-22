package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao;

import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao.JDBCChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import javax.sql.DataSource;
import java.util.List;

public class JDBCWebsiteInfoDAO extends JDBCDAO{
    private JDBCChainWebsiteInfoDAO chainWebsiteInfoDAO;

    public JDBCWebsiteInfoDAO(DataSource dataSource, JDBCChainWebsiteInfoDAO chainWebsiteInfoDAO) {
        super(dataSource);
        this.chainWebsiteInfoDAO = chainWebsiteInfoDAO;
    }

    public List<WebsiteInfo> loadWebsiteInfoWithTheEarliestUpdateTime(int count){
        return jdbcTemplate.query("SELECT wi.id, wit.name " +
                        "FROM website_info wi " +
                        "JOIN website_info_type wit ON wi.type_id = wit.id " +
                        "ORDER BY wi.last_update_date_time ASC, wi.id ASC " +
                        "LIMIT ?;",
                (rs, rowNum) -> chainWebsiteInfoDAO.loadWebsiteInfo(rs.getString("name"), rs.getInt("id")),
                count);
    }
    public void remove(int idWebsiteInfo){
        jdbcTemplate.update("delete from website_info where id = ?", idWebsiteInfo);
    }
}
