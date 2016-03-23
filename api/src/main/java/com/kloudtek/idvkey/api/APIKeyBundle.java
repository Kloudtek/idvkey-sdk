/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kloudtek.kryptotek.CryptoUtils;
import com.kloudtek.kryptotek.DigestAlgorithm;
import com.kloudtek.kryptotek.key.HMACKey;
import com.kloudtek.util.StringUtils;

import java.io.Serializable;
import java.security.InvalidKeyException;

/**
 * Created by yannick on 27/08/15.
 */
public class APIKeyBundle implements Serializable {
    @JsonProperty("id")
    private String id;
    @JsonProperty("alias")
    private String alias;
    @JsonProperty("type")
    private KeyType type;
    @JsonProperty("value")
    private String value;

    public APIKeyBundle() {
    }

    public APIKeyBundle(String id, String alias, KeyType type, String value) {
        this.id = id;
        this.alias = alias;
        this.type = type;
        this.value = value;
    }

    @JsonIgnore
    public HMACKey toHMACKey() throws InvalidKeyException {
        if (type != KeyType.HMAC_SHA256) {
            throw new IllegalArgumentException("Key is not an HMAC_SHA256 Key: " + type.name());
        }
        return CryptoUtils.readHMACKey(DigestAlgorithm.SHA256, StringUtils.base64Decode(value));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public KeyType getType() {
        return type;
    }

    public void setType(KeyType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
