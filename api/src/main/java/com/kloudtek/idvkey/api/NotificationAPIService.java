/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import javax.ws.rs.*;

/**
 * Interface of JAX-RS IDVKey REST API server
 */
@Path("/notifications")
public interface NotificationAPIService {

    @POST
    @Path("authentication")
    @AuthenticateCustomer
    @Produces("application/json")
    OperationResult requestAuthentication(@QueryParam("serviceId") String serviceId, @QueryParam("redirectUrl") String redirectUrl,
                                          @QueryParam("cancelUrl") String cancelUrl);

    /**
     * Get the user ref for the user authenticated with that operation id
     *
     * @param opId Operation id
     * @return User ref
     */
    @GET
    @Path("authentication/{opId}")
    @Produces("application/json")
    @AuthenticateCustomer
    AuthenticationStatus confirmUserAuthentication(@PathParam("opId") String opId);

    @POST
    @Path("approval")
    @AuthenticateCustomer
    @Consumes("application/json")
    @Produces("application/json")
    OperationResult requestApproval(@QueryParam("serviceId") String serviceId, ApprovalRequest req);

    @GET
    @Path("approval/{opId}")
    @AuthenticateCustomer
    @Produces("text/plain")
    ApprovalState getUserApprovalState(@PathParam("opId") String opId);
}
