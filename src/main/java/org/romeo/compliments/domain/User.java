package org.romeo.compliments.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * User: tylerromeo
 * Date: 6/15/16
 * Time: 8:32 PM
 */
public class User {

    private String id;
    private String name;
    private String email;
    private String imageUrl;
    private int complimentsSent;
    private int complimentsReceived;

    public User(String email, String id, String name, String imageUrl, int complimentsSent, int complimentsReceived) {
        this.email = email;
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.complimentsSent = complimentsSent;
        this.complimentsReceived = complimentsReceived;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
