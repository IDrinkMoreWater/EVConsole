/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        vmc_columnDAO ����������ļ�  
**------------------------------------------------------------------------------------------------------
** Created by:          yanbo 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.dao;

import java.util.ArrayList;
import java.util.List;

import com.easivend.common.ToolClass;
import com.easivend.model.Tb_vmc_column;
import com.easivend.model.Tb_vmc_product;
import com.easivend.model.Tb_vmc_system_parameter;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class vmc_columnDAO 
{
	private DBOpenHelper helper;// ����DBOpenHelper����
    private SQLiteDatabase db;// ����SQLiteDatabase����
    //SimpleDateFormat sdf = new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
    // ���幹�캯��
	public vmc_columnDAO(Context context) 
	{
		helper=new DBOpenHelper(context);// ��ʼ��DBOpenHelper����
	}
	
	//��ӻ��޸Ļ�������
	public void addorupdate(Tb_vmc_column tb_vmc_column)throws SQLException
	{
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		//�Ƿ��Ѿ����ڱ���Ʒ
        Cursor cursor = db.rawQuery("select columnID from vmc_column where cabID = ? and columnID=?", 
        		new String[] { tb_vmc_column.getCabineID(),tb_vmc_column.getColumnID() });// ���ݱ�Ų���֧����Ϣ�����洢��Cursor����
        if (cursor.moveToNext()) // �������ҵ���֧����Ϣ
        {
        	//ִ���޸Ļ�������
	 		db.execSQL(
				"update vmc_column set " +
				"productID=?,pathCount=?,pathRemain=?,columnStatus=?," +
				"lasttime=(datetime('now', 'localtime')) " +
				"where cabID=? and columnID=?",
		        new Object[] { tb_vmc_column.getProductID(),tb_vmc_column.getPathCount(),
						tb_vmc_column.getPathRemain(),tb_vmc_column.getColumnStatus(),
						tb_vmc_column.getCabineID(),tb_vmc_column.getColumnID()});
		
		}	      
        else
        {	
        	// ִ����ӻ���		
     		db.execSQL(
     				"insert into vmc_column" +
     				"(" +
     				"cabID,columnID,productID,pathCount,pathRemain," +
     				"columnStatus,lasttime" +
     				") " +
     				"values" +
     				"(" +
     				"?,?,?,?,?,?,(datetime('now', 'localtime'))" +
     				")",
     		        new Object[] { tb_vmc_column.getCabineID(),tb_vmc_column.getColumnID(),tb_vmc_column.getProductID(),
     						tb_vmc_column.getPathCount(),tb_vmc_column.getPathRemain(),tb_vmc_column.getColumnStatus()});
     		
        }
        
        
        
 		if (!cursor.isClosed()) 
 		{  
 			cursor.close();  
 		}  
 		db.close(); 
	}
	//ɾ������
	public void detele(Tb_vmc_column tb_vmc_column) 
	{       
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        // ִ��ɾ����Ʒ��
        db.execSQL("delete from vmc_column where cabID=? and columnID=?", 
        		new Object[] { tb_vmc_column.getCabineID(),tb_vmc_column.getColumnID()});        
        db.close(); 
	}
	//ɾ���ù�ȫ��������Ϣ
  	public void deteleCab(String cabID) 
  	{       
          db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
          // ִ��ɾ���ù���Ʒ��
          db.execSQL("delete from vmc_column where cabID=?", 
          		new Object[] { cabID});    
          
          db.close(); 
  	}		
	/**
     * ����һ����Ʒ��Ϣ
     * 
     * @param id
     * @return
     */
    public Tb_vmc_column find(String cabID,String columnID) 
    {
    	Tb_vmc_column tb_vmc_column=null;
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        Cursor cursor = db.rawQuery("select cabID,columnID,productID,pathCount,pathRemain," +
            		"columnStatus,lasttime from vmc_column where cabID=? and columnID=?", 
            		new String[] { cabID,columnID });// ���ݱ�Ų���֧����Ϣ�����洢��Cursor����
        if (cursor.moveToNext()) {// �������ҵ���֧����Ϣ

            // ����������֧����Ϣ�洢��Tb_outaccount����
        	tb_vmc_column=new Tb_vmc_column(
    				cursor.getString(cursor.getColumnIndex("cabID")), cursor.getString(cursor.getColumnIndex("columnID")),
    				cursor.getString(cursor.getColumnIndex("productID")),cursor.getInt(cursor.getColumnIndex("pathCount")),
    				cursor.getInt(cursor.getColumnIndex("pathRemain")),cursor.getInt(cursor.getColumnIndex("columnStatus")),
    				cursor.getString(cursor.getColumnIndex("lasttime"))
    		);
        }
        if (!cursor.isClosed()) 
 		{  
 			cursor.close();  
 		}  
 		db.close();
        return tb_vmc_column;// ���û����Ϣ���򷵻�null
    }
    
    /**
     * ��ȡ����������Ʒ��Ϣ
     * 
     * @param cabID
     *            ���    
     * @return
     */
    public List<Tb_vmc_column> getScrollData(String cabID) 
    {
        List<Tb_vmc_column> tb_inaccount = new ArrayList<Tb_vmc_column>();// �������϶���
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        Cursor cursor = db.rawQuery("select cabID,columnID,productID,pathCount,pathRemain," +
        		"columnStatus,lasttime from vmc_column where cabID=? ", 
        		new String[] { cabID});// ���ݱ�Ų���֧����Ϣ�����洢��Cursor����
    
        //�������е�������Ϣ
        while (cursor.moveToNext()) 
        {	
            // ����������������Ϣ��ӵ�������
            tb_inaccount.add(new Tb_vmc_column
        		(
        				cursor.getString(cursor.getColumnIndex("cabID")), cursor.getString(cursor.getColumnIndex("columnID")),
        				cursor.getString(cursor.getColumnIndex("productID")),cursor.getInt(cursor.getColumnIndex("pathCount")),
        				cursor.getInt(cursor.getColumnIndex("pathRemain")),cursor.getInt(cursor.getColumnIndex("columnStatus")),
        				cursor.getString(cursor.getColumnIndex("lasttime"))
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
