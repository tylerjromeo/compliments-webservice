package org.romeo.compliments.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * User: tylerromeo
 * Date: 6/17/16
 * Time: 8:38 PM
 */
public class Compliment {

    private String id;
    private String fromId;
    private String toId;
    private String contents;
    private Date sendDate;

    public Compliment() {

    }

    private Compliment(Builder builder) {
        setId(builder.id);
        setFromId(builder.fromId);
        setToId(builder.toId);
        setContents(builder.contents);
        setSendDate(builder.sendDate);
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "Unique identifier for the compliment", required = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty(required = false)
    @ApiModelProperty(notes = "Id of the user who sent the compliment", required = false)
    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "Id of the user the compliment was sent to", required = true)
    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The text of the compliment", required = true)
    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The date the compliment was sent", required = true)
    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public static final class Builder {
        private String id;
        private String fromId;
        private String toId;
        private String contents;
        private Date sendDate;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder fromId(String val) {
            fromId = val;
            return this;
        }

        public Builder toId(String val) {
            toId = val;
            return this;
        }

        public Builder contents(String val) {
            contents = val;
            return this;
        }

        public Builder sendDate(Date val) {
            sendDate = val;
            return this;
        }

        public Compliment build() {
            return new Compliment(this);
        }
    }
}
