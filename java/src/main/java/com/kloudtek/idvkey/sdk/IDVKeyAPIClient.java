/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kloudtek.idvkey.api.*;
import com.kloudtek.kryptotek.CryptoUtils;
import com.kloudtek.kryptotek.DigestAlgorithm;
import com.kloudtek.kryptotek.jce.JCECryptoEngine;
import com.kloudtek.kryptotek.key.HMACKey;
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
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.security.InvalidKeyException;
import java.util.List;

import static com.kloudtek.util.StringUtils.base64Decode;
import static org.apache.http.auth.AuthScope.ANY;

/**
 * Allows to perform API operations on the IDVKey services
 */
public class IDVKeyAPIClient {
    public static final int DEFAULT_TIMEOUT = 30000;
    public static final String IDVKEY_URL = "https://api.idvkey.com";
    protected CloseableHttpClient httpClient;
    protected String serverUrl;
    private static final ObjectMapper jsonMapper;

    static {
        jsonMapper = new ObjectMapper();
    }

    /**
     * Constructor
     *
     * @param keyId     Key id
     * @param base64Key A {@link SignAndVerifyKey} key
     * @throws InvalidKeyException if the key was invalid
     */
    public IDVKeyAPIClient(String keyId, KeyType keyType, String base64Key) throws InvalidKeyException {
        this(keyId, keyType, base64Decode(base64Key));
    }

    /**
     * Constructor
     *
     * @param keyId   Key id
     * @param keyData A {@link SignAndVerifyKey} key
     * @throws InvalidKeyException if the key was invalid
     */
    public IDVKeyAPIClient(String keyId, KeyType keyType, byte[] keyData) throws InvalidKeyException {
        if (keyType != KeyType.HMAC_SHA256) {
            throw new IllegalArgumentException("This constructor only support HMAC SHA-256 at this time");
        }
        final HMACKey key = CryptoUtils.readHMACKey(DigestAlgorithm.SHA256, keyData);
        init(IDVKEY_URL, keyId, key, key, DEFAULT_TIMEOUT);
    }

    /**
     * Constructor
     *
     * @param keyId Key id
     * @param key   A {@link SignAndVerifyKey} key
     */
    public IDVKeyAPIClient(String keyId, SignAndVerifyKey key) {
        this(keyId, key, key, DEFAULT_TIMEOUT);
    }

    /**
     * Constructor (only use this to connect to IDVKey test server)
     *
     * @param serverUrl Server URL
     * @param id        Key id
     * @param key       A {@link SignAndVerifyKey} key
     */
    public IDVKeyAPIClient(String serverUrl, String id, SignAndVerifyKey key) {
        this(serverUrl, id, key, key, DEFAULT_TIMEOUT);
    }

    public IDVKeyAPIClient(String keyId, SigningKey signingKey, SignatureVerificationKey signatureVerificationKey, int timeout) {
        this(IDVKEY_URL, keyId, signingKey, signatureVerificationKey, timeout);
    }

    public IDVKeyAPIClient(String serverUrl, String keyId, SigningKey signingKey, SignatureVerificationKey signatureVerificationKey, int timeout) {
        init(serverUrl, keyId, signingKey, signatureVerificationKey, timeout);
    }

