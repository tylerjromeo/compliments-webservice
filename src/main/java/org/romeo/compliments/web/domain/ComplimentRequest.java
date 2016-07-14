package org.romeo.compliments.web.domain;

import javax.validation.constraints.NotNull;

/**
 * User: tylerromeo
 * Date: 7/14/16
 * Time: 2:16 PM
 */
public class ComplimentRequest {

    @NotNull
    private String toEmail;
    @NotNull
    private String contents;
    @NotNull
    private long fromId;

    public ComplimentRequest() {

    }

    public ComplimentRequest(String toEmail, String content, long fromId) {
        this.toEmail = toEmail;
        this.contents = content;
        this.fromId = fromId;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }
}
