package uk.rl.ac.facilities.api.dto;

public class CreateEstDTO {

    private String estName;

    private String country;

    private String url;

    public String getEstName() {
        return estName;
    }

    public void setEstName(String estName) {
        this.estName = estName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {this.country = country; }

    public String getUrl() {return url; }

    public void setUrl(String url) {this.url = url; }

}
