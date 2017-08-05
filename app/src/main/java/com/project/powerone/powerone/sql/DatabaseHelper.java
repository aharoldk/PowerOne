package com.project.powerone.powerone.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.powerone.powerone.OrderProductActivity;
import com.project.powerone.powerone.pojo.ARBalance;
import com.project.powerone.powerone.pojo.Customer;
import com.project.powerone.powerone.pojo.Order;
import com.project.powerone.powerone.pojo.OrderProduct;
import com.project.powerone.powerone.pojo.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aharoldk on 21/07/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase sqLiteDatabase;

    private String status = "Active";

    // Database Name
    private static final String DATABASE_NAME = "dbo.db";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Table
    private static final String TABLE_NAME1 = "MobCustomer";
    private static final String TABLE_NAME2 = "MobProduct";
    private static final String TABLE_NAME3 = "MobProductPrice";
    private static final String TABLE_NAME4 = "MobARBalance";
    private static final String TABLE_NAME5 = "MobSalesOrder";
    private static final String TABLE_NAME6 = "MobARPayment";
    private static final String TABLE_NAME7 = "MobSite";
    private static final String TABLE_NAME8 = "MobSalesman";

//    table1
    private static final String ID = "UrutID";
    private static final String SITE_ID = "SiteID";

    private static final String SALESMAN_ID = "SalesmanID";
    private static final String CUST_ID = "CustID";
    private static final String CUST_NAME = "CustName";
    private static final String CUST_ADDRESS = "CustAddress";
    private static final String PRICE_TYPE = "PriceType";
    private static final String GEO_MAPLONG = "GeoMapLong";
    private static final String GEO_MAPLAT = "GeoMapLat";
    private static final String GPS_MAPLONG = "GPSMapLong";
    private static final String GPS_MAPLAT = "GPSMapLat";
    private static final String STATUS_CUSTOMER = "StatusCustomer";
    private static final String DATE_TIME = "DateTime";

//    table 2 menggunakan ID dan SITE_ID
    private static final String PRODUCT_ID = "ProductID";
    private static final String PRODUCT_NAME = "ProductName";
    private static final String BIG_PACK = "BigPack";
    private static final String SMALL_PACK = "SmallPack";
    private static final String NO_OF_PACK = "NoOfPack";
    private static final String PRINSIPAL_NAME = "PrinsipalName";
    private static final String GROUP_PRODUCT_NAME = "GroupProductName";
    private static final String SUB_GROUP_PRODUCT_NAME = "SubGroupProductName";
    private static final String QTY_ON_HAND = "QtyOnHand";


//    table3 menggunakan ID, SITE_ID, dan PRODUCT_ID
    private static final String PRODUCT_TYPE = "ProductType";
    private static final String SALES_PRICE = "SalesPrice";

//    table4 menggunakan ID, SITE_ID, SALESMAN_ID, dan CUST_ID
    private static final String INVOICE_ID = "InvoiceID";
    private static final String DDUE_DATE = "dDueDate";
    private static final String BALANCE_AR = "BalanceAR";

//    table5, menggunakan ID, SITE_ID, SALESMAN_ID, CUST_ID, PRODUCT_ID after QTY_SMALL -> SALES_PRICE
    private static final String QTY_BIG = "QtyBig";
    private static final String QTY_SMALL = "QtySmall";
    private static final String PCT_DISC1 = "PctDisc1";
    private static final String PCT_DISC2 = "PctDisc2";
    private static final String PCT_DISC3 = "PctDisc3";
    private static final String BCONFIRM = "bConfirm";
    private static final String BTRANSFER = "bTransfer";

//    table6, menggunakan ID, SITE_ID, SALESMAN_ID, CUST_ID, INVOICE_ID
    private static final String NOMINAL_PAYMENT = "NominalPayment";
    private static final String PAYMENT_TYPE = "PaymenType";
    private static final String BILL_YET_NO = "BillyetNo";
    private static final String BILL_YET_DUE_DATE = "BillyetDueDate";

//    table7, menggunakan SITE_ID
    private static final String SITE_NAME ="SiteName";

