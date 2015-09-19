/*
 * Copyright (c) 2015 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk;

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
    private Type type;
    @JsonProperty("value")
    private String value;

    public APIKeyBundle() {
    }

    public APIKeyBundle(String id, String alias, Type type, String value) {
        this.id = id;
        this.alias = alias;
        this.type = type;
        this.value = value;
    }

    @JsonIgnore
    public HMACKey getHMACKey() throws InvalidKeyException {
        if (type != Type.HMAC) {
            throw new IllegalArgumentException("Key is not an HMAC Key: " + type.name());
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public enum Type {
        HMAC, RSA
    }
}
