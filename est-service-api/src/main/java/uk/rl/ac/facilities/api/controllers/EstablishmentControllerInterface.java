package uk.rl.ac.facilities.api.controllers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import uk.rl.ac.facilities.api.dto.CreateEstDTO;
import uk.rl.ac.facilities.api.dto.EstSearchQueryDTO;
import uk.rl.ac.facilities.api.dto.EstablishmentDTO;
import uk.rl.ac.facilities.facilities.api.generated.ror.RorSchemaV21;

import java.util.List;

@Path("establishment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface EstablishmentControllerInterface {

    @GET
    @Path("/{establishmentId}")
    EstablishmentDTO getEstablishment(@PathParam("establishmentId") Long establishmentId);

    @POST
    @Path("/search")
    List<EstablishmentDTO>  getEstablishmentsByQuery(
            EstSearchQueryDTO name,
            @QueryParam("useAlias") @DefaultValue("true") Boolean useAlias,
            @QueryParam("onlyVerified") @DefaultValue("true") Boolean onlyVerified,
            @QueryParam("limit") @DefaultValue("100") int limit
    );

    @GET
    @Path("/unverified")
    List<EstablishmentDTO> getUnverifiedEstablishments();

    @POST
    Response createUnverifiedEstablishment(CreateEstDTO establishmentName);

    @GET
    @Path("/ror-search")
    List<RorSchemaV21>  getRorMatches(
            @QueryParam("searchQuery") String searchQuery
    );

    @PUT
    @Path("/{establishmentId}/enrich-verify/ror")
    EstablishmentDTO rorVerifyAndEnrichData(@PathParam("establishmentId") Long establishmentId,
                                    RorSchemaV21 rorMatch);

    @PUT
    @Path("/{establishmentId}/enrich-verify")
    Response manualVerifyAndEnrichData(@PathParam("establishmentId") Long establishmentId,
                                    EstablishmentDTO inputEst);

    @PUT
    @Path("/{establishmentId}/aliases")
    EstablishmentDTO addEstablishmentAliases(@PathParam("establishmentId") Long establishmentId,
                                     List<String> aliasName);

    @PUT
    @Path("/{establishmentId}/categories")
    EstablishmentDTO addEstablishmentCategoryLinks(@PathParam("establishmentId") Long establishmentId,
                                           List<Long> categoryIds);

    @DELETE
    @Path("/{establishmentId}/aliases")
    Response deleteEstablishmentAliases(@PathParam("establishmentId") Long establishmentId);

    @DELETE
    @Path("/{establishmentId}/categories")
    Response deleteEstablishmentCategoryLinks(@PathParam("establishmentId") Long establishmentId);

    @DELETE
    @Path("/{establishmentId}")
    void deleteEstablishmentAndLinkedDepartments(@PathParam("establishmentId") Long establishmentId);

}