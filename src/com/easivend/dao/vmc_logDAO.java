/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        vmc_logDAO ������־�ļ�  
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
import com.easivend.model.Tb_vmc_log;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class vmc_logDAO {
	private DBOpenHelper helper;// ����DBOpenHelper����
    private SQLiteDatabase db;// ����SQLiteDatabase����
    //SimpleDateFormat sdf = new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
    // ���幹�캯��
	public vmc_logDAO(Context context) 
	{
		helper=new DBOpenHelper(context);// ��ʼ��DBOpenHelper����
	}
	
	//���
	public void add(Tb_vmc_log tb_vmc_log)throws SQLException
	{
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		
        // ִ����Ӷ���֧����	
 		db.execSQL(
 				"insert into vmc_log" +
 				"(" +
 				"logID,logType,logDesc,logTime" +
 				") " +
 				"values" +
 				"(" +
 				"?,?,?,(datetime('now', 'localtime'))" +
 				")",
 		        new Object[] { tb_vmc_log.getLogID(), tb_vmc_log.getLogType(),tb_vmc_log.getLogDesc()});
 		
		db.close(); 
	}
	//ɾ��ʱ�䷶Χ�ڵ�����
	public void detele(String starttime, String endtime) 
	{   
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����        	
		// ִ��ɾ��������ϸ��Ϣ��	
 		db.execSQL(
 				"delete from vmc_log where logTime between ? and ?",
 				new String[] { starttime,endtime });
	 		 
        db.close();
    }
	//����ʱ�䷶Χ�ڵĶ���֧������
	public List<Tb_vmc_log> getScrollPay(String starttime, String endtime) 
	{   
		List<Tb_vmc_log> tb_inaccount = new ArrayList<Tb_vmc_log>();// �������϶���
             
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<����ʱ����"+starttime+",��"+endtime);     
        //ȡ��ʱ�䷶Χ�ڶ���֧������
		Cursor cursor = db.rawQuery("select logID,logType,logDesc,logTime " +
				" from vmc_log where logTime between ? and ?", 
				new String[] { starttime,endtime });// ��ȡ������Ϣ���е������
//		Cursor cursor = db.rawQuery("select logID,logType,logDesc,logTime " +
//				" from vmc_log ", 
//				null);// ��ȡ������Ϣ���е������
		while (cursor.moveToNext()) 
        {
			// ����������������Ϣ��ӵ�������
            tb_inaccount.add(new Tb_vmc_log
        		(
        				cursor.getString(cursor.getColumnIndex("logID")), cursor.getInt(cursor.getColumnIndex("logType")),
        				cursor.getString(cursor.getColumnIndex("logDesc")),cursor.getString(cursor.getColumnIndex("logTime"))
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