    private void init(String serverUrl, String keyId, SigningKey signingKey, SignatureVerificationKey signatureVerificationKey, int timeout) {
        this.serverUrl = serverUrl;
        // Create HTTP Client with REST security interceptor
        final HCInterceptor hcInterceptor = new HCInterceptor(new JCECryptoEngine(), null);
        HttpClientBuilder httpClientBuilder = hcInterceptor.createClientBuilder();
        RequestConfig.Builder requestBuilder = RequestConfig.custom().setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout).setSocketTimeout(timeout);
        httpClientBuilder.setDefaultRequestConfig(requestBuilder.build());
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        // This is used to used to adjust http timestamps in case client has the wrong time
        final TimeAsHttpContentTimeSync timeSync = new TimeAsHttpContentTimeSync(new URLBuilder(serverUrl).path("/public/time").toString());
        credentialsProvider.setCredentials(ANY, new RestAuthCredential(keyId, signingKey, signatureVerificationKey,
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
     * Return the list of services that have been registered on IDVKey
     *
     * @return list of services
     */
    public List<Service> getServices() throws IOException {
        return jsonMapper.readValue(get("api/services"), jsonMapper.getTypeFactory().constructCollectionType(List.class, Service.class));
    }

    /**
     * Link an IDVKey user to your service/website.
     * You need to call this operation before a user on your website can use his IDVKey device
     *
     * @param url       The URL to which the user's browser will be redirected to after he's approved the link
     * @param serviceId Your website serviceId
     * @param userRef   User reference (generally the user's username on your website)
     * @param cancelUrl URL to redirect user to should he wish to cancel the linking
     * @return URL you should redirect your user's browser to, in order for him to approve the linking
     * @throws IOException                If the server returned an error
     * @throws UserAlreadyLinkedException If the user was already linked
     * @throws ServiceNotFoundException   If the service was not found
     */
    public OperationResult linkUser(String serviceId, URL url, String userRef, URL cancelUrl) throws IOException, UserAlreadyLinkedException, ServiceNotFoundException {
        try {
            return jsonMapper.readValue(postJson("api/services/" + serviceId + "/links", new ServiceLinkRequest(userRef, url, cancelUrl)), OperationResult.class);
        } catch (HttpException e) {
            if (e.getStatusCode() == 409) {
                throw new UserAlreadyLinkedException();
            } else if (e.getStatusCode() == 404) {
                throw new ServiceNotFoundException();
            } else {
                throw e;
            }
        }
    }

    /**
     * Unlink an IDVKey user to your service/website.
     *
     * @param serviceId Your website serviceId
     * @param userRef   User reference (generally the user's username on your website)
     * @throws IOException If the server returned an error
     */
    public void unlinkUser(String serviceId, String userRef) throws IOException {
        try {
            delete(new URLBuilder("api/services/" + serviceId + "/links/ref/").path(userRef, true).toString());
        } catch (HttpException e) {
            if (e.getStatusCode() != 404) {
                throw e;
            }
        }
    }

    /**
     * Check if a user has been linked against your website.
     * Use this to verify the user has been successfully linked to your website/service after he's been redirected to
     * the redirectUrl you specified in {@link #linkUser(String, URL, String, URL)}.
     *
     * @param serviceId Website serviceId
     * @param opId      Operation id
     * @return Service link details or null if no service link with that userRef was found.
     * @throws IOException If error occurred performing the operation
     */
    public ServiceLinkRequestStatus getServiceLinkRequestStatus(String serviceId, String opId) throws IOException {
        try {
            String statusStr = get(new URLBuilder("api/services/" + serviceId + "/links/requests/").path(opId, true).toString());
            try {
                return ServiceLinkRequestStatus.valueOf(statusStr);
            } catch (IllegalArgumentException e) {
                throw new IOException("Server returned invalid status value: " + statusStr);
            }
        } catch (HttpException e) {
            if (e.getStatusCode() == 404) {
                return null;
            } else {
                throw e;
            }
        }
    }

    /**
     * Check if a user has been linked against your website.
     * Use this to verify the user has been successfully linked to your website/service after he's been redirected to
     * the redirectUrl you specified in {@link #linkUser(String, URL, String, URL)}.
     *
     * @param serviceId Website serviceId
     * @param userRef   User reference (generally the user's username on your website)
     * @return Service link details or null if no service link with that userRef was found.
     * @throws IOException If error occurred performing the operation
     */
    public ServiceLink getServiceLinkInfo(String serviceId, String userRef) throws IOException {
        try {
            return getJson(new URLBuilder("api/services/" + serviceId + "/links/ref/").path(userRef, true).toString(), ServiceLink.class);
        } catch (HttpException e) {
            if (e.getStatusCode() == 404) {
                return null;
            } else {
                throw e;
            }
        }
    }

    /**
     * Initiate an IDVKey authentication for a user
     *
     * @param serviceId   Website serviceId
     * @param redirectUrl URL that the user's browser should be redirected to after he's performed the authentication.
     * @param cancelUrl   URL that the user's browser should be redirected to if he cancelled the authentication.
     * @return Operation result. This will contain the URL you should redirect your user's browser to.
     * ({@link OperationResult#getRedirectUrl()}), and an operation id that you will use to verify that the user has completed
     * authentication successfully ({@link OperationResult#getOpId()})
     * @throws IOException If error occurred performing the operation
     */
    public OperationResult authenticateUser(@NotNull String serviceId, @NotNull URL redirectUrl, URL cancelUrl) throws IOException {
        return postJson("api/notifications/authentication", new AuthenticationRequest(serviceId, redirectUrl, cancelUrl),
                OperationResult.class);
    }

    /**
     * Confirm that user Authentication was done successfully
     *
     * @param opId Operation id returned by {@link #authenticateUser(String, URL, URL)}
     * @return Authentication status
     * @throws IOException If error occurred performing the operation
     */
    public AuthenticationStatus getAuthenticationStatus(@NotNull String opId) throws IOException {
        return getJson("api/notifications/authentication/" + opId, AuthenticationStatus.class);
    }

    /**
     * Request for a user to approve an operation using IDVKey
     *
     * @param approvalRequest Approval request details
     * @return operation result
     * @throws IOException If an error occurs while performing the operation
     */
    @SuppressWarnings("ConstantConditions")
    public OperationResult requestApproval(@NotNull ApprovalRequest approvalRequest) throws IOException {
        if (approvalRequest == null) {
            throw new IllegalArgumentException("approval request missing");
        } else if (StringUtils.isBlank(approvalRequest.getTitle())) {
            throw new IllegalArgumentException("approval title missing");
        } else if (StringUtils.isBlank(approvalRequest.getText())) {
            throw new IllegalArgumentException("approval text missing");
        }
        return postJson(new URLBuilder("api/notifications/approval").toString(), approvalRequest, OperationResult.class);
    }

    /**
     * Check what is the approval state of an operation
     *
     * @param opId Operation Id
     * @return approval state
     * @throws IOException If an error occurs while performing the operation
     */
    public ApprovalRequestStatus getApprovalState(@NotNull String opId) throws IOException {
        return getJson("api/notifications/approval/" + opId, ApprovalRequestStatus.class);
    }

    private void checkStatus(CloseableHttpResponse response) throws IOException {
        final int retCode = response.getStatusLine().getStatusCode();
        if (retCode < 200 || retCode > 299) {
            String body;
            if (response.getEntity() != null) {
                try {
                    body = IOUtils.toString(response.getEntity().getContent());
                } catch (Exception e) {
                    body = "";
                }
            } else {
                body = "";
            }
            throw new HttpException(response.getStatusLine().getReasonPhrase(), response.getStatusLine().getStatusCode(), body);
        }
    }

    protected String get(String path) throws IOException {
        return exec(new HttpGet(buildUrl(path)));
    }

    protected String delete(String path) throws IOException {
        return exec(new HttpDelete(buildUrl(path)));
    }

    protected <X> X getJson(String path, Class<X> expectedJsonReturnClass) throws IOException {
        final HttpGet req = new HttpGet(buildUrl(path));
        return jsonMapper.readValue(exec(req), expectedJsonReturnClass);
    }

    protected String postJson(String path, Object obj) throws IOException {
        final HttpPost post = new HttpPost(buildUrl(path));
        post.setEntity(new ByteArrayEntity(jsonMapper.writeValueAsBytes(obj), ContentType.APPLICATION_JSON));
        return exec(post);
    }

    protected <X> X postJson(String path, Object obj, Class<X> expectedJsonReturnClass) throws IOException {
        final HttpPost post = new HttpPost(buildUrl(path));
        post.setEntity(new ByteArrayEntity(jsonMapper.writeValueAsBytes(obj), ContentType.APPLICATION_JSON));
        return jsonMapper.readValue(exec(post), expectedJsonReturnClass);
    }

    protected String post(String path, String string) throws IOException {
        final HttpPost post = new HttpPost(buildUrl(path));
        if (string != null) {
            post.setEntity(new StringEntity(string));
        }
        return exec(post);
    }

    private String exec(HttpUriRequest req) throws IOException {
        req.setHeader("api-version", "1.0");
        final CloseableHttpResponse response = httpClient.execute(req);
        try {
            checkStatus(response);
            if (response.getEntity() != null) {
                return StringUtils.utf8(IOUtils.toByteArray(response.getEntity().getContent()));
            } else {
                return null;
            }
        } finally {
            response.close();
        }
    }

    private URLBuilder url(String path) {
        return new URLBuilder(serverUrl).path(path);
    }

    private URI buildUrl(String path) {
        return new URLBuilder(serverUrl).path(path).toUri();
    }

    private URI linkUserUrl(String serviceId, String userRef) {
        return linkUrlUrlBuilder(serviceId, userRef).toUri();
    }

    private URLBuilder linkUrlUrlBuilder(String serviceId, String userRef) {
        return url("api/services/" + serviceId + "/links/ref/").path(userRef);
    }
}
