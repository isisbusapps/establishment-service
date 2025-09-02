package uk.ac.stfc.facilities.client.rest.resources;

import com.ninja_squad.dbsetup.operation.Operation;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static uk.ac.stfc.facilities.client.rest.base.Constants.*;

public class EstablishmentData {
    public static Operation data = sequenceOf(
            insertInto("ESTABLISHMENT_NEW")
                    .columns("ID",              "ESTABLISHMENT_NAME",  "ROR_ID",       "COUNTRY_NAME",   "ESTABLISHMENT_URL",  "FROM_DATE",  "THRU_DATE",  "VERIFIED")
                    .values(VERIFIED_EST_ID,    VERIFIED_EST_NAME,     "rorstring1",   "UK",             "aaa",                null,         null,         1         )
                    .values(UNVERIFIED_EST_ID,  UNVERIFIED_EST_NAME,   null,           null,             null,                 null,         null,         0         )
                    .build(),

            insertInto("ESTABLISHMENT_ALIAS")
                    .columns("ALIAS_ID", "ESTABLISHMENT_ID",  "ALIAS")
                    .values(ALIAS_ID,     VERIFIED_EST_ID,   VERIFIED_EST_ALIAS)
                    .build(),

            insertInto("ESTABLISHMENT_CATEGORY_LINK")
                    .columns("ESTABLISHMENT_ID", "CATEGORY_ID")
                    .values(VERIFIED_EST_ID,     VERIFIED_EST_CATEGORY_ID)
                    .build()
    );
}
