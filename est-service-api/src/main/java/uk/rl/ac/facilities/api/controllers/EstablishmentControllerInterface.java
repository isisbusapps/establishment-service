package uk.rl.ac.facilities.api.controllers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import uk.rl.ac.facilities.api.dto.EstablishmentDTO;
import uk.rl.ac.facilities.api.dto.EstablishmentDetailsDTO;
import uk.rl.ac.facilities.facilities.api.generated.ror.RorSchemaV21;

import java.util.List;

@Path("establishment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface EstablishmentControllerInterface {

    @GET
    @Path("/{establishmentId}")
    EstablishmentDTO getEstablishment(@PathParam("establishmentId") Long establishmentId);

    @GET
    @Path("/{establishmentId}/details")
    EstablishmentDetailsDTO getEstablishmentDetails(@PathParam("establishmentId") Long establishmentId);

    @GET
    @Path("/search")
    List<EstablishmentDTO>  getEstablishmentsByQuery(
            @QueryParam("searchQuery") String searchQuery,
            @QueryParam("useAlias") boolean useAlias,
            @QueryParam("onlyVerified") boolean onlyVerified,
            @QueryParam("limit") int limit
    );

    @GET
    @Path("/unverified")
    List<EstablishmentDTO> getUnverifiedEstablishments();

    @POST
    Response createUnverifiedEstablishment(String establishmentName);

    @GET
    @Path("/ror-search")
    List<RorSchemaV21>  getRorMatches(
            @QueryParam("searchQuery") String searchQuery
    );

    @PUT
    @Path("/{establishmentId}/ror-enrich-verify")
    Response rorVerifyAndEnrichData(@PathParam("establishmentId") Long establishmentId,
                                    RorSchemaV21 rorMatch);

    @PUT
    @Path("/{establishmentId}/manual-enrich-verify")
    Response manualVerifyAndEnrichData(@PathParam("establishmentId") Long establishmentId,
                                    EstablishmentDTO inputEst);

    @PUT
    @Path("/{establishmentId}/aliases")
    Response addEstablishmentAliases(@PathParam("establishmentId") Long establishmentId,
                                     List<String> aliasName);

    @PUT
    @Path("/{establishmentId}/categories")
    Response addEstablishmentCategoryLinks(@PathParam("establishmentId") Long establishmentId,
                                           List<Long> categoryIds);

    @DELETE
    @Path("/{establishmentId}")
    Response deleteEstablishmentAndLinkedDepartments(@PathParam("establishmentId") Long establishmentId);

}