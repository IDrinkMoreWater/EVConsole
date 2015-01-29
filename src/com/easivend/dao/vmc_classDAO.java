/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        vmc_classDAO �����Ͳ����ļ�  
**------------------------------------------------------------------------------------------------------
** Created by:          yanbo 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.dao;

import com.easivend.model.Tb_vmc_class;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;



public class vmc_classDAO 
{
	private DBOpenHelper helper;// ����DBOpenHelper����
    private SQLiteDatabase db;// ����SQLiteDatabase����
    // ���幹�캯��
	public vmc_classDAO(Context context) 
	{
		helper=new DBOpenHelper(context);// ��ʼ��DBOpenHelper����
	}
	//���
	public void add(Tb_vmc_class tb_vmc_class)
	{
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// ִ�����
		db.execSQL("insert into vmc_class (classID,className) values (?,?)",
                new Object[] { tb_vmc_class.getClassID(), tb_vmc_class.getClassName() });
	}
    //�޸�
	public void update(Tb_vmc_class tb_vmc_class) {
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        // ִ���޸�
        db.execSQL("update vmc_class set className = ? where classID = ?", 
        		new Object[] { tb_vmc_class.getClassName(),tb_vmc_class.getClassID()});
    }
	//ɾ��
	public void detele(Tb_vmc_class tb_vmc_class) 
	{       
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        // ִ��ɾ��������Ϣ����
        db.execSQL("delete from vmc_class where classID = ?", 
        		new Object[] { tb_vmc_class.getClassID()});
        
    }
}
