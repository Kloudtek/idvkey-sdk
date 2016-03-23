/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    OperationResult requestAuthentication(@Valid AuthenticationRequest authenticationRequest);

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
    AuthenticationStatus getAuthenticationStatus(@NotNull @PathParam("opId") String opId);

    @POST
    @Path("approval")
    @AuthenticateCustomer
    @Consumes("application/json")
    @Produces("application/json")
    OperationResult requestApproval(@Valid ApprovalRequest req);

    @GET
    @Path("approval/{opId}")
    @AuthenticateCustomer
    @Produces("text/plain")
    ApprovalState getUserApprovalState(@NotNull @PathParam("opId") String opId);
}
