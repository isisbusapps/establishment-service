package uk.ac.stfc.facilities.client.rest.resources;

import com.ninja_squad.dbsetup.operation.Operation;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static uk.ac.stfc.facilities.client.rest.base.Constants.*;

public class EstablishmentData {
    public static Operation data = sequenceOf(
            insertInto("ESTABLISHMENT_NEW")
                    .columns("ID",      "ESTABLISHMENT_NAME", "ROR_ID",       "COUNTRY_NAME",   "ESTABLISHMENT_URL",  "FROM_DATE",   "THRU_DATE",  "VERIFIED")
                    .values(-600009,    VERIFIED_EST_NAME,        "rorstring1",   "UK",              "aaa",                null,         null,         1)
                    .values(-600008,    UNVERIFIED_EST_NAME,       "rorstring2",   "UK",              "bbb",                null,         null,         0 )
                    .build()
    );
}
