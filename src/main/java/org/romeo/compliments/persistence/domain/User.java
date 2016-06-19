package org.romeo.compliments.persistence.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: tylerromeo
 * Date: 6/18/16
 * Time: 9:42 AM
 */

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String imageUrl;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Compliment> complimentsSent;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Compliment> complimentsReceived;

    public User() {

    }

    public User(String name, String email, String imageUrl) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.complimentsSent = new ArrayList<>();
        this.complimentsReceived = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Compliment> getComplimentsSent() {
        return complimentsSent;
    }

    public void setComplimentsSent(List<Compliment> complimentsSent) {
        this.complimentsSent = complimentsSent;
    }

    public List<Compliment> getComplimentsReceived() {
        return complimentsReceived;
    }

    public void setComplimentsReceived(List<Compliment> complimentsReceived) {
        this.complimentsReceived = complimentsReceived;
    }
}
