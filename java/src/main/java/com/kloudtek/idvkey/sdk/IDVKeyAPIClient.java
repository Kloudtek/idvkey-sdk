/*
 * Copyright (c) 2015 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.net.URL;

import static com.kloudtek.util.StringUtils.urlEncode;
import static org.apache.http.auth.AuthScope.ANY;

/**
 * Allows to perform API operations on the IDVKey services
 */
public class IDVKeyAPIClient {
    public static final int DEFAULT_TIMEOUT = 30000;
    protected CloseableHttpClient httpClient;
    protected String serverUrl;
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    /**
     * Constructor
     *
     * @param id  Key id
     * @param key A {@link SignAndVerifyKey} key
     */
    public IDVKeyAPIClient(String id, SignAndVerifyKey key) {
        this(id, key, key, DEFAULT_TIMEOUT);
    }

    /**
     * Contructor (only use this to connect to IDVKey test server)
     *
     * @param serverUrl Server URL
     * @param id        Key id
     * @param key       A {@link SignAndVerifyKey} key
     */
    public IDVKeyAPIClient(String serverUrl, String id, SignAndVerifyKey key) {
        this(serverUrl, id, key, key, DEFAULT_TIMEOUT);
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

    /**
     * Close the client and release resources
     */
    public void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            //
        }
    }

    /**
     * Link an IDVKey user to your website.
     * You need to call this operation before a user on your website can use his IDVKey device
     *
     * @param domain      Your website domain
     * @param redirectUrl The URL to which the user's browser will be redirected to after he's approved the link
     * @param userRef     User reference (generally the user's username on your website)
     * @return URL you should redirect your user's browser to, in order for him to approve the linking
     * @throws IOException If the server returned an error
     */
    public URL linkUserToCustomerService(String domain, String redirectUrl, String userRef) throws IOException {
        final HttpPost req = new HttpPost(new URLBuilder(serverUrl).addPath("api/idvkey/customerservice/" +
                urlEncode(domain) + "/link/" + urlEncode(userRef)).toUri());
        final CloseableHttpResponse response = httpClient.execute(req);
        checkStatus(response);
        final String token = StringUtils.utf8(IOUtils.toByteArray(response.getEntity().getContent()));
        return new URLBuilder(serverUrl).addPath("s/linktoservice.xhtml").add("token", token).add("url", redirectUrl).toUrl();
    }

    /**
     * Check if a user has been linked against your website.
     * You should call this the user's browser has been redirected to the redirectUrl you specified in {@link #linkUserToCustomerService(String, String, String)}.
     *
     * @param domain  Website domain
     * @param userRef User reference (generally the user's username on your website)
     * @return true if the user is linked against your website
     * @throws IOException If the server returned an error
     */
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

    /**
     * Initiate an IDVKey authentication for a user
     *
     * @param domain      Website domain
     * @param redirectUrl URL that the user's browser should be redirected to after he's performed the authentication.
     * @return Operation result. This will contain the URL you should redirect your user's browser to
     * ({@link OperationResult#getRedirectUrl()}), and an operation id that you will use to verify that the user has completed
     * authentication successfully ({@link OperationResult#getOpId()})
     * @throws IOException
     */
    public OperationResult authenticateUser(String domain, String redirectUrl) throws IOException {
        final HttpPost req = new HttpPost(new URLBuilder(serverUrl).addPath("api/idvkey/customerservice/" +
                urlEncode(domain) + "/auth").toUri());
        final CloseableHttpResponse response = httpClient.execute(req);
        checkStatus(response);
        final Long opId = Long.parseLong(StringUtils.utf8(IOUtils.toByteArray(response.getEntity().getContent())));
        return new OperationResult(opId, new URLBuilder(serverUrl).addPath("s/authenticate").add("opId", opId).add("url", redirectUrl).toUrl());
    }

    public String confirmUserAuthentication(Long opId) throws IOException {
        final HttpGet req = new HttpGet(new URLBuilder(serverUrl).addPath("api/idvkey/customerservice/confirmauth/" + opId).toUri());
        final CloseableHttpResponse response = httpClient.execute(req);
        checkStatus(response);
        return StringUtils.utf8(IOUtils.toByteArray(response.getEntity().getContent()));
    }

    private void checkStatus(CloseableHttpResponse response) throws IOException {
        final int retCode = response.getStatusLine().getStatusCode();
        if (retCode < 200 || retCode > 299) {
            throw new IOException("Server returned " + response.getStatusLine());
        }
    }
}
