package org.romeo.compliments.users;

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
}
