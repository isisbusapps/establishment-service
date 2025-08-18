package uk.ac.stfc.facilities.BaseClasses;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public abstract class DateConverter {

    public ZonedDateTime instantToZonedDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return instant.atZone(ZoneId.systemDefault());
    }

    public Instant zonedDateTimeToInstant(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }
        return zonedDateTime.toInstant();
    }
}
