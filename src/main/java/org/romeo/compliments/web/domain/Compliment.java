package org.romeo.compliments.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * User: tylerromeo
 * Date: 6/17/16
 * Time: 8:38 PM
 */
public class Compliment {

    private long id;
    private long fromId;
    private long toId;
    private String contents;
    private Date sendDate;

    public Compliment() {

    }

    public Compliment(long id, long fromId, long toId, String contents, Date sendDate) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.contents = contents;
        this.sendDate = sendDate;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "Unique identifier for the compliment", required = true)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty(required = false)
    @ApiModelProperty(notes = "Id of the user who sent the compliment", required = false)
    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "Id of the user the compliment was sent to", required = true)
    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
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

    public static Compliment fromDbCompliment(org.romeo.compliments.persistence.domain.Compliment compliment) {
        return new Compliment(compliment.getId(), compliment.getFrom().getId(), compliment.getTo().getId(), compliment.getContents(), compliment.getSendDate());
    }
}