//    table8, menggunakan SALESMAN_ID, SITE_ID setelah SALESMAN_NAME
    private static final String SALESMAN_NAME = "SalesmanName";
    private static final String PASSWORD = "Password";
    private static final String DLAST_LOGIN = "dLastLogin";

    private static final String QUERY_TABLE1 = "CREATE TABLE "+TABLE_NAME1+"("+ID+" INTEGER PRIMARY KEY NOT NULL,"+SITE_ID+" CHARACTER(10) NULL,"+SALESMAN_ID+" CHARACTER(20) NULL,"+CUST_ID+" CHARACTER(10) NULL,"+CUST_NAME+" VARCHAR(100) NULL, "+CUST_ADDRESS+" VARCHAR(250) NULL, "+PRICE_TYPE+" CHARACTER(10) NULL, "+GEO_MAPLONG+" DOUBLE NULL, "+GEO_MAPLAT+" DOUBLE NULL, "+GPS_MAPLONG+" DOUBLE NULL, "+GPS_MAPLAT+" DOUBLE NULL, "+STATUS_CUSTOMER+" CHARACTER(12) NULL , "+DATE_TIME+" VARCHAR(20) NULL)";

    private static final String QUERY_TABLE2 = "CREATE TABLE "+TABLE_NAME2+"("+ID+" INTEGER PRIMARY KEY NOT NULL,"+SITE_ID+" CHARACTER(10) NULL,"+PRODUCT_ID+" CHARACTER(20) NULL, "+PRODUCT_NAME+" VARCHAR(100) NULL, "+BIG_PACK+" CHARACTER(10) NULL, "+SMALL_PACK+" CHARACTER(10) NULL, "+NO_OF_PACK+" INT  NULL, "+PRINSIPAL_NAME+" VARCHAR(50) NULL, "+GROUP_PRODUCT_NAME+" VARCHAR(50) NULL, "+SUB_GROUP_PRODUCT_NAME+" VARCHAR(50) NULL, "+QTY_ON_HAND+" INT NULL)";

    private static final String QUERY_TABLE3 = "CREATE TABLE "+TABLE_NAME3+"("+ID+" INTEGER PRIMARY KEY NOT NULL, "+SITE_ID+" CHARACTER(10) NULL, "+PRODUCT_ID+" CHARACTER(20) NULL, "+PRODUCT_TYPE+" CHARACTER(10) NULL, "+SALES_PRICE+" INT NULL)";

    private static final String QUERY_TABLE4 = "CREATE TABLE "+TABLE_NAME4+"("+ID+" INTEGER PRIMARY KEY NOT NULL, "+SITE_ID+" CHARACTER(10) NULL, "+SALESMAN_ID+" CHARACTER(20) NULL, "+CUST_ID+" CHARACTER(10) NULL, "+INVOICE_ID+" VARCHAR(20) NULL, "+DDUE_DATE+" DATETIME NULL, "+BALANCE_AR+" INTEGER NOT NULL)";

    private static final String QUERY_TABLE5 = "CREATE TABLE "+TABLE_NAME5+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+SITE_ID+" CHARACTER(10) NULL, "+SALESMAN_ID+" CHARACTER(20) NULL, "+CUST_ID+" CHARACTER(10) NULL, "+PRODUCT_ID+" CHARACTER(20), "+QTY_BIG+" INT NULL, "+QTY_SMALL+" INT NULL, "+SALES_PRICE+" INT NULL , "+PCT_DISC1+" DOUBLE NULL, "+PCT_DISC2+" DOUBLE NULL, "+PCT_DISC3+" DOUBLE NULL, "+BCONFIRM+" INT NULL, "+BTRANSFER+" INT NULL)";

    private static final String QUERY_TABLE6 = "CREATE TABLE "+TABLE_NAME6+"("+ID+" INTEGER PRIMARY KEY NOT NULL, "+SITE_ID+" CHARACTER(10) NULL, "+SALESMAN_ID+" CHARACTER(20) NULL, "+CUST_ID+" CHARACTER(10) NULL, "+INVOICE_ID+" CHARACTER(20) NULL, "+NOMINAL_PAYMENT+" NUMERIC(18, 2) NULL, "+PAYMENT_TYPE+" CHARACTER(1) NULL, "+BILL_YET_NO+" VARCHAR(20) NULL, "+BILL_YET_DUE_DATE+" DATETIME NULL )";

    private static final String QUERY_TABLE7 = "CREATE TABLE "+TABLE_NAME7+"("+SITE_ID+" CHARACTER(10) NOT NULL, "+SITE_NAME+" VARCHAR(50) NULL)";

    private static final String QUERY_TABLE8 = "CREATE TABLE "+TABLE_NAME8+"("+SALESMAN_ID+" CHARACTER(20) NOT NULL, "+SALESMAN_NAME+" VARCHAR(50) NULL, "+SITE_ID+" CHARACTER(10) NOT NULL, "+PASSWORD+" VARCHAR(50) NULL, "+DLAST_LOGIN+" DATETIME NULL)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(QUERY_TABLE1);
        sqLiteDatabase.execSQL(QUERY_TABLE2);
        sqLiteDatabase.execSQL(QUERY_TABLE3);
        sqLiteDatabase.execSQL(QUERY_TABLE4);
        sqLiteDatabase.execSQL(QUERY_TABLE5);
        sqLiteDatabase.execSQL(QUERY_TABLE6);
        sqLiteDatabase.execSQL(QUERY_TABLE7);
        sqLiteDatabase.execSQL(QUERY_TABLE8);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QUERY_TABLE1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QUERY_TABLE2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QUERY_TABLE3);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QUERY_TABLE4);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QUERY_TABLE5);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QUERY_TABLE6);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QUERY_TABLE7);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QUERY_TABLE8);

        onCreate(sqLiteDatabase);
    }

    public void emptyDatabase(String table) {
        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        sqLiteDatabase.delete(table, null, null);
    }

    public Boolean insertSalesman(String salesmanID, String salesmanName, String siteID, String password, String dlastlogin){
        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(SALESMAN_ID, salesmanID);
        contentValues.put(SALESMAN_NAME, salesmanName);
        contentValues.put(SITE_ID, siteID);
        contentValues.put(PASSWORD, password);
        contentValues.put(DLAST_LOGIN, dlastlogin);

        long result = sqLiteDatabase.insert(TABLE_NAME8, null, contentValues);

        if(result == -1){
            return false;

        } else {
            return true;

        }
    }

    public Boolean insertSalesOrder(String siteID, String salesmanID, String custID, String productID, int sBig, int sSmall, int salesPrice, int sDisc1, int sDisc2, int sDisc3, int bConfirm, int bTransfer){
        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(SITE_ID, siteID);
        contentValues.put(SALESMAN_ID, salesmanID);
        contentValues.put(CUST_ID, custID);
        contentValues.put(PRODUCT_ID, productID);
        contentValues.put(QTY_BIG, sBig);
        contentValues.put(QTY_SMALL, sSmall);
        contentValues.put(SALES_PRICE, salesPrice);
        contentValues.put(PCT_DISC1, sDisc1);
        contentValues.put(PCT_DISC2, sDisc2);
        contentValues.put(PCT_DISC3, sDisc3);
        contentValues.put(BCONFIRM, bConfirm);
        contentValues.put(BTRANSFER, bTransfer);

        long result = sqLiteDatabase.insert(TABLE_NAME5, null, contentValues);

        if(result == -1){
            return false;

        } else {
            return true;

        }
    }

    public Cursor loginSalesman(){
        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ TABLE_NAME8, null);

        return cursor;
    }

    public boolean insertCustomer(int urutID, String siteID, String salesmanID, String custID, String custName, String custAddress, String priceType, double geoMapLong, double geoMapLat, double gpsMapLong, double gpsMapLat, String statusCustomer, String dateTime) {

        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ID, urutID);
        contentValues.put(SITE_ID, siteID);
        contentValues.put(SALESMAN_ID, salesmanID);
        contentValues.put(CUST_ID, custID);
        contentValues.put(CUST_NAME, custName);
        contentValues.put(CUST_ADDRESS, custAddress);
        contentValues.put(PRICE_TYPE, priceType);
        contentValues.put(GEO_MAPLONG,  geoMapLong);
        contentValues.put(GEO_MAPLAT, geoMapLat);
        contentValues.put(GPS_MAPLONG, gpsMapLong);
        contentValues.put(GPS_MAPLAT, gpsMapLat);
        contentValues.put(STATUS_CUSTOMER, statusCustomer);
        contentValues.put(DATE_TIME, dateTime);

        long result = sqLiteDatabase.insert(TABLE_NAME1, null, contentValues);

        if(result == -1){
            return false;

        } else {
            return true;

        }
    }

    public List<Order> getAllOrder(String condition){
        List<Order> orders = new ArrayList<>();

        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME5, null, " "+CUST_ID+" =?", new String[] {condition}, null, null, null);

        while (cursor.moveToNext()) {

            int urutID = cursor.getInt(0);
            String siteID = cursor.getString(1);
            String salesmanID = cursor.getString(2);
            String custID = cursor.getString(3);
            String productID = cursor.getString(4);
            int qtyBig = cursor.getInt(5);
            int qtySmall = cursor.getInt(6);
            int salesPrice = cursor.getInt(7);
            double disc1 = cursor.getDouble(8);
            double disc2 = cursor.getDouble(9);
            double disc3 = cursor.getDouble(10);
            int bConfirm = cursor.getInt(11);
            int bTransfer = cursor.getInt(12);

            Order order = new Order(siteID, salesmanID, custID, productID, urutID, qtyBig, qtySmall, salesPrice, disc1, disc2, disc3, bConfirm, bTransfer);

            orders.add(order);
        }

        return orders;
    }

    public List<ARBalance> getAllArBalance(){
        List<ARBalance> arBalances = new ArrayList<>();

        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME4, null);

        while (cursor.moveToNext()){

            int urutID = cursor.getInt(0);
            String siteID = cursor.getString(1);
            String salesmanID = cursor.getString(2);
            String custID = cursor.getString(3);
            String invoiceID = cursor.getString(4);
            String dDueDate = cursor.getString(5);
            long balanceAR = cursor.getLong(6);

            ARBalance arBalance = new ARBalance(siteID, salesmanID, custID, invoiceID, dDueDate, balanceAR, urutID);
            arBalances.add(arBalance);
        }

        return arBalances;
    }

    public Cursor getCustomer(){
        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ TABLE_NAME1+" WHERE "+STATUS_CUSTOMER+" = ? ORDER BY datetime("+DATE_TIME+") DESC Limit 1", new String[] {status});

        return cursor;
    }

    public List<Customer> getAllCustomer(){
        List<Customer> listCustomer = new ArrayList<>();

        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME1+ " ORDER BY "+STATUS_CUSTOMER+" DESC, "+CUST_NAME+" ASC", null);

        while (cursor.moveToNext()){

            int urutID = cursor.getInt(0);
            String siteID = cursor.getString(1);
            String salesmanID = cursor.getString(2);
            String custID = cursor.getString(3);
            String custName = cursor.getString(4);
            String custAddress = cursor.getString(5);
            String priceType = cursor.getString(6);
            double geoMapLong = cursor.getDouble(7);
            double geoMapLat = cursor.getDouble(8);
            double gpsMapLong = cursor.getDouble(9);
            double gpsMapLat = cursor.getDouble(10);
            String statusCustomer = cursor.getString(11);
            String dateTime = cursor.getString(12);

            Customer customer = new Customer(siteID, salesmanID, custID, custName, custAddress, priceType, statusCustomer, dateTime, urutID, geoMapLong, geoMapLat, gpsMapLong, gpsMapLat);

            listCustomer.add(customer);
        }

        return listCustomer;
    }

    public List<OrderProduct> getAllProductPrice(String condition) {
        List<OrderProduct> list = new ArrayList<>();

        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT a.*, c.SiteID, c.ProductID, c.ProductType, c.SalesPrice FROM "+TABLE_NAME2+" as a, "+TABLE_NAME3+" as c WHERE a.SiteID = c.SiteID AND a.ProductID = c.ProductID AND c.ProductType = ?", new String[]{condition});

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String siteID = cursor.getString(1);
            String productID = cursor.getString(2);
            String productName = cursor.getString(3);
            String bigPack = cursor.getString(4);
            String smallPack = cursor.getString(5);
            int noOfPack = cursor.getInt(6);
            String prinsipalName = cursor.getString(7);
            String groupProductName = cursor.getString(8);
            String subgroupProductName = cursor.getString(9);
            int qtyOnHand = cursor.getInt(10);
            String productType = cursor.getString(13);
            int salesPrice = cursor.getInt(14);


            OrderProduct orderProduct = new OrderProduct(siteID, productID, productName, bigPack, smallPack, prinsipalName, groupProductName, subgroupProductName, noOfPack, id, qtyOnHand, productType, salesPrice);

            list.add(orderProduct);
        }


        return list;
    }


    public List<Product> getAllProduct(){
        List<Product> list = new ArrayList<>();

        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME2, null);

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String siteID = cursor.getString(1);
            String productID = cursor.getString(2);
            String productName = cursor.getString(3);
            String bigPack = cursor.getString(4);
            String smallPack = cursor.getString(5);
            int noOfPack = cursor.getInt(6);
            String prinsipalName = cursor.getString(7);
            String groupProductName = cursor.getString(8);
            String subgroupProductName = cursor.getString(9);
            int qtyOnHand = cursor.getInt(10);

            Product product = new Product(siteID, productID, productName, bigPack, smallPack, prinsipalName, groupProductName, subgroupProductName, noOfPack, id, qtyOnHand);

            list.add(product);
        }

        return list;

    }

    public boolean insertProduct(int urutID, String siteID, String productID, String productName, String bigPack, String smallPack, String prinsipalName, String groupProductName, String subGroupProductName, int noOfPack, int qtyOnHand) {
        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ID, urutID);
        contentValues.put(SITE_ID, siteID);
        contentValues.put(PRODUCT_ID, productID);
        contentValues.put(PRODUCT_NAME, productName);
        contentValues.put(BIG_PACK, bigPack);
        contentValues.put(SMALL_PACK, smallPack);
        contentValues.put(PRINSIPAL_NAME, prinsipalName);
        contentValues.put(GROUP_PRODUCT_NAME, groupProductName);
        contentValues.put(SUB_GROUP_PRODUCT_NAME, subGroupProductName);
        contentValues.put(NO_OF_PACK, noOfPack);
        contentValues.put(QTY_ON_HAND, qtyOnHand);

        long result = sqLiteDatabase.insert(TABLE_NAME2, null, contentValues);

        if(result == -1){
            return false;

        } else {
            return true;

        }
    }

    public boolean insertPrice(int urutID, String siteID, String productID, String productType, int salesPrice) {
        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ID, urutID);
        contentValues.put(SITE_ID, siteID);
        contentValues.put(PRODUCT_ID, productID);
        contentValues.put(PRODUCT_TYPE, productType);
        contentValues.put(SALES_PRICE, salesPrice);

        long result = sqLiteDatabase.insert(TABLE_NAME3, null, contentValues);

        if(result == -1){
            return false;

        } else {
            return true;

        }

    }

    public Cursor getPrice(String condition){
        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME3+" WHERE "+PRODUCT_ID+" = ?", new String[] {condition});

        return cursor;

    }

    public boolean insertAR(String siteID, String salesmanID,String custID, String invoiceID, String dDueDate, long balanceAR, int urutID) {

        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ID, urutID);
        contentValues.put(SITE_ID, siteID);
        contentValues.put(SALESMAN_ID, salesmanID);
        contentValues.put(CUST_ID, custID);
        contentValues.put(INVOICE_ID, invoiceID);
        contentValues.put(DDUE_DATE, dDueDate);
        contentValues.put(BALANCE_AR, balanceAR);

        long result = sqLiteDatabase.insert(TABLE_NAME4, null, contentValues);

        if(result == -1){
            return false;

        } else {
            return true;

        }
    }

    public boolean updateCustomerVisit(String custID, double gpsMapLong, double gpsMapLat, String statusCustomer, String dateTime){
        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(GPS_MAPLONG, gpsMapLong);
        contentValues.put(GPS_MAPLAT, gpsMapLat);
        contentValues.put(STATUS_CUSTOMER, statusCustomer);
        contentValues.put(DATE_TIME, dateTime);

        long result = sqLiteDatabase.update(TABLE_NAME1, contentValues, CUST_ID+" = ?", new String[] { custID});

        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    public Cursor getArCustomer(String condition) {
        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME4+" WHERE "+CUST_ID+" = ?", new String[] {condition});

        return cursor;
    }

    public Cursor getProduct(String condition) {
        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME2+" WHERE "+PRODUCT_ID+" = ?", new String[] {condition});

        return cursor;
    }

    public int deleteOrder(String urutID) {
        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME5, ID+" = ?", new String[] {urutID});
    }

    public boolean updatePassword(String condition, String newPassword){
        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(PASSWORD, newPassword);

        long result = sqLiteDatabase.update(TABLE_NAME8, contentValues, SALESMAN_ID+" = ?", new String[] { condition});

        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean updateLogin(String dbUserid, String dateTime) {
        sqLiteDatabase = DatabaseHelper.this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DLAST_LOGIN, dateTime);

        long result = sqLiteDatabase.update(TABLE_NAME8, contentValues, SALESMAN_ID+" = ?", new String[] { dbUserid});

        if(result == -1){
            return false;
        } else {
            return true;
        }
    }
}
