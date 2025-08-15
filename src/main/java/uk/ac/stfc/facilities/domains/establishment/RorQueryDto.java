package uk.ac.stfc.facilities.domains.establishment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RorQueryDto {

    private List<RorSchemaV21> items;

    public List<RorSchemaV21> getItems() {
        return items;
    }

    public void setItems(List<RorSchemaV21> items) {
        this.items = items;
    }


    @Override
    public String toString() {
        if (items == null || items.isEmpty()) {
            return "No items";
        }
        StringBuilder printString = new StringBuilder();
        printString.append("No. of items: ").append(items.size()).append("; ");
        for (RorSchemaV21 item : items) {
            printString.append(item.getId()).append(", ");
        }
        if (printString.length() > 2) {
            printString.setLength(printString.length() - 2);
        }
        return printString.toString();
    }
}
