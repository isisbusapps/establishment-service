package uk.ac.stfc.facilities.controllers;

import uk.ac.stfc.facilities.domains.establishment.EstablishmentDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("establishment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface EstablishmentControllerInterface {

    @GET
    @Path("/unverified")
    List<EstablishmentDTO> getUnverifiedEstablishments();






}