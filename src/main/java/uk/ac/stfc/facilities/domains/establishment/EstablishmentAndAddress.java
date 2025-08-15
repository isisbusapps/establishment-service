package uk.ac.stfc.facilities.domains.establishment;

import com.stfc.UserOffice.domains.address.Address;

public class EstablishmentAndAddress {
    Establishment est;
    Address address;

    public EstablishmentAndAddress() {
    }

    public EstablishmentAndAddress(Establishment est, Address address) {
        this.est = est;
        this.address = address;
    }

    public Establishment getEst() {
        return est;
    }

    public Address getAddress() {
        return address;
    }

    public void setEst(Establishment est) {
        this.est = est;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
