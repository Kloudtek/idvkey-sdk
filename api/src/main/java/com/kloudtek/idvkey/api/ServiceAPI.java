/*
 * Copyright (c) 2016 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import javax.ws.rs.*;
import java.util.List;

/**
 * Interface of JAX-RS IDVKey REST API server
 */
@Path("/services")
public interface ServiceAPI {
    @GET
    @Path("/")
    @Produces("application/json")
    @AuthenticateCustomer
    List<Service> getCustomerServiceList();

    /**
     * Request a user for his permission to link him to a customer service/website.
     *
     * @return URL to redirect user for him to perform authentication
     * @param serviceId       Website service id
     * @param userRef         User reference that will represent this user within the specified service/website
     * @param redirectUrl     URL to redirect to after user approval is completed
     * @param cancelUrl       URL to redirect to if the user wants to cancel the operation
     */
    @POST
    @Path("{serviceId}/links")
    @Produces("application/json")
    @Consumes("application/json")
    @AuthenticateCustomer
    OperationResult requestUserLink(@PathParam("serviceId") String serviceId, ServiceLinkRequest serviceLinkRequest);

    @GET
    @Path("{serviceId}/links/ref/{userRef}")
    @AuthenticateCustomer
    @Produces("application/json")
    ServiceLink getLinkStateByRef(@PathParam("serviceId") String serviceId, @PathParam("userRef") String userRef);

    @DELETE
    @Path("{serviceId}/links/ref/{userRef}")
    @AuthenticateCustomer
    void unlinkUserFromCustomerService(@PathParam("serviceId") String serviceId, @PathParam("userRef") String userRef);
}
