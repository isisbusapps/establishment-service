package uk.ac.stfc.facilities.controllers;

import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import uk.ac.stfc.facilities.domains.establishment.EstablishmentDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import uk.ac.stfc.facilities.exceptions.RestControllerException;

import java.util.List;

import static uk.ac.stfc.facilities.helpers.Constants.DEFAULT_ESTABLISHMENT_SEARCH_LIMIT;

@Path("establishment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface EstablishmentControllerInterface {

    @GET
    @Path("/search")
    List<EstablishmentDTO>  getEstablishmentsByQuery(
            @QueryParam("searchQuery") String searchQuery,
            @QueryParam("useAlias") @DefaultValue("true") boolean useAlias,
            @QueryParam("onlyVerified") @DefaultValue("true") boolean onlyVerified,
            @QueryParam("limit") @DefaultValue(DEFAULT_ESTABLISHMENT_SEARCH_LIMIT) int limit
    ) throws RestControllerException;

    @GET
    @Path("/unverified")
    List<EstablishmentDTO> getUnverifiedEstablishments();

    @POST
    Response createUnverifiedEstablishment(@RequestBody String establishmentName)
            throws RestControllerException;



}