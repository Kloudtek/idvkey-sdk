/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Interface of JAX-RS IDVKey REST API server
 */
public interface IDVKeyAPIService {
    /**
     * Request a user for his permission to link him to a customer service/website.
     *
     * @return URL to redirect user for him to perform authentication
     * @param serviceId       Website service id
     * @param userRef         User reference that will represent this user within the specified service/website
     * @param redirectUrl     URL to redirect to after user approval is completed
     * @param cancelUrl       URL to redirect to if the user wants to cancel the operation
     * @param securityContext AJAX security context  @return Operation Id
     */
    @POST
    @Path("linkuser")
    @AuthenticateCustomer
    String linkUserToCustomerService(@QueryParam("serviceId") String serviceId, @QueryParam("userRef") String userRef,
                                     @QueryParam("redirectUrl") String redirectUrl, @QueryParam("cancelUrl") String cancelUrl, @Context SecurityContext securityContext);

    @GET
    @Path("linkuser")
    @AuthenticateCustomer
    @Produces("text/plain")
    boolean isUserLinkedToCustomerService(@QueryParam("serviceId") String serviceId, @QueryParam("userRef") String userRef,
                                          @Context SecurityContext securityContext);

    @DELETE
    @Path("linkuser")
    @AuthenticateCustomer
    void unlinkUserFromCustomerService(@QueryParam("serviceId") String serviceId, @QueryParam("userRef") String userRef,
                                       @Context SecurityContext securityContext);

    @POST
    @Path("authenticate")
    @AuthenticateCustomer
    @Produces("application/json")
    OperationResult requestUserAuthentication(@QueryParam("serviceId") String serviceId, @QueryParam("redirectUrl") String redirectUrl,
                                              @QueryParam("cancelUrl") String cancelUrl, @Context SecurityContext securityContext);

    /**
     * Get the user ref for the user authenticated with that operation id
     *
     * @param opId Operation id
     * @return User ref
     */
    @GET
    @Path("authenticate")
    @AuthenticateCustomer
    String confirmUserAuthentication(@QueryParam("opId") String opId);

    @POST
    @Path("approve")
    @AuthenticateCustomer
    @Consumes("application/json")
    @Produces("application/json")
    OperationResult requestApproval(@QueryParam("serviceId") String serviceId, @QueryParam("userRef") String userRef,
                                    @QueryParam("redirectUrl") String redirectUrl, @QueryParam("cancelUrl") String cancelUrl,
                                    ApprovalRequest req, @Context SecurityContext securityContext);

    @GET
    @Path("approve")
    @AuthenticateCustomer
    @Produces("text/plain")
    ApprovalState getUserApprovalState(@QueryParam("opId") String opId, @Context SecurityContext securityContext);
}
