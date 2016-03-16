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
