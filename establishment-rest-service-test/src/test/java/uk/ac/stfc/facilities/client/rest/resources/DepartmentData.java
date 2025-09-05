package uk.ac.stfc.facilities.client.rest.resources;

import com.ninja_squad.dbsetup.operation.Operation;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static uk.ac.stfc.facilities.client.rest.base.Constants.*;

public class DepartmentData {
    public static Operation data = sequenceOf(
            insertInto("DEPARTMENT")
                    .columns("ID",               "DEPARTMENT_NAME",    "OLD_ESTABLISHMENT_ID",  "ESTABLISHMENT_ID")
                    .values( TEST_DEPARTMENT_ID,  TEST_DEPARTMENT_NAME,  null,                    VERIFIED_EST_ID     )
                    .build(),

            insertInto("DEPARTMENT_LABEL_LINK")
                    .columns("DEPARTMENT_ID",     "LABEL_ID")
                    .values( TEST_DEPARTMENT_ID,  TEST_LABEL_ID)
                    .build()
    );
}
