package org.romeo.compliments.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * User: tylerromeo
 * Date: 6/15/16
 * Time: 8:32 PM
 */

public class User {

    private long id;
    private String name;
    private String email;
    private String imageUrl;
    private int complimentsSent;
    private int complimentsReceived;

    public User(long id, String email, String name, String imageUrl, int complimentsSent, int complimentsReceived) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.imageUrl = imageUrl;
        this.complimentsSent = complimentsSent;
        this.complimentsReceived = complimentsReceived;
    }

    public static User fromDbUser(org.romeo.compliments.persistence.domain.User dbUser) {
        if(dbUser == null) {
            return null;
        } else {
            return new User(dbUser.getId(), dbUser.getEmail(), dbUser.getName(), dbUser.getImageUrl(), dbUser.getComplimentsSent().size(), dbUser.getComplimentsReceived().size());
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The user's email address", required = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The user's name", required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty(required = false)
    @ApiModelProperty(notes = "A url pointing to an image of the user", required = false)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonProperty(required = true, defaultValue = "0")
    @ApiModelProperty(notes = "The numer of compliments this user has sent", required = true)
    public int getComplimentsSent() {
        return complimentsSent;
    }

    public void setComplimentsSent(int complimentsSent) {
        this.complimentsSent = complimentsSent;
    }

    @JsonProperty(required = true, defaultValue = "0")
    @ApiModelProperty(notes = "The numer of compliments this user has received", required = true)
    public int getComplimentsReceived() {
        return complimentsReceived;
    }

    public void setComplimentsReceived(int complimentsReceived) {
        this.complimentsReceived = complimentsReceived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (complimentsSent != user.complimentsSent) return false;
        if (complimentsReceived != user.complimentsReceived) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        return imageUrl != null ? imageUrl.equals(user.imageUrl) : user.imageUrl == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + complimentsSent;
        result = 31 * result + complimentsReceived;
        return result;
    }
}
