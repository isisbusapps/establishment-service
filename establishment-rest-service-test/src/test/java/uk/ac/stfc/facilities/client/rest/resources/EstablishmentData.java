package uk.ac.stfc.facilities.client.rest.resources;

import com.ninja_squad.dbsetup.operation.Operation;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static uk.ac.stfc.facilities.client.rest.base.Constants.*;

public class EstablishmentData {
    public static Operation data = sequenceOf(
            insertInto("ESTABLISHMENT_NEW")
                    .columns("ID",              "ESTABLISHMENT_NAME",  "ROR_ID",       "COUNTRY_NAME",   "ESTABLISHMENT_URL",  "FROM_DATE",  "THRU_DATE",  "VERIFIED")
                    .values(VERIFIED_EST_ID,    VERIFIED_EST_NAME,     "rorstring1",   "UK",             "http://acme.test",                null,         null,         true         )
                    .values(UNVERIFIED_EST_ID,  UNVERIFIED_EST_NAME,   null,           null,             null,                 null,         null,         false         )
                    .build(),

            insertInto("ESTABLISHMENT_ALIAS")
                    .columns("ALIAS_ID", "ESTABLISHMENT_ID",  "ALIAS")
                    .values(ALIAS_ID,     VERIFIED_EST_ID,   VERIFIED_EST_ALIAS)
                    .build(),

            insertInto("CATEGORY")
                    .columns("ID",      "CATEGORY_NAME")
                    .values(CATEGORY_ID, CATEGORY_NAME)
                    .values(ROR_TYPE_ID_1, ROR_PAYLOAD_TYPE_1)
                    .values(ROR_TYPE_ID_2, ROR_PAYLOAD_TYPE_2)
                    .values(CATEGORY_ID_2, CATEGORY_NAME_2)
                    .values(CATEGORY_ID_3, CATEGORY_NAME_3)
                    .build(),

            insertInto("ESTABLISHMENT_CATEGORY_LINK")
                    .columns("ESTABLISHMENT_ID", "CATEGORY_ID")
                    .values(VERIFIED_EST_ID,     CATEGORY_ID)
                    .build()
    );
}
