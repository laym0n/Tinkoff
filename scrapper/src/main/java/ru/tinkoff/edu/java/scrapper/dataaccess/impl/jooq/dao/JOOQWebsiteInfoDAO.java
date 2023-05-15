package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.jooq.Record2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.websiteinfochaindao.JOOQChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.WebsiteInfoType;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records.WebsiteInfoRecord;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JOOQWebsiteInfoDAO extends JOOQDAO {
    private WebsiteInfo websiteInfo = WebsiteInfo.WEBSITE_INFO;
    private WebsiteInfoType websiteInfoType = WebsiteInfoType.WEBSITE_INFO_TYPE;
    private JOOQChainWebsiteInfoDAO chainWebsiteInfoDAO;

    public JOOQWebsiteInfoDAO(@Lazy JOOQChainWebsiteInfoDAO chainWebsiteInfoDAO) {
        this.chainWebsiteInfoDAO = chainWebsiteInfoDAO;
    }

    public List<ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo> loadWebsiteInfoWithTheEarliestUpdateTime(
        int count
    ) {
        List<Record2<WebsiteInfoRecord, String>> loadedRecords = context
            .select(websiteInfo, websiteInfoType.NAME)
            .from(websiteInfo)
            .join(websiteInfoType)
            .on(websiteInfoType.ID.eq(websiteInfo.TYPE_ID))
            .orderBy(websiteInfo.LAST_UPDATE_DATE_TIME)
            .limit(count)
            .fetch();
        return loadedRecords.stream()
            .map(loadedRecord ->
                chainWebsiteInfoDAO.loadWebsiteInfo(
                    loadedRecord.get(websiteInfoType.NAME),
                    loadedRecord.value1().getId()
                )
            ).toList();
    }

    public void remove(int idWebsiteInfo) {
        context.delete(websiteInfo).where(websiteInfo.ID.eq(idWebsiteInfo)).execute();
    }

    public void updateLastCheckUpdateForWebsite(int id) {
        context.update(websiteInfo)
            .set(websiteInfo.LAST_UPDATE_DATE_TIME, LocalDateTime.now())
            .execute();
    }

    public Integer create(String websiteType) {
        WebsiteInfoRecord newRecord = context.newRecord(websiteInfo);
        newRecord.setLastUpdateDateTime(LocalDateTime.now());
        newRecord.setTypeId(
            context.fetchOne(websiteInfoType, websiteInfoType.NAME.eq(websiteType)).getId()
        );
        newRecord.store();
        return newRecord.getId();
    }

    public Optional<WebsiteInfoRecord> findById(int id) {
        return Optional.ofNullable(
            context.fetchOne(websiteInfo, websiteInfo.ID.eq(id))
        );
    }
}
