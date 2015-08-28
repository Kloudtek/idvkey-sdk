/*
 * Copyright (c) 2015 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk;

import com.kloudtek.kryptotek.DigestAlgorithm;
import com.kloudtek.kryptotek.jce.JCECryptoEngine;
import com.kloudtek.kryptotek.key.SignAndVerifyKey;
import com.kloudtek.kryptotek.key.SignatureVerificationKey;
import com.kloudtek.kryptotek.key.SigningKey;
import com.kloudtek.kryptotek.rest.client.httpcomponents.HCInterceptor;
import com.kloudtek.kryptotek.rest.client.httpcomponents.RestAuthCredential;
import com.kloudtek.kryptotek.rest.client.httpcomponents.TimeAsHttpContentTimeSync;
import com.kloudtek.util.StringUtils;
import com.kloudtek.util.URLBuilder;
import com.kloudtek.util.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

import static com.kloudtek.util.StringUtils.urlEncode;
import static org.apache.http.auth.AuthScope.ANY;

/**
 * Created by yannick on 27/08/15.
 */
public class IDVKeyAPIClient {
    public static final int DEFAULT_TIMEOUT = 30000;
    protected CloseableHttpClient httpClient;
    protected String serverUrl;

    public IDVKeyAPIClient(String id, SignAndVerifyKey hmacKey) {
        this(id, hmacKey, hmacKey, DEFAULT_TIMEOUT);
    }

    public IDVKeyAPIClient(String serverUrl, String id, SignAndVerifyKey hmacKey) {
        this(serverUrl, id, hmacKey, hmacKey, DEFAULT_TIMEOUT);
    }

    public IDVKeyAPIClient(String id, SigningKey signingKey, SignatureVerificationKey signatureVerificationKey, int timeout) {
        this("https://www.idvkey.com", id, signingKey, signatureVerificationKey, timeout);
    }

    public IDVKeyAPIClient(String serverUrl, String id, SigningKey signingKey, SignatureVerificationKey signatureVerificationKey, int timeout) {
        this.serverUrl = serverUrl;
        // Create HTTP Client with REST security interceptor
        final HCInterceptor hcInterceptor = new HCInterceptor(new JCECryptoEngine(), null);
        HttpClientBuilder httpClientBuilder = hcInterceptor.createClientBuilder();
        RequestConfig.Builder requestBuilder = RequestConfig.custom().setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout).setSocketTimeout(timeout);
        httpClientBuilder.setDefaultRequestConfig(requestBuilder.build());
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        // This is used to used to adjust http timestamps in case client has the wrong time
        final TimeAsHttpContentTimeSync timeSync = new TimeAsHttpContentTimeSync(new URLBuilder(serverUrl).addPath("/time").toString());
        credentialsProvider.setCredentials(ANY, new RestAuthCredential(id, signingKey, signatureVerificationKey,
                DigestAlgorithm.SHA256, timeSync));
        httpClient = httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider).build();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }

    public void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            //
        }
    }

    public UserLinkResponse linkUserToCustomerService(String websiteDomain, String finalUrl, String userRef) throws IOException {
        final HttpPost req = new HttpPost(new URLBuilder(serverUrl).addPath("api/idvkey/customerservice/" +
                urlEncode(websiteDomain) + "/link/" + urlEncode(userRef)).toUri());
        final CloseableHttpResponse response = httpClient.execute(req);
        checkStatus(response);
        final String token = StringUtils.utf8(IOUtils.toByteArray(response.getEntity().getContent()));
        return new UserLinkResponse(token, new URLBuilder(serverUrl).addPath("s/linktoservice.xhtml").add("token", token).add("url", finalUrl).toString());
    }

    public boolean checkUserLinkedToCustomerService(String websiteDomain, String finalUrl, String userRef) throws IOException {
        final HttpPost req = new HttpPost(new URLBuilder(serverUrl).addPath("api/idvkey/customerservice/" +
                urlEncode(websiteDomain) + "/link/" + urlEncode(userRef)).toUri());
        final CloseableHttpResponse response = httpClient.execute(req);
        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 404) {
            return false;
        } else if (statusCode == 204) {
            return true;
        } else {
            throw new IOException("Server returned " + response.getStatusLine());
        }
    }

    private void checkStatus(CloseableHttpResponse response) throws IOException {
        final int retCode = response.getStatusLine().getStatusCode();
        if (retCode < 200 || retCode > 299) {
            throw new IOException("Server returned " + response.getStatusLine());
        }
    }

    public boolean isUserLinked(String domain, String userRef) throws IOException {
        final HttpGet req = new HttpGet(new URLBuilder(serverUrl).addPath("api/idvkey/customerservice/" +
                urlEncode(domain) + "/link/" + urlEncode(userRef)).toUri());
        final CloseableHttpResponse response = httpClient.execute(req);
        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 404) {
            return false;
        } else if (statusCode == 200) {
            return true;
        } else {
            throw new IOException("Server returned " + response.getStatusLine());
        }
    }

    public class UserLinkResponse {
        private String token;
        private String url;

        public UserLinkResponse(String token, String url) {
            this.token = token;
            this.url = url;
        }

        public String getToken() {
            return token;
        }

        public String getUrl() {
            return url;
        }
    }
}
