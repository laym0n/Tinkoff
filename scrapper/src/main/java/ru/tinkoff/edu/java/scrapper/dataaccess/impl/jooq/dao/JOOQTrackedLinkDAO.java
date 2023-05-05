package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.jooq.Condition;
import org.jooq.Record2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.websiteinfochaindao.JOOQChainWebsiteInfoDAO;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.WebsiteInfoType;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records.TrackedLinkRecord;
import ru.tinkoff.edu.java.scrapper.entities.TrackedLink;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JOOQTrackedLinkDAO extends JOOQDAO {
    private WebsiteInfo websiteInfo = WebsiteInfo.WEBSITE_INFO;
    private WebsiteInfoType websiteInfoType = WebsiteInfoType.WEBSITE_INFO_TYPE;
    private ru.tinkoff.edu.java.scrapper.domain.jooq.tables.TrackedLink trackedLink =
        ru.tinkoff.edu.java.scrapper.domain.jooq.tables.TrackedLink.TRACKED_LINK;
    private JOOQChainWebsiteInfoDAO websiteInfoDAO;

    public JOOQTrackedLinkDAO(JOOQChainWebsiteInfoDAO websiteInfoDAO) {
        this.websiteInfoDAO = websiteInfoDAO;
    }

    public void add(TrackedLink newTrackedLink) {
        TrackedLinkRecord newTrackedLinkrecord = context.newRecord(trackedLink);
        newTrackedLinkrecord.setChatId(newTrackedLink.getIdChat());
        newTrackedLinkrecord.setWebsiteInfoId(newTrackedLink.getIdWebsiteInfo());
        newTrackedLinkrecord.store();
        newTrackedLink.setId(newTrackedLinkrecord.getId());
    }

    public Optional<TrackedLink> remove(LinkInfo linkInfo, int idChat) {
        Optional<Integer> idWebsiteInfo = websiteInfoDAO.findIdByLinkInfo(linkInfo);
        if (idWebsiteInfo.isEmpty()) {
            return Optional.empty();
        }
        TrackedLinkRecord removedLink = context
            .selectFrom(trackedLink)
            .where(
                trackedLink.WEBSITE_INFO_ID.eq(idWebsiteInfo.get())
                    .and(trackedLink.CHAT_ID.eq(idChat))
            )
            .fetchOne();
        if (removedLink == null) {
            return Optional.empty();
        }
        TrackedLink result = new TrackedLink(
            removedLink.getId(),
            removedLink.getChatId(),
            removedLink.getWebsiteInfoId(),
            linkInfo
        );
        removedLink.delete();

        return Optional.ofNullable(result);
    }

    public boolean containsTrackedLinkWithChatIdAndLinkInfo(LinkInfo linkInfo, int idChat) {
        Optional<Integer> idWebsiteInfo = websiteInfoDAO.findIdByLinkInfo(linkInfo);
        if (idWebsiteInfo.isEmpty()) {
            return false;
        }
        int count = context.selectCount()
            .from(trackedLink)
            .where(new Condition[] {trackedLink.CHAT_ID.eq(idChat),
                trackedLink.WEBSITE_INFO_ID.eq(idWebsiteInfo.get())})
            .fetch().get(0).value1();
        return count > 0;
    }

    public int[] findAllChatsWithIdWebsiteInfo(int idWebsiteInfo) {
        return context.select(trackedLink.CHAT_ID)
            .from(trackedLink)
            .where(trackedLink.WEBSITE_INFO_ID.eq(idWebsiteInfo))
            .fetch()
            .getValues(trackedLink.CHAT_ID)
            .stream()
            .flatMapToInt(IntStream::of).toArray();

    }

    public List<TrackedLink> findAllByChatId(int idChat) {
        List<Record2<TrackedLinkRecord, String>> loadedRecords = context
            .select(trackedLink, websiteInfoType.NAME)
            .from(trackedLink)
            .join(websiteInfo)
            .on(websiteInfo.ID.eq(trackedLink.WEBSITE_INFO_ID))
            .join(websiteInfoType)
            .on(websiteInfoType.ID.eq(websiteInfo.TYPE_ID))
            .where(trackedLink.CHAT_ID.eq(idChat))
            .fetch();
        return loadedRecords.stream().map(loadedRecord ->
            new TrackedLink(
                loadedRecord.value1().getId(),
                loadedRecord.value1().getChatId(),
                loadedRecord.value1().getWebsiteInfoId(),
                websiteInfoDAO.loadLinkInfoForWebsiteById(
                    loadedRecord.value1().getWebsiteInfoId(), loadedRecord.get(websiteInfoType.NAME)
                )
            )).toList();
    }
}
