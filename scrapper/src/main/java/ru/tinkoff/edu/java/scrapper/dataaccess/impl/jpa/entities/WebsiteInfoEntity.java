package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.annotations.UpdateTimestamp;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "website_info")
public class WebsiteInfoEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @Column(name = "last_update_date_time ")
    @UpdateTimestamp
    protected Timestamp lastCheckUpdate;
    @JoinColumn(name = "type_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private WebsiteInfoTypeEntity typeOfWebsite;
    public WebsiteInfoEntity(WebsiteInfo websiteInfo){
        this.id = websiteInfo.getId();
        this.lastCheckUpdate = Timestamp.valueOf(websiteInfo.getLastCheckUpdateDateTime().toLocalDateTime());
    }
    public WebsiteInfo getWebsiteInfo(){
        throw new NotImplementedException();
    }
}
