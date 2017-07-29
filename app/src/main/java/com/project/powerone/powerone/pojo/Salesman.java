package com.project.powerone.powerone.pojo;

/**
 * Created by aharoldk on 22/07/17.
 */

public class Salesman {
    private String SalesmanID;
    private String SalesmanName;
    private String SiteID;
    private String Password;
    private String dLastLogin;

    public Salesman() {
    }

    public Salesman(String salesmanID, String salesmanName, String siteID, String password, String dLastLogin) {
        SalesmanID = salesmanID;
        SalesmanName = salesmanName;
        SiteID = siteID;
        Password = password;
        this.dLastLogin = dLastLogin;
    }

    public String getSalesmanID() {
        return SalesmanID;
    }

    public void setSalesmanID(String salesmanID) {
        SalesmanID = salesmanID;
    }

    public String getSalesmanName() {
        return SalesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        SalesmanName = salesmanName;
    }

    public String getSiteID() {
        return SiteID;
    }

    public void setSiteID(String siteID) {
        SiteID = siteID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getdLastLogin() {
        return dLastLogin;
    }

    public void setdLastLogin(String dLastLogin) {
        this.dLastLogin = dLastLogin;
    }
}
