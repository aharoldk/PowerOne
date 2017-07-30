package com.project.powerone.powerone.pojo;

/**
 * Created by aharoldk on 30/07/17.
 */

public class Product {

    private String SiteID, ProductID, ProductName, BigPack, SmallPack, PrinsipalName, GroupProductName, SubGroupProductName;
    private int NoOfPack, UrutID;
    private double QtyOnHand;

    public Product() {
    }

    public Product(String siteID, String productID, String productName, String bigPack, String smallPack, String prinsipalName, String groupProductName, String subGroupProductName, int noOfPack, int urutID, double qtyOnHand) {
        SiteID = siteID;
        ProductID = productID;
        ProductName = productName;
        BigPack = bigPack;
        SmallPack = smallPack;
        PrinsipalName = prinsipalName;
        GroupProductName = groupProductName;
        SubGroupProductName = subGroupProductName;
        NoOfPack = noOfPack;
        UrutID = urutID;
        QtyOnHand = qtyOnHand;
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

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getBigPack() {
        return BigPack;
    }

    public void setBigPack(String bigPack) {
        BigPack = bigPack;
    }

    public String getSmallPack() {
        return SmallPack;
    }

    public void setSmallPack(String smallPack) {
        SmallPack = smallPack;
    }

    public String getPrinsipalName() {
        return PrinsipalName;
    }

    public void setPrinsipalName(String prinsipalName) {
        PrinsipalName = prinsipalName;
    }

    public String getGroupProductName() {
        return GroupProductName;
    }

    public void setGroupProductName(String groupProductName) {
        GroupProductName = groupProductName;
    }

    public String getSubGroupProductName() {
        return SubGroupProductName;
    }

    public void setSubGroupProductName(String subGroupProductName) {
        SubGroupProductName = subGroupProductName;
    }

    public int getNoOfPack() {
        return NoOfPack;
    }

    public void setNoOfPack(int noOfPack) {
        NoOfPack = noOfPack;
    }

    public int getUrutID() {
        return UrutID;
    }

    public void setUrutID(int urutID) {
        UrutID = urutID;
    }

    public double getQtyOnHand() {
        return QtyOnHand;
    }

    public void setQtyOnHand(double qtyOnHand) {
        QtyOnHand = qtyOnHand;
    }
}
