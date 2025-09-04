package uk.ac.stfc.facilities.domains.establishment;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import uk.ac.stfc.facilities.BaseClasses.Dto;

public class CategoryDTO implements Dto {

    @Schema(
            description = "The unique identifier for the category",
            example = "1",
            required = true,
            readOnly = true
    )
    private Long categoryId;

    @Schema(
            description = "The name of the category",
            example = "education",
            required = true,
            nullable = false
    )
    private String categoryName;

    public Long getCategoryId() {return categoryId;}

    public void setCategoryId(Long categoryId) {this.categoryId = categoryId;}

    public String getCategoryName() {return categoryName;}

    public void setCategoryName(String categoryName) {this.categoryName = categoryName;}
}
