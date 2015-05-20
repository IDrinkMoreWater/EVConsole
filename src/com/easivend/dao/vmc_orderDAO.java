/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        vmc_orderDAO ����������ļ�  
**------------------------------------------------------------------------------------------------------
** Created by:          yanbo 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.easivend.model.Tb_vmc_order_pay;
import com.easivend.model.Tb_vmc_order_product;
import com.easivend.model.Tb_vmc_product;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class vmc_orderDAO 
{
	private DBOpenHelper helper;// ����DBOpenHelper����
    private SQLiteDatabase db;// ����SQLiteDatabase����
    //SimpleDateFormat sdf = new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
    // ���幹�캯��
	public vmc_orderDAO(Context context) 
	{
		helper=new DBOpenHelper(context);// ��ʼ��DBOpenHelper����
	}
	
	//���
	public void add(Tb_vmc_order_pay tb_vmc_order_pay,Tb_vmc_order_product tb_vmc_order_product)throws SQLException
	{
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		
        // ִ����Ӷ���֧����	
 		db.execSQL(
 				"insert into vmc_order_pay" +
 				"(" +
 				"ordereID,payType,payStatus,RealStatus,smallNote,smallConi,smallAmount," +
 				"smallCard,shouldPay,shouldNo,realNote,realCoin,realAmount,debtAmount,realCard,payTime" +
 				") " +
 				"values" +
 				"(" +
 				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,(datetime('now', 'localtime'))" +
 				")",
 		        new Object[] { tb_vmc_order_pay.getOrdereID(), tb_vmc_order_pay.getPayType(),tb_vmc_order_pay.getPayStatus(), tb_vmc_order_pay.getRealStatus(),
 						tb_vmc_order_pay.getSmallNote(), tb_vmc_order_pay.getSmallConi(),tb_vmc_order_pay.getSmallAmount(), tb_vmc_order_pay.getSmallCard(),
 						tb_vmc_order_pay.getShouldPay(), tb_vmc_order_pay.getShouldNo(), tb_vmc_order_pay.getRealNote(), tb_vmc_order_pay.getRealCoin(), 
 						tb_vmc_order_pay.getRealAmount(), tb_vmc_order_pay.getDebtAmount(), tb_vmc_order_pay.getRealCard()});
 		
		// ִ����Ӷ�����ϸ��Ϣ��	
 		db.execSQL(
 				"insert into vmc_order_product" +
 				"(" +
 				"orderID,productID,yujiHuo,realHuo,cabID,columnID,huoStatus" +
 				") " +
 				"values" +
 				"(" +
 				"?,?,?,?,?,?,?" +
 				")",
 		        new Object[] { tb_vmc_order_product.getOrderID(), tb_vmc_order_product.getProductID(),tb_vmc_order_product.getYujiHuo(), tb_vmc_order_product.getRealHuo(),
 						tb_vmc_order_product.getCabID(), tb_vmc_order_product.getColumnID(),tb_vmc_order_product.getHuoStatus()});
 	 	db.close(); 
	}
	//ɾ��ʱ�䷶Χ�ڵ�����
	public void detele(String starttime, String endtime) 
	{   
		List<String> alllist=new ArrayList<String>(); 
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        //ȡ��ʱ�䷶Χ�ڶ���֧������
		Cursor cursor = db.rawQuery("select ordereID FROM [vmc_order_pay] where payTime between ? and ?", 
				new String[] { starttime,endtime });// ��ȡ������Ϣ���е������
		while (cursor.moveToNext()) 
        {
			alllist.add(cursor.getString(cursor.getColumnIndex("ordereID")));
        }
		
		for(int i=0;i<alllist.size();i++) 
		{	
			// ִ��ɾ��������ϸ��Ϣ��	
	 		db.execSQL(
	 				"delete from vmc_order_product where orderID=?",
	 		        new Object[] { alllist.get(i)});
	 		// ִ����Ӷ���֧����	
	 		db.execSQL(
	 				"delete from vmc_order_pay where ordereID=?",
	 		        new Object[] { alllist.get(i)});
		}
		if (!cursor.isClosed()) 
 		{  
 			cursor.close();  
 		}  
        db.close();
    }
	//����ʱ�䷶Χ�ڵĶ���֧������
	public List<Tb_vmc_order_pay> getScrollPay(String starttime, String endtime) 
	{   
		List<Tb_vmc_order_pay> tb_inaccount = new ArrayList<Tb_vmc_order_pay>();// �������϶���
             
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        //ȡ��ʱ�䷶Χ�ڶ���֧������
		Cursor cursor = db.rawQuery("select ordereID,payType,payStatus,RealStatus,smallNote,smallConi,smallAmount," +
				"smallCard,shouldPay,shouldNo,realNote,realCoin,realAmount,debtAmount,realCard,payTime " +
				" FROM [vmc_order_pay] where payTime between ? and ?", 
				new String[] { starttime,endtime });// ��ȡ������Ϣ���е������
		while (cursor.moveToNext()) 
        {
			// ����������������Ϣ��ӵ�������
            tb_inaccount.add(new Tb_vmc_order_pay
        		(
        				cursor.getString(cursor.getColumnIndex("ordereID")), cursor.getInt(cursor.getColumnIndex("payType")),
        				cursor.getInt(cursor.getColumnIndex("payStatus")),cursor.getInt(cursor.getColumnIndex("RealStatus")),
        				cursor.getFloat(cursor.getColumnIndex("smallNote")),cursor.getFloat(cursor.getColumnIndex("smallConi")),
        				cursor.getFloat(cursor.getColumnIndex("smallAmount")),cursor.getFloat(cursor.getColumnIndex("smallCard")),
        				cursor.getFloat(cursor.getColumnIndex("shouldPay")), cursor.getInt(cursor.getColumnIndex("shouldNo")),
        				cursor.getFloat(cursor.getColumnIndex("realNote")),cursor.getFloat(cursor.getColumnIndex("realCoin")),
        				cursor.getFloat(cursor.getColumnIndex("realAmount")),cursor.getFloat(cursor.getColumnIndex("debtAmount")),
        				cursor.getFloat(cursor.getColumnIndex("realCard")),cursor.getString(cursor.getColumnIndex("payTime"))
        		)
           );
        }
				
		if (!cursor.isClosed()) 
 		{  
 			cursor.close();  
 		}  
        db.close();
        return tb_inaccount;// ���ؼ���
    }
	//����ʱ�䷶Χ�ڵĶ�����ϸ��Ϣ����
	public List<Tb_vmc_order_product> getScrollProduct(String starttime, String endtime) 
	{   
		List<Tb_vmc_order_product> tb_inaccount = new ArrayList<Tb_vmc_order_product>();// �������϶���
             
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        //ȡ��ʱ�䷶Χ�ڶ���֧������
		Cursor cursor = db.rawQuery("select orderID,productID,yujiHuo,realHuo,cabID,columnID,huoStatus " +
				" FROM [vmc_order_product] where payTime between ? and ?", 
				new String[] { starttime,endtime });// ��ȡ������Ϣ���е������
		while (cursor.moveToNext()) 
        {
			// ����������������Ϣ��ӵ�������
            tb_inaccount.add(new Tb_vmc_order_product
        		(
        				cursor.getString(cursor.getColumnIndex("orderID")), cursor.getString(cursor.getColumnIndex("productID")),
        				cursor.getInt(cursor.getColumnIndex("yujiHuo")),cursor.getInt(cursor.getColumnIndex("realHuo")),
        				cursor.getString(cursor.getColumnIndex("cabID")),cursor.getString(cursor.getColumnIndex("columnID")),
        				cursor.getInt(cursor.getColumnIndex("huoStatus"))
        		)
           );
        }
				
		if (!cursor.isClosed()) 
 		{  
 			cursor.close();  
 		}  
        db.close();
        return tb_inaccount;// ���ؼ���
    }
}
