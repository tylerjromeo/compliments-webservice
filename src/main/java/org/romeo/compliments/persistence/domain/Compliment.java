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
}
