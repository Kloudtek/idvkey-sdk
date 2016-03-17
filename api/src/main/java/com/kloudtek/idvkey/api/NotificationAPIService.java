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
    @Path("approve")
    @AuthenticateCustomer
    @Consumes("application/json")
    @Produces("application/json")
    OperationResult requestApproval(@QueryParam("serviceId") String serviceId, @QueryParam("userRef") String userRef,
                                    @QueryParam("redirectUrl") String redirectUrl, @QueryParam("cancelUrl") String cancelUrl,
                                    ApprovalRequest req);

    @GET
    @Path("approve")
    @AuthenticateCustomer
    @Produces("text/plain")
    ApprovalState getUserApprovalState(@QueryParam("opId") String opId);
}
