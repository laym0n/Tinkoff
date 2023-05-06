package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.hibernate.annotations.UpdateTimestamp;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "website_info")
public class WebsiteInfoEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @Column(name = "last_update_date_time ")
    @UpdateTimestamp
    @EqualsExclude
    @HashCodeExclude
    protected Timestamp lastCheckUpdate;
    @JoinColumn(name = "type_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @EqualsExclude
    @HashCodeExclude
    private WebsiteInfoTypeEntity typeOfWebsite;

    public WebsiteInfoEntity(Timestamp lastCheckUpdate) {
        this(0, lastCheckUpdate, null);
    }
    public WebsiteInfoEntity(int id) {
        this(id, Timestamp.valueOf(LocalDateTime.now()), null);
    }

    public WebsiteInfoEntity(WebsiteInfo websiteInfo){
        this.id = websiteInfo.getId();
        this.lastCheckUpdate = Timestamp.valueOf(websiteInfo.getLastCheckUpdateDateTime().toLocalDateTime());
    }
    public WebsiteInfo getWebsiteInfo(){
        throw new NotImplementedException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebsiteInfoEntity that = (WebsiteInfoEntity) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
