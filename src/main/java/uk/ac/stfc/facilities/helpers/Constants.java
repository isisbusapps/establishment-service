package uk.ac.stfc.facilities.helpers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Constants {

    public static final Set<String> DEPT_STOPWORDS = new HashSet<>(Arrays.asList(
            "department", "dept", "division", "office", "school", "faculty", "laboratory", "center", "centre",
            "institute", "facility", "research", "ltd", "plc",
            "of", "for", "and", "the", "a", "an", "in", "on", "with", "to", "by", "under", "from",
            "le", "an", "des", "et", "institut", "departamento", "di", "de", "du"
    ));
    public static final int EST_SEARCH_CUTOFF = 60; // minimum similarity score (0-100) for a match
    public static final int DEPT_LABEL_CUTOFF = 80; // minimum similarity score (0-100) for a match
    public static final String FALLBACK_LABEL_NAME = "Other";

}
