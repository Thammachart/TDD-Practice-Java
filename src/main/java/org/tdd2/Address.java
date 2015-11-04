package org.tdd2;

public class Address {
    private String streetAddressOne;
    private String streetAddressTwo;
    private String city;
    private String state;
    private String zip;

    public String getStreetAddressOne() {
        return streetAddressOne;
    }

    public void setStreetAddressOne(String streetAddressOne) {
        this.streetAddressOne = streetAddressOne;
    }

    public String getStreetAddressTwo() {
        return streetAddressTwo;
    }

    public void setStreetAddressTwo(String streetAddressTwo) {
        this.streetAddressTwo = streetAddressTwo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (streetAddressOne != null ? !streetAddressOne.equals(address.streetAddressOne) : address.streetAddressOne != null)
            return false;
        if (streetAddressTwo != null ? !streetAddressTwo.equals(address.streetAddressTwo) : address.streetAddressTwo != null)
            return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (state != null ? !state.equals(address.state) : address.state != null) return false;
        return !(zip != null ? !zip.equals(address.zip) : address.zip != null);

    }

    @Override
    public int hashCode() {
        int result = streetAddressOne != null ? streetAddressOne.hashCode() : 0;
        result = 31 * result + (streetAddressTwo != null ? streetAddressTwo.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (zip != null ? zip.hashCode() : 0);
        return result;
    }
}
