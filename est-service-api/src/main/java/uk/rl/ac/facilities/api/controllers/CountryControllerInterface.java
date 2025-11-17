package uk.rl.ac.facilities.api.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response; import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import uk.rl.ac.facilities.api.dto.CountryDTO;
import uk.rl.ac.facilities.api.dto.CreateEstDTO;
import uk.rl.ac.facilities.api.dto.EstSearchQueryDTO;
import uk.rl.ac.facilities.api.dto.EstablishmentDTO;
import uk.rl.ac.facilities.facilities.api.generated.ror.RorSchemaV21;

import java.util.List;

@Path("country")
@Produces(MediaType.APPLICATION_JSON)
public interface CountryControllerInterface {

    @GET
    List<CountryDTO> getCountries();

    @GET
    @Path("/{countryId}")
    CountryDTO getCountry(@PathParam("countryId") Long countryId);
}