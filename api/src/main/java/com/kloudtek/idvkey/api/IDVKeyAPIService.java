/*
 * Copyright (c) 2015 Kloudtek Ltd
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
     * Request a user for his permission to link him to a customer service/website
     *
     * @param serviceId       Website service id
     * @param userRef         User reference that will represent this user within the specified service/website
     * @param securityContext AJAX security context
     * @return Operation Id
     */
    @POST
    @Path("linkuser/{serviceId}/{userRef}")
    @AuthenticateCustomer
    String linkUserToCustomerService(@PathParam("serviceId") String serviceId, @PathParam("userRef") String userRef,
                                     @Context SecurityContext securityContext);

    @GET
    @Path("linkuser/{serviceId}/{userRef}")
    @AuthenticateCustomer
    @Produces("text/plain")
    boolean isUserLinkedToCustomerService(@PathParam("serviceId") String serviceId, @PathParam("userRef") String userRef,
                                          @Context SecurityContext securityContext);

    @GET
    @Path("authentication/request/{serviceId}")
    @AuthenticateCustomer
    String requestUserAuthentication(@PathParam("serviceId") String serviceId, @Context SecurityContext securityContext);

    @GET
    @Path("authentication/confirm/{opId}")
    @AuthenticateCustomer
    String confirmUserAuthentication(@PathParam("opId") Long opId);

    @POST
    @Path("approval/request/{serviceId}/{userRef}")
    @AuthenticateCustomer
    @Consumes("application/json")
    @Produces("text/plain")
    String requestApproval(@PathParam("serviceId") String serviceId, @PathParam("userRef") String userRef,
                           ApprovalRequest req, @Context SecurityContext securityContext);

    @GET
    @Path("approval/state/{opId}")
    @AuthenticateCustomer
    @Produces("text/plain")
    ApprovalState getUserApprovalState(@PathParam("opId") String opId, @Context SecurityContext securityContext);
}
