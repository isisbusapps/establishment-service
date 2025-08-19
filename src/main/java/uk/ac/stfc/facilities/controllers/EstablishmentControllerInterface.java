package uk.ac.stfc.facilities.controllers;

import uk.ac.stfc.facilities.domains.establishment.EstablishmentDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import uk.ac.stfc.facilities.exceptions.RestControllerException;

import java.util.List;

import static uk.ac.stfc.facilities.helpers.Constants.ESTABLISHMENT_SEARCH_LIMIT_STR;

@Path("establishment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface EstablishmentControllerInterface {

    @GET
    @Path("/top-matches")
    List<EstablishmentDTO>  getTopEstablishmentsByQuery(
            @QueryParam("searchQuery") String searchQuery,
            @QueryParam("useAlias") @DefaultValue("true") boolean useAlias,
            @QueryParam("onlyVerified") @DefaultValue("true") boolean onlyVerified,
            @QueryParam("limit") @DefaultValue(ESTABLISHMENT_SEARCH_LIMIT_STR) int limit
    ) throws RestControllerException;

    @GET
    @Path("/unverified")
    List<EstablishmentDTO> getUnverifiedEstablishments();


}