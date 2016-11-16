/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        DBOpenHelper ���ݿ�����ļ�  
**------------------------------------------------------------------------------------------------------
** Created by:          yanbo 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 17;// �������ݿ�汾��
    private static final String DBNAME = "vmc.db";// �������ݿ���

    public DBOpenHelper(Context context) {// ���幹�캯��

        super(context, DBNAME, null, VERSION);// ��д����Ĺ��캯��
    }

    @Override
    public void onCreate(SQLiteDatabase db) {// �������ݿ�
    	// vmc_system_parameterϵͳ������
        db.execSQL
        (
        		"CREATE TABLE vmc_system_parameter" +
        		"("+
        		"devID nvarchar(100) PRIMARY KEY,"+
                "devhCode nvarchar(50),"+ 
        		"isNet INT NOT NULL DEFAULT 0," +
        		"isfenClass INT NOT NULL DEFAULT 0," +
        		"isbuyCar INT NOT NULL DEFAULT 0," +
        		"liebiaoKuan INT NOT NULL DEFAULT 0, " +
        		"mainPwd nvarchar(50)," +
        		"amount INT NOT NULL DEFAULT 0," +
        		"card INT NOT NULL DEFAULT 0," +
        		"zhifubaofaca INT NOT NULL DEFAULT 0," +
        		"zhifubaoer INT NOT NULL DEFAULT 0," +
        		"weixing INT NOT NULL DEFAULT 0," +
        		"printer INT NOT NULL DEFAULT 0," +
        		"language INT NOT NULL DEFAULT 0," +
        		"rstTime DATETIME," +
        		"rstDay int," +
        		"baozhiProduct INT NOT NULL DEFAULT 0," +
        		"emptyProduct INT NOT NULL DEFAULT 0," +
        		"proSortType INT NOT NULL DEFAULT 6," +
        		"marketAmount Decimal(8,2)," +
        		"billAmount Decimal(8,2)," +
        		"event nvarchar(500)," +
        		"demo nvarchar(500)"+
        		")"
        		
        );
        //vmc_machineset �������ñ�
        db.execSQL
        (
        		"CREATE TABLE vmc_machineset" +
        		"("+
        		"machID INT	AUTO_INCREMENT PRIMARY KEY,"+
                "logoStr nvarchar(50),"+ 
        		"audioWork INT NOT NULL DEFAULT 0," +
        		"audioWorkstart DATETIME," +
        		"audioWorkend DATETIME," +
        		"audioSun INT NOT NULL DEFAULT 0," +
        		"audioSunstart DATETIME," +
        		"audioSunend DATETIME," +
        		"tempWork INT NOT NULL DEFAULT 0," +
        		"tempWorkstart DATETIME," +
        		"tempWorkend DATETIME," +
        		"tempSunstart DATETIME," +
        		"tempSunend DATETIME," +
        		"ligntWorkstart DATETIME," +
        		"ligntWorkend DATETIME," +
        		"ligntSunstart DATETIME," +
        		"ligntSunend DATETIME," +
        		"coldWorkstart DATETIME," +
        		"coldWorkend DATETIME," +
        		"coldSunstart DATETIME," +
        		"coldSunend DATETIME," +
        		"chouWorkstart DATETIME," +
        		"chouWorkend DATETIME," +
        		"chouSunstart DATETIME," +
        		"chouSunend DATETIME" +
        		")"
        		
        );
        //vmc_class ��Ʒ����
        db.execSQL
        (
        		"CREATE TABLE vmc_class" +
        		"("+
        		"classID nvarchar(50) PRIMARY KEY,"+
                "className nvarchar(200),"+  
                "attBatch1 nvarchar(50),"+
        		"classTime DATETIME NOT NULL" +
        		")"
        		
        );
        //vmc_classproduct  ��Ʒ����������
        db.execSQL
        (
        		"CREATE TABLE vmc_classproduct" +
        		"("+
        		"classID nvarchar(50),"+
                "productID nvarchar(200),"+         		     		
        		"classTime DATETIME NOT NULL" +
        		")"
        		
        );
        //vmc_product ��Ʒ��
        db.execSQL
        (
        		"CREATE TABLE vmc_product" +
        		"("+
        		"productID nvarchar(200) PRIMARY KEY,"+
                "productName nvarchar(200),"+ 
        		"productDesc nvarchar(500)," +
        		"marketPrice Decimal(8,2)," +
        		"salesPrice Decimal(8,2)," +
        		"shelfLife INT," +
        		"downloadTime DATETIME," +
        		"onloadTime DATETIME," +
        		"attBatch1 nvarchar(50)," +
        		"attBatch2 nvarchar(50)," +
        		"attBatch3 nvarchar(50)," +
        		"paixu INT NOT NULL," +        		
        		"isdelete INT" +
        		")"
        		
        );
        //vmc_cabinet �����ͱ�
        db.execSQL
        (
        		"CREATE TABLE vmc_cabinet" +
        		"("+
        		"cabID nvarchar(100) PRIMARY KEY,"+  
        		"cabType INT NOT NULL DEFAULT 0" +
        		")"
        		
        );
      //vmc_column ������
        db.execSQL
        (
        		"CREATE TABLE vmc_column" +
        		"("+
        		"cabID nvarchar(100)," +
        		"columnID nvarchar(100)," +
        		"productID nvarchar(100)," +
        		"pathCount INT," +
        		"pathRemain INT," +
        		"columnStatus INT NOT NULL DEFAULT 0,"+                     		
        		"lasttime DATETIME NOT NULL,"+
        		"path_id int," +
        		"isupload INT NOT NULL DEFAULT 0,"+
        		"tihuoPwd nvarchar(50)" +
        		")"
        		
        );
      //vmc_order_pay ����֧����
        db.execSQL
        (
        		"CREATE TABLE vmc_order_pay" +
        		"("+
        		"ordereID nvarchar(100) PRIMARY KEY," +
        		"payType INT," +
        		"payStatus INT," +
        		"RealStatus INT," +
        		"smallNote Decimal(8,2)," +
        		"smallConi Decimal(8,2)," +
        		"smallAmount Decimal(8,2)," +
        		"smallCard Decimal(8,2)," +
        		"shouldPay Decimal(8,2)," +
        		"shouldNo INT," +
        		"realNote Decimal(8,2)," +
        		"realCoin Decimal(8,2)," +
        		"realAmount Decimal(8,2)," +
        		"debtAmount Decimal(8,2)," +
        		"realCard Decimal(8,2),"+  
        		"rfd_card_no nvarchar(100) ," +
        		"payTime DATETIME NOT NULL," +
        		"isupload INT NOT NULL DEFAULT 0" +
        		")"
        		
        );
      //vmc_order_product ������ϸ��Ϣ��
        db.execSQL
        (
        		"CREATE TABLE vmc_order_product" +
        		"("+
        		"orderID nvarchar(100)," +
        		"productID nvarchar(100)," +
        		"yujiHuo int," +
        		"realHuo int," +
        		"cabID nvarchar(100)," +
        		"columnID nvarchar(100)," +
        		"huoStatus int" +
        		")"
        		
        );
      //vmc_log ������־��Ϣ��
        db.execSQL
        (
        		"CREATE TABLE vmc_log" +
        		"("+
        		"logID nvarchar(100)PRIMARY KEY," +
        		"logType INT," +
        		"logDesc nvarchar(500)," +
        		"logTime DATETIME NOT NULL" +
        		")"
        		
        );
    }
 // �������ݿ�ʱ����İ汾���뵱ǰ�İ汾�Ų�ͬʱ����ø÷�����
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {    	
//    	// vmc_system_parameterϵͳ������
//    	db.execSQL
//        (
//        		"DROP TABLE IF EXISTS vmc_system_parameter"         		
//        );
//    	// vmc_machineset�������ñ�
//    	db.execSQL
//        (
//        		"DROP TABLE IF EXISTS vmc_machineset"         		
//        );
//    	// vmc_class��Ʒ����
//    	db.execSQL
//        (
//        		"DROP TABLE IF EXISTS vmc_class"         		
//        );
//    	// vmc_classproduct ��Ʒ����������
//    	db.execSQL
//        (
//        		"DROP TABLE IF EXISTS vmc_classproduct"         		
//        );
//    	// vmc_product��Ʒ��
//    	db.execSQL
//        (
//        		"DROP TABLE IF EXISTS vmc_product"         		
//        );
//    	// vmc_cabinet�����ͱ�
//    	db.execSQL
//        (
//        		"DROP TABLE IF EXISTS vmc_cabinet"         		
//        );
//    	// vmc_column������
//    	db.execSQL
//        (
//        		"DROP TABLE IF EXISTS vmc_column"         		
//        );
//    	// vmc_order_pay����֧����
//    	db.execSQL
//        (
//        		"DROP TABLE IF EXISTS vmc_order_pay"         		
//        );
//    	// vmc_order_product������ϸ��Ϣ��
//    	db.execSQL
//        (
//        		"DROP TABLE IF EXISTS vmc_order_product"         		
//        );
//    	// vmc_log������־��Ϣ��
//    	db.execSQL
//        (
//        		"DROP TABLE IF EXISTS vmc_log"         		
//        );
//    	
//        onCreate(db);  
//    	db.execSQL
//        (
//      		"alter table vmc_system_parameter add column event nvarchar(500)"         		
//        );
//    	db.execSQL
//        (
//       		"alter table vmc_system_parameter add column demo nvarchar(500)"         		
//        );
    	db.execSQL
        (
       		"alter table vmc_order_pay add column rfd_card_no nvarchar(100)"         		
        );	
    	
    }
}
