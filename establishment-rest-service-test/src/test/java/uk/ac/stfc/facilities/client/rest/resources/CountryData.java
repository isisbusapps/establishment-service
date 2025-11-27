package uk.ac.stfc.facilities.client.rest.resources;

import com.ninja_squad.dbsetup.operation.Operation;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static uk.ac.stfc.facilities.client.rest.base.Constants.*;

public class CountryData {

    public static Operation data = sequenceOf(insertInto("COUNTRY_NEW")
                .columns("ID",     "COUNTRY_NAME")
                .values(COUNTRY_ID_1,  TEST_COUNTRY_1)
                .values(COUNTRY_ID_2,  TEST_COUNTRY_2)
                .build());
}
