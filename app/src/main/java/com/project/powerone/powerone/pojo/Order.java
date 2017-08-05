package com.project.powerone.powerone.pojo;

/**
 * Created by aharoldk on 03/08/17.
 */

public class Order {

    private String SiteID, SalesmanID, CustID, ProductID;
    private int UrutID, QtyBig, QtySmall, SalesPrice;
    private double PctDisc1, PctDisc2, PctDisc3;
    private int bConfirm , bTransfer;

    public Order() {
    }

    public Order(String siteID, String salesmanID, String custID, String productID, int urutID, int qtyBig, int qtySmall, int salesPrice, double pctDisc1, double pctDisc2, double pctDisc3, int bConfirm, int bTransfer) {
        SiteID = siteID;
        SalesmanID = salesmanID;
        CustID = custID;
        ProductID = productID;
        UrutID = urutID;
        QtyBig = qtyBig;
        QtySmall = qtySmall;
        SalesPrice = salesPrice;
        PctDisc1 = pctDisc1;
        PctDisc2 = pctDisc2;
        PctDisc3 = pctDisc3;
        this.bConfirm = bConfirm;
        this.bTransfer = bTransfer;
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

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public int getUrutID() {
        return UrutID;
    }

    public void setUrutID(int urutID) {
        UrutID = urutID;
    }

    public int getQtyBig() {
        return QtyBig;
    }

    public void setQtyBig(int qtyBig) {
        QtyBig = qtyBig;
    }

    public int getQtySmall() {
        return QtySmall;
    }

    public void setQtySmall(int qtySmall) {
        QtySmall = qtySmall;
    }

    public int getSalesPrice() {
        return SalesPrice;
    }

    public void setSalesPrice(int salesPrice) {
        SalesPrice = salesPrice;
    }

    public double getPctDisc1() {
        return PctDisc1;
    }

    public void setPctDisc1(double pctDisc1) {
        PctDisc1 = pctDisc1;
    }

    public double getPctDisc2() {
        return PctDisc2;
    }

    public void setPctDisc2(double pctDisc2) {
        PctDisc2 = pctDisc2;
    }

    public double getPctDisc3() {
        return PctDisc3;
    }

    public void setPctDisc3(double pctDisc3) {
        PctDisc3 = pctDisc3;
    }

    public int getbConfirm() {
        return bConfirm;
    }

    public void setbConfirm(int bConfirm) {
        this.bConfirm = bConfirm;
    }

    public int getbTransfer() {
        return bTransfer;
    }

    public void setbTransfer(int bTransfer) {
        this.bTransfer = bTransfer;
    }
}
