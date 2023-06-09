/*
 * This file is generated by jOOQ.
 */

package ru.tinkoff.edu.java.scrapper.domain.jooq.tables.pojos;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class TrackedLink implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer websiteInfoId;
    private Integer chatId;

    public TrackedLink() {
    }

    public TrackedLink(TrackedLink value) {
        this.id = value.id;
        this.websiteInfoId = value.websiteInfoId;
        this.chatId = value.chatId;
    }

    @ConstructorProperties({"id", "websiteInfoId", "chatId"})
    public TrackedLink(
        @Nullable Integer id,
        @NotNull Integer websiteInfoId,
        @NotNull Integer chatId
    ) {
        this.id = id;
        this.websiteInfoId = websiteInfoId;
        this.chatId = chatId;
    }

    /**
     * Getter for <code>TRACKED_LINK.ID</code>.
     */
    @Nullable
    public Integer getId() {
        return this.id;
    }

    /**
     * Setter for <code>TRACKED_LINK.ID</code>.
     */
    public void setId(@Nullable Integer id) {
        this.id = id;
    }

    /**
     * Getter for <code>TRACKED_LINK.WEBSITE_INFO_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Integer getWebsiteInfoId() {
        return this.websiteInfoId;
    }

    /**
     * Setter for <code>TRACKED_LINK.WEBSITE_INFO_ID</code>.
     */
    public void setWebsiteInfoId(@NotNull Integer websiteInfoId) {
        this.websiteInfoId = websiteInfoId;
    }

    /**
     * Getter for <code>TRACKED_LINK.CHAT_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Integer getChatId() {
        return this.chatId;
    }

    /**
     * Setter for <code>TRACKED_LINK.CHAT_ID</code>.
     */
    public void setChatId(@NotNull Integer chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TrackedLink other = (TrackedLink) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.websiteInfoId == null) {
            if (other.websiteInfoId != null) {
                return false;
            }
        } else if (!this.websiteInfoId.equals(other.websiteInfoId)) {
            return false;
        }
        if (this.chatId == null) {
            if (other.chatId != null) {
                return false;
            }
        } else if (!this.chatId.equals(other.chatId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.websiteInfoId == null) ? 0 : this.websiteInfoId.hashCode());
        result = prime * result + ((this.chatId == null) ? 0 : this.chatId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TrackedLink (");

        sb.append(id);
        sb.append(", ").append(websiteInfoId);
        sb.append(", ").append(chatId);

        sb.append(")");
        return sb.toString();
    }
}
