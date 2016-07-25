package org.romeo.compliments.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.romeo.compliments.domain.Compliment;

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
    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The user's name", required = true)
    @Column(nullable = true)
    private String name;
    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The user's email address", required = true)
    @Column(nullable = false, unique = true)
    private String email;
    @ApiModelProperty(notes = "A url pointing to an image of the user", required = false)
    private String imageUrl;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "from")//TODO lazy fetch
    @JsonIgnore
    private List<Compliment> complimentsSent;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "to")//TODO lazy fetch
    @JsonIgnore
    private List<Compliment> complimentsReceived;

    public User() {

    }

    public User(long id) {
        this.id = id;
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

    @JsonProperty(value = "complimentsSent", required = true, defaultValue = "0")
    @ApiModelProperty(notes = "The number of compliments this user has sent", required = true)
    @Transient
    public int getComplimentsSentCount() {
        return complimentsSent.size();
    }

    public List<Compliment> getComplimentsSent() {
        return complimentsSent;
    }

    public void setComplimentsSent(List<Compliment> complimentsSent) {
        this.complimentsSent = complimentsSent;
    }

    @JsonProperty(value = "complimentsReceived", required = true, defaultValue = "0")
    @ApiModelProperty(notes = "The number of compliments this user has received", required = true)
    @Transient
    public int getComplimentsReceivedCount() {
        return complimentsReceived.size();
    }

    public List<Compliment> getComplimentsReceived() {
        return complimentsReceived;
    }

    public void setComplimentsReceived(List<Compliment> complimentsReceived) {
        this.complimentsReceived = complimentsReceived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (imageUrl != null ? !imageUrl.equals(user.imageUrl) : user.imageUrl != null) return false;
        if (complimentsSent != null ? !complimentsSent.equals(user.complimentsSent) : user.complimentsSent != null)
            return false;
        return complimentsReceived != null ? complimentsReceived.equals(user.complimentsReceived) : user.complimentsReceived == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (complimentsSent != null ? complimentsSent.hashCode() : 0);
        result = 31 * result + (complimentsReceived != null ? complimentsReceived.hashCode() : 0);
        return result;
    }


}
