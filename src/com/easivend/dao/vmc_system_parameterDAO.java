/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           vmc_system_parameterDAO.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        vmc_system_parameterDAO ϵͳ����������ļ�  
**------------------------------------------------------------------------------------------------------
** Created by:          yanbo 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.easivend.common.ToolClass;
import com.easivend.model.Tb_vmc_system_parameter;

public class vmc_system_parameterDAO
{
	private DBOpenHelper helper;// ����DBOpenHelper����
    private SQLiteDatabase db;// ����SQLiteDatabase����
    //SimpleDateFormat sdf = new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
    // ���幹�캯��
	public vmc_system_parameterDAO(Context context) 
	{
		helper=new DBOpenHelper(context);// ��ʼ��DBOpenHelper����
	}
	//��ӻ��޸�
	public void add(Tb_vmc_system_parameter tb_vmc_system_parameter)throws SQLException
	{
		int max=0;
		//ȡ������ֵ
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        Cursor cursor = db.rawQuery("select count(devID) from vmc_system_parameter", null);// ��ȡ������Ϣ�ļ�¼��
        if (cursor.moveToNext()) {// �ж�Cursor���Ƿ�������

            max=cursor.getInt(0);// �����ܼ�¼��
        }
     
        // ����һ������
	    db.beginTransaction();
	    try {
	        if(max==0)
	        {
	        // ִ�������Ʒ		
	 		db.execSQL(
	 				"insert into vmc_system_parameter" +
	 				"(" +
	 				"devID,devhCode,isNet,isfenClass,isbuyCar," +
	 				"liebiaoKuan,mainPwd,amount,card,zhifubaofaca,zhifubaoer,weixing,printer," +
	 				"language,rstTime,rstDay,baozhiProduct,emptyProduct,proSortType,marketAmount,billAmount" +
	 				") " +
	 				"values" +
	 				"(" +
	 				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
	 				")",
	 		        new Object[] { tb_vmc_system_parameter.getDevID(), tb_vmc_system_parameter.getDevhCode(),tb_vmc_system_parameter.getIsNet(), tb_vmc_system_parameter.getIsfenClass(),
	 						tb_vmc_system_parameter.getIsbuyCar(), tb_vmc_system_parameter.getLiebiaoKuan(),tb_vmc_system_parameter.getMainPwd(), tb_vmc_system_parameter.getAmount(),
	 						tb_vmc_system_parameter.getCard(), tb_vmc_system_parameter.getZhifubaofaca(), tb_vmc_system_parameter.getZhifubaoer(), tb_vmc_system_parameter.getWeixing(),
	 						tb_vmc_system_parameter.getPrinter(), tb_vmc_system_parameter.getLanguage(), tb_vmc_system_parameter.getRstTime(), tb_vmc_system_parameter.getRstDay(),
	 						tb_vmc_system_parameter.getBaozhiProduct(), tb_vmc_system_parameter.getEmptyProduct(), tb_vmc_system_parameter.getProSortType(),
	 						tb_vmc_system_parameter.getMarketAmount(),tb_vmc_system_parameter.getBillAmount()});
	 		
	        }
	        else
	        {
	            // ִ�������Ʒ		
	     		db.execSQL(
	     				"update vmc_system_parameter " +
	     				"set " +
	     				"devID=?,devhCode=?,isNet=?,isfenClass=?,isbuyCar=?," +
	     				"liebiaoKuan=?,mainPwd=?,amount=?,card=?,zhifubaofaca=?,zhifubaoer=?,weixing=?,printer=?," +
	     				"language=?,rstTime=?,rstDay=?,baozhiProduct=?,emptyProduct=?,proSortType=?,marketAmount=?,billAmount=?" 
	     				,
	     		        new Object[] { tb_vmc_system_parameter.getDevID(), tb_vmc_system_parameter.getDevhCode(),tb_vmc_system_parameter.getIsNet(), tb_vmc_system_parameter.getIsfenClass(),
	     						tb_vmc_system_parameter.getIsbuyCar(), tb_vmc_system_parameter.getLiebiaoKuan(),tb_vmc_system_parameter.getMainPwd(), tb_vmc_system_parameter.getAmount(),
	     						tb_vmc_system_parameter.getCard(), tb_vmc_system_parameter.getZhifubaofaca(), tb_vmc_system_parameter.getZhifubaoer(), tb_vmc_system_parameter.getWeixing(),
	     						tb_vmc_system_parameter.getPrinter(), tb_vmc_system_parameter.getLanguage(), tb_vmc_system_parameter.getRstTime(), tb_vmc_system_parameter.getRstDay(),
	     						tb_vmc_system_parameter.getBaozhiProduct(), tb_vmc_system_parameter.getEmptyProduct(), tb_vmc_system_parameter.getProSortType(),
	     						tb_vmc_system_parameter.getMarketAmount(),tb_vmc_system_parameter.getBillAmount()});
	     		
	            }	
	        
	        // ��������ı�־Ϊ�ɹ������������setTransactionSuccessful() ������Ĭ�ϻ�ع�����
		    db.setTransactionSuccessful();
	    } catch (Exception e) {
	        // process it
	        e.printStackTrace();
	    } finally {
	        // ��������ı�־�Ƿ�Ϊ�ɹ������Ϊ�ɹ����ύ���񣬷���ع�����
	        db.endTransaction();
	        if (!cursor.isClosed()) 
	 		{  
	 			cursor.close();  
	 		}  
	 		db.close(); 
	    }
	} 
	//�޸�����
	public void updatepwd(Tb_vmc_system_parameter tb_vmc_system_parameter)throws SQLException
	{
		int max=0;
		//ȡ������ֵ
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        Cursor cursor = db.rawQuery("select count(devID) from vmc_system_parameter", null);// ��ȡ������Ϣ�ļ�¼��
        if (cursor.moveToNext()) {// �ж�Cursor���Ƿ�������

            max=cursor.getInt(0);// �����ܼ�¼��
        }
     
        // ����һ������
	    db.beginTransaction();
	    try {
	        if(max>0)	        
	        {
	            // ִ�������Ʒ		
	     		db.execSQL(
	     				"update vmc_system_parameter " +
	     				"set " +
	     				"mainPwd=? " +
	     				"where devID=?" 
	     				,
	     		        new Object[] { tb_vmc_system_parameter.getMainPwd(),tb_vmc_system_parameter.getDevID()});
	     		
	            }		           
	        
	        // ��������ı�־Ϊ�ɹ������������setTransactionSuccessful() ������Ĭ�ϻ�ع�����
		    db.setTransactionSuccessful();
	    } catch (Exception e) {
	        // process it
	        e.printStackTrace();
	    } finally {
	        // ��������ı�־�Ƿ�Ϊ�ɹ������Ϊ�ɹ����ύ���񣬷���ع�����
	        db.endTransaction();
	        if (!cursor.isClosed()) 
	 		{  
	 			cursor.close();  
	 		}  
	 		db.close(); 
	    }
	} 
	//�޸Ļ��Ϣ
	public void updateevent(Tb_vmc_system_parameter tb_vmc_system_parameter)throws SQLException
	{
		int max=0;
		//ȡ������ֵ
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        Cursor cursor = db.rawQuery("select count(devID) from vmc_system_parameter", null);// ��ȡ������Ϣ�ļ�¼��
        if (cursor.moveToNext()) {// �ж�Cursor���Ƿ�������

            max=cursor.getInt(0);// �����ܼ�¼��
        }
     
        // ����һ������
	    db.beginTransaction();
	    try {
	        if(max>0)	        
	        {
	            // ִ�������Ʒ		
	     		db.execSQL(
	     				"update vmc_system_parameter " +
	     				"set " +
	     				"event=? " +
	     				"where devID=?" 
	     				,
	     		        new Object[] { tb_vmc_system_parameter.getEvent(),tb_vmc_system_parameter.getDevID()});
	     		
	            }		           
	        
	        // ��������ı�־Ϊ�ɹ������������setTransactionSuccessful() ������Ĭ�ϻ�ع�����
		    db.setTransactionSuccessful();
	    } catch (Exception e) {
	        // process it
	        e.printStackTrace();
	    } finally {
	        // ��������ı�־�Ƿ�Ϊ�ɹ������Ϊ�ɹ����ύ���񣬷���ع�����
	        db.endTransaction();
	        if (!cursor.isClosed()) 
	 		{  
	 			cursor.close();  
	 		}  
	 		db.close(); 
	    }
	} 
	//�޸Ĺ�����ʾ��Ϣ
	public void updatedemo(Tb_vmc_system_parameter tb_vmc_system_parameter)throws SQLException
	{
		int max=0;
		//ȡ������ֵ
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        Cursor cursor = db.rawQuery("select count(devID) from vmc_system_parameter", null);// ��ȡ������Ϣ�ļ�¼��
        if (cursor.moveToNext()) {// �ж�Cursor���Ƿ�������

            max=cursor.getInt(0);// �����ܼ�¼��
        }
     
        // ����һ������
	    db.beginTransaction();
	    try {
	        if(max>0)	        
	        {
	            // ִ�������Ʒ		
	     		db.execSQL(
	     				"update vmc_system_parameter " +
	     				"set " +
	     				"demo=? " +
	     				"where devID=?" 
	     				,
	     		        new Object[] { tb_vmc_system_parameter.getDemo(),tb_vmc_system_parameter.getDevID()});
	     		
	            }		           
	        
	        // ��������ı�־Ϊ�ɹ������������setTransactionSuccessful() ������Ĭ�ϻ�ع�����
		    db.setTransactionSuccessful();
	    } catch (Exception e) {
	        // process it
	        e.printStackTrace();
	    } finally {
	        // ��������ı�־�Ƿ�Ϊ�ɹ������Ϊ�ɹ����ύ���񣬷���ع�����
	        db.endTransaction();
	        if (!cursor.isClosed()) 
	 		{  
	 			cursor.close();  
	 		}  
	 		db.close(); 
	    }
	} 
	/**
     * ����һ����Ʒ��Ϣ
     * 
     * @param id
     * @return
     */
    public Tb_vmc_system_parameter find() 
    {
    	Tb_vmc_system_parameter tb_vmc_system_parameter=null;
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        Cursor cursor = db.rawQuery("select devID,devhCode,isNet,isfenClass,isbuyCar," +
 				"liebiaoKuan,mainPwd,amount,card,zhifubaofaca,zhifubaoer,weixing,printer," +
 				"language,rstTime,rstDay,baozhiProduct,emptyProduct,proSortType,marketAmount,billAmount," +
 				"event,demo from vmc_system_parameter", null );// ���ݱ�Ų���֧����Ϣ�����洢��Cursor����
        if (cursor.moveToNext()) 
        {// �������ҵ���֧����Ϣ

            // ����������֧����Ϣ�洢��Tb_outaccount����
            tb_vmc_system_parameter=new Tb_vmc_system_parameter(cursor.getString(cursor.getColumnIndex("devID")), cursor.getString(cursor.getColumnIndex("devhCode")),
    				cursor.getInt(cursor.getColumnIndex("isNet")),cursor.getInt(cursor.getColumnIndex("isfenClass")),cursor.getInt(cursor.getColumnIndex("isbuyCar")),cursor.getInt(cursor.getColumnIndex("liebiaoKuan")),
    				cursor.getString(cursor.getColumnIndex("mainPwd")),cursor.getInt(cursor.getColumnIndex("amount")), cursor.getInt(cursor.getColumnIndex("card")), cursor.getInt(cursor.getColumnIndex("zhifubaofaca")),
    				cursor.getInt(cursor.getColumnIndex("zhifubaoer")), cursor.getInt(cursor.getColumnIndex("weixing")), cursor.getInt(cursor.getColumnIndex("printer")), cursor.getInt(cursor.getColumnIndex("language")),
    				cursor.getString(cursor.getColumnIndex("rstTime")), cursor.getInt(cursor.getColumnIndex("rstDay")),  cursor.getInt(cursor.getColumnIndex("baozhiProduct")),  cursor.getInt(cursor.getColumnIndex("emptyProduct")),
    				cursor.getInt(cursor.getColumnIndex("proSortType")),cursor.getFloat(cursor.getColumnIndex("marketAmount")),cursor.getFloat(cursor.getColumnIndex("billAmount")),
    				cursor.getString(cursor.getColumnIndex("event")),cursor.getString(cursor.getColumnIndex("demo"))
    				
    		);
        }
        else
        {
        	
		}        
        if (!cursor.isClosed()) 
 		{  
 			cursor.close();  
 		}  
 		db.close();
        return tb_vmc_system_parameter;// ���û����Ϣ���򷵻�null
    }
}
