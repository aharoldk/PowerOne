package com.project.powerone.powerone.pojo;

/**
 * Created by aharoldk on 30/07/17.
 */

public class Customer {

    private String SiteID, SalesmanID, CustID, CustName, CustAddress, PriceType, StatusCustomer, DateTime;
    private int UrutID;
    private double GeoMapLong, GeoMapLat, GPSMapLong, GPSMapLat;

    public Customer() {
    }
    public Customer(String siteID, String salesmanID, String custID, String custName, String custAddress, String priceType, String statusCustomer, String dateTime, int urutID, double geoMapLong, double geoMapLat, double gpsMapLong, double gpsMapLat) {
        SiteID = siteID;
        SalesmanID = salesmanID;
        CustID = custID;
        CustName = custName;
        CustAddress = custAddress;
        PriceType = priceType;
        StatusCustomer = statusCustomer;
        DateTime = dateTime;
        UrutID = urutID;
        GeoMapLong = geoMapLong;
        GeoMapLat = geoMapLat;
        GPSMapLong = gpsMapLong;
        GPSMapLat = gpsMapLat;
    }

    public String getSiteID() {
        return SiteID;
    }

    public void setSiteID(String siteID) {
        SiteID = siteID;
    }

    public String getSalesmanID() {
        return SalesmanID;
    }

    public void setSalesmanID(String salesmanID) {
        SalesmanID = salesmanID;
    }

    public String getCustID() {
        return CustID;
    }

    public void setCustID(String custID) {
        CustID = custID;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getCustAddress() {
        return CustAddress;
    }

    public void setCustAddress(String custAddress) {
        CustAddress = custAddress;
    }

    public String getPriceType() {
        return PriceType;
    }

    public void setPriceType(String priceType) {
        PriceType = priceType;
    }

    public String getStatusCustomer() {
        return StatusCustomer;
    }

    public void setStatusCustomer(String statusCustomer) {
        StatusCustomer = statusCustomer;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public int getUrutID() {
        return UrutID;
    }

    public void setUrutID(int urutID) {
        UrutID = urutID;
    }

    public double getGeoMapLong() {
        return GeoMapLong;
    }

    public void setGeoMapLong(double geoMapLong) {
        GeoMapLong = geoMapLong;
    }

    public double getGeoMapLat() {
        return GeoMapLat;
    }

    public void setGeoMapLat(double geoMapLat) {
        GeoMapLat = geoMapLat;
    }

    public double getGPSMapLong() {
        return GPSMapLong;
    }

    public void setGPSMapLong(double GPSMapLong) {
        this.GPSMapLong = GPSMapLong;
    }

    public double getGPSMapLat() {
        return GPSMapLat;
    }

    public void setGPSMapLat(double GPSMapLat) {
        this.GPSMapLat = GPSMapLat;
    }
}
