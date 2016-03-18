/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.Date;

/**
 * Represents a customer service (generally a website)
 */
@JsonRootName("service")
public class Service {
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String website;
    @JsonProperty
    private String suggestedSecurityLevel;
    @JsonProperty
    private Date created;
    @JsonProperty
    private Date lastUpdated;

    public Service() {
    }

    public Service(String id, String name, String website, String suggestedSecurityLevel, Date created, Date lastUpdated) {
        this.id = id;
        this.name = name;
        this.website = website;
        this.suggestedSecurityLevel = suggestedSecurityLevel;
        this.created = created;
        this.lastUpdated = lastUpdated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSuggestedSecurityLevel() {
        return suggestedSecurityLevel;
    }

    public void setSuggestedSecurityLevel(String suggestedSecurityLevel) {
        this.suggestedSecurityLevel = suggestedSecurityLevel;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
