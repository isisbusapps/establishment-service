package uk.rl.ac.facilities.rest.helpers;

import jakarta.ws.rs.BadRequestException;
import uk.rl.ac.facilities.rest.exceptions.InvalidInputException;

import java.net.MalformedURLException;
import java.net.URI;

public class InputValidation {

    public static void validateUrl(String url) {
        if (url == null || url.isBlank()) {
            return;
        }

        if (!url.matches("^[a-zA-Z][a-zA-Z0-9+.-]*://.*")) {
            throw new InvalidInputException(
                    "Invalid URL provided: " + url
            );
        }

        try {
            URI.create(url).toURL();
        } catch (MalformedURLException e) {
            throw new InvalidInputException(
                    "Invalid URL provided: " + url
            );
        }
    }
}
