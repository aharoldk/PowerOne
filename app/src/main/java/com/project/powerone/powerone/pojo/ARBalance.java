package com.project.powerone.powerone.pojo;

/**
 * Created by aharoldk on 02/08/17.
 */

public class ARBalance {

    private String SiteID, SalesmanID, CustID, InvoiceID, dDueDate;
    private long BalanceAR;
    private int UrutID;

    public ARBalance() {
    }

    public ARBalance(String siteID, String salesmanID, String custID, String invoiceID, String dDueDate, long balanceAR, int urutID) {
        SiteID = siteID;
        SalesmanID = salesmanID;
        CustID = custID;
        InvoiceID = invoiceID;
        this.dDueDate = dDueDate;
        BalanceAR = balanceAR;
        UrutID = urutID;
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

    public String getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        InvoiceID = invoiceID;
    }

    public String getdDueDate() {
        return dDueDate;
    }

    public void setdDueDate(String dDueDate) {
        this.dDueDate = dDueDate;
    }

    public long getBalanceAR() {
        return BalanceAR;
    }

    public void setBalanceAR(long balanceAR) {
        BalanceAR = balanceAR;
    }

    public int getUrutID() {
        return UrutID;
    }

    public void setUrutID(int urutID) {
        UrutID = urutID;
    }
}
