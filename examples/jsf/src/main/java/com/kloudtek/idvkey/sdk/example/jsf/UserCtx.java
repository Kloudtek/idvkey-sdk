package com.kloudtek.idvkey.sdk.example.jsf;

import com.kloudtek.idvkey.sdk.IDVKeyAPIClient;
import com.kloudtek.idvkey.sdk.UserAlreadyLinkedException;
import com.kloudtek.kryptotek.CryptoUtils;
import com.kloudtek.kryptotek.DigestAlgorithm;
import com.kloudtek.kryptotek.key.HMACKey;
import com.kloudtek.util.StringUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.IOException;
import java.security.InvalidKeyException;

/**
 * Created by yannick on 1/14/16.
 */
@ManagedBean
@SessionScoped
public class UserCtx {
    private User user;
    private transient final IDVKeyAPIClient apiClient;
    private transient final String websiteId;

    public UserCtx() throws InvalidKeyException {
        final String keyId = System.getProperty("keyid");
        final String apiKey = System.getProperty("key");
        websiteId = System.getProperty("websiteId");
        if(StringUtils.isBlank(apiKey)) {
            throw new IllegalStateException("keyid system property not set");
        } else if(StringUtils.isBlank(apiKey)) {
            throw new IllegalStateException("key system property not set");
        }
        final HMACKey hmacKey = CryptoUtils.readHMACKey(DigestAlgorithm.SHA256, StringUtils.base64Decode(apiKey));
        apiClient = new IDVKeyAPIClient(keyId, hmacKey);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLinked() throws IOException {
        return user != null && user.isIdvkeyLinked();
    }

    public void linkUser() throws IOException, UserAlreadyLinkedException {
        apiClient.linkUser(websiteId,"http://localhost:8080",user.getUsername());
    }
}
