package io.yodo.whisper.commons.security.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@SuppressWarnings({"WeakerAccess", "unused"})
public class TokenDetails {

    private String subject;

    private String[] scopes;

    @JsonProperty("not_before")
    private Date notBefore;

    @JsonProperty("expires_at")
    private Date expiresAt;

    private String issuer;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String[] getScopes() {
        return scopes;
    }

    public void setScopes(String[] scopes) {
        this.scopes = scopes;
    }

    public Date getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Date notBefore) {
        this.notBefore = notBefore;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
