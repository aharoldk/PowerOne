package com.project.powerone.powerone.pojo;

/**
 * Created by aharoldk on 31/07/17.
 */

public class Price {

    private int UrutID, SalesPrice;
    private String SiteID, ProductID, ProductType;

    public Price() {
    }

    public Price(int urutID, int salesPrice, String siteID, String productID, String productType) {
        UrutID = urutID;
        SalesPrice = salesPrice;
        SiteID = siteID;
        ProductID = productID;
        ProductType = productType;
    }

    public int getUrutID() {
        return UrutID;
    }

    public void setUrutID(int urutID) {
        UrutID = urutID;
    }

    public int getSalesPrice() {
        return SalesPrice;
    }

    public void setSalesPrice(int salesPrice) {
        SalesPrice = salesPrice;
    }

    public String getSiteID() {
        return SiteID;
    }

    public void setSiteID(String siteID) {
        SiteID = siteID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductType() {
        return ProductType;
    }

    public void setProductType(String productType) {
        ProductType = productType;
    }
}
