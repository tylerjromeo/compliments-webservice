package org.romeo.compliments.persistence.domain;


import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * User: tylerromeo
 * Date: 6/18/16
 * Time: 9:37 AM
 */

@Entity
public class Compliment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne(optional = false)
    private User from;
    @ManyToOne(optional = false)
    private User to;
    @Column(nullable = false)
    private String contents;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;

    public Compliment() {

    }

    public Compliment(User from, User to, String contents) {
        this.from = from;
        this.to = to;
        this.contents = contents;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
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

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
}
