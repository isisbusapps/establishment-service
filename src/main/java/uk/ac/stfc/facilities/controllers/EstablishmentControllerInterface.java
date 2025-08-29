package uk.ac.stfc.facilities.controllers;

import jakarta.ws.rs.core.Response;
import uk.ac.stfc.facilities.domains.establishment.EstablishmentDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import uk.ac.stfc.facilities.domains.establishment.RorSchemaV21;
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
    Response createUnverifiedEstablishment(String establishmentName)
            throws RestControllerException;

    @GET
    @Path("/ror-search")
    List<RorSchemaV21>  getRorMatches(
            @QueryParam("searchQuery") String searchQuery
    ) throws RestControllerException;

    @PUT
    @Path("/{establishmentId}/ror-enrich-verify")
    Response rorVerifyAndEnrichData(@PathParam("establishmentId") Long establishmentId,
                                    RorSchemaV21 rorMatch) throws RestControllerException;

    @PUT
    @Path("/{establishmentId}/manual-enrich-verify")
    Response manualVerifyAndEnrichData(@PathParam("establishmentId") Long establishmentId,
                                    EstablishmentDTO inputEst) throws RestControllerException;

    @PUT
    @Path("/{establishmentId}/aliases")
    Response addEstablishmentAliases(@PathParam("establishmentId") Long establishmentId,
                                     List<String> aliasName)
            throws RestControllerException;

    @PUT
    @Path("/{establishmentId}/types")
    Response addEstablishmentTypes(@PathParam("establishmentId") Long establishmentId,
                                     List<String> typeNames)
            throws RestControllerException;

    @DELETE
    @Path("/{establishmentId}")
    Response deleteEstablishmentAndLinkedDepartments(@PathParam("establishmentId") Long establishmentId)
            throws RestControllerException;

}