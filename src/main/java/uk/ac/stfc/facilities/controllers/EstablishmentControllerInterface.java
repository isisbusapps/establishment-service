package uk.ac.stfc.facilities.controllers;

import uk.ac.stfc.facilities.domains.establishment.EstablishmentDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Tag(name = "establishment")
@Path("establishment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface EstablishmentControllerInterface {

    @GET
    @Path("/unverified")
    @Operation(summary = "Get all unverified establishments",
            description = "This endpoint is anonymous and does not require any authentication")
    @APIResponse(responseCode = "200", description = "Establishments found")
    List<EstablishmentDTO> getUnverifiedEstablishments();


}