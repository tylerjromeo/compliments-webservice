package org.romeo.compliments.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * User: tylerromeo
 * Date: 6/18/16
 * Time: 9:37 AM
 */

@Entity
public class Compliment {

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "Unique identifier for the compliment", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(optional = false)//TODO: lazy fetch
    @JsonIgnore
    private User from;
    @ManyToOne(optional = false)//TODO: lazy fetch
    @JsonIgnore
    private User to;
    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The text of the compliment", required = true)
    @Column(nullable = false)
    private String contents;
    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The date the compliment was sent", required = true)
    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;
    @JsonProperty(required = false)
    @ApiModelProperty(notes = "List of reactions from the recipient of the comment", required = true)
    @OneToMany
    private List<Reaction> reactions;

    public Compliment() {

    }

    public Compliment(User from, User to, String contents) {
        this.from = from;
        this.to = to;
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty(value = "fromId", required = false)
    @ApiModelProperty(notes = "Id of the user who sent the compliment", required = false)
    @Transient
    public long getFromUserId() {
        return from.getId();
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    @JsonProperty(value = "toId", required = true)
    @ApiModelProperty(notes = "Id of the user the compliment was sent to", required = true)
    @Transient
    public long getToUserId() {
        return to.getId();
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }

    public Date getSendDate() {
        if(sendDate == null) {
            return null;
        }
        return new Date(sendDate.getTime());
    }

    public void setSendDate(Date sendDate) {
        if(sendDate == null) {
            this.sendDate = null;
        } else {
            this.sendDate = new Date(sendDate.getTime());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Compliment that = (Compliment) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;
        if (contents != null ? !contents.equals(that.contents) : that.contents != null) return false;
        if (sendDate != null ? !sendDate.equals(that.sendDate) : that.sendDate != null) return false;
        return reactions != null ? reactions.equals(that.reactions) : that.reactions == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (contents != null ? contents.hashCode() : 0);
        result = 31 * result + (sendDate != null ? sendDate.hashCode() : 0);
        result = 31 * result + (reactions != null ? reactions.hashCode() : 0);
        return result;
    }
}
