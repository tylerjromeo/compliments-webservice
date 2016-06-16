package org.romeo.compliments.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * User: tylerromeo
 * Date: 6/15/16
 * Time: 8:32 PM
 */
public class Model {

    private String id;
    private String email;
    private String imageUrl;

    public Model(String id, String email, String imageUrl) {
        this.id = id;
        this.email = email;
        this.imageUrl = imageUrl;
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

    @JsonProperty(required = false)
    @ApiModelProperty(notes = "A url pointing to an image of the user", required = false)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
