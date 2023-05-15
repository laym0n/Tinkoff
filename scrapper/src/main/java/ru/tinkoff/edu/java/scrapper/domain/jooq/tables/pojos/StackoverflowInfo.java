/*
 * This file is generated by jOOQ.
 */

package ru.tinkoff.edu.java.scrapper.domain.jooq.tables.pojos;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;

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
public class StackoverflowInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer websiteInfoId;
    private Integer questionId;

    public StackoverflowInfo() {
    }

    public StackoverflowInfo(StackoverflowInfo value) {
        this.websiteInfoId = value.websiteInfoId;
        this.questionId = value.questionId;
    }

    @ConstructorProperties({"websiteInfoId", "questionId"})
    public StackoverflowInfo(
        @NotNull Integer websiteInfoId,
        @NotNull Integer questionId
    ) {
        this.websiteInfoId = websiteInfoId;
        this.questionId = questionId;
    }

    /**
     * Getter for <code>STACKOVERFLOW_INFO.WEBSITE_INFO_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Integer getWebsiteInfoId() {
        return this.websiteInfoId;
    }

    /**
     * Setter for <code>STACKOVERFLOW_INFO.WEBSITE_INFO_ID</code>.
     */
    public void setWebsiteInfoId(@NotNull Integer websiteInfoId) {
        this.websiteInfoId = websiteInfoId;
    }

    /**
     * Getter for <code>STACKOVERFLOW_INFO.QUESTION_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Integer getQuestionId() {
        return this.questionId;
    }

    /**
     * Setter for <code>STACKOVERFLOW_INFO.QUESTION_ID</code>.
     */
    public void setQuestionId(@NotNull Integer questionId) {
        this.questionId = questionId;
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
        final StackoverflowInfo other = (StackoverflowInfo) obj;
        if (this.websiteInfoId == null) {
            if (other.websiteInfoId != null) {
                return false;
            }
        } else if (!this.websiteInfoId.equals(other.websiteInfoId)) {
            return false;
        }
        if (this.questionId == null) {
            if (other.questionId != null) {
                return false;
            }
        } else if (!this.questionId.equals(other.questionId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.websiteInfoId == null) ? 0 : this.websiteInfoId.hashCode());
        result = prime * result + ((this.questionId == null) ? 0 : this.questionId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("StackoverflowInfo (");

        sb.append(websiteInfoId);
        sb.append(", ").append(questionId);

        sb.append(")");
        return sb.toString();
    }
}
