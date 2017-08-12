package com.project.powerone.powerone.pojo;

/**
 * Created by aharoldk on 11/08/17.
 */

public class ARPayment {
    private int UrutID;
    private String SiteID;
    private String SalesmanID;
    private String CustID;
    private String InvoiceID;
    private int NominalPayment;
    private String PaymentType;
    private String BillyetNo;
    private String BillyetDueDate;
    private int bConfirm;
    private int bTransfer;

    public ARPayment(int urutID, String siteID, String salesmanID, String custID, String invoiceID, int nominalPayment, String paymentType, String billyetNo, String billyetDueDate, int bConfirm, int bTransfer) {
        UrutID = urutID;
        SiteID = siteID;
        SalesmanID = salesmanID;
        CustID = custID;
        InvoiceID = invoiceID;
        NominalPayment = nominalPayment;
        PaymentType = paymentType;
        BillyetNo = billyetNo;
        BillyetDueDate = billyetDueDate;
        this.bConfirm = bConfirm;
        this.bTransfer = bTransfer;
    }

    public int getUrutID() {
        return UrutID;
    }

    public void setUrutID(int urutID) {
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

    public int getNominalPayment() {
        return NominalPayment;
    }

    public void setNominalPayment(int nominalPayment) {
        NominalPayment = nominalPayment;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getBillyetNo() {
        return BillyetNo;
    }

    public void setBillyetNo(String billyetNo) {
        BillyetNo = billyetNo;
    }

    public String getBillyetDueDate() {
        return BillyetDueDate;
    }

    public void setBillyetDueDate(String billyetDueDate) {
        BillyetDueDate = billyetDueDate;
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
