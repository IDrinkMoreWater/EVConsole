package com.easivend.dao;

import java.util.ArrayList;
import java.util.List;

import com.easivend.model.Tb_vmc_cabinet;
import com.easivend.model.Tb_vmc_class;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class vmc_cabinetDAO
{
	private DBOpenHelper helper;// ����DBOpenHelper����
    private SQLiteDatabase db;// ����SQLiteDatabase����
    //SimpleDateFormat sdf = new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
    // ���幹�캯��
	public vmc_cabinetDAO(Context context) 
	{
		helper=new DBOpenHelper(context);// ��ʼ��DBOpenHelper����
	}
	//��ӹ�����
	public void add(Tb_vmc_cabinet tb_vmc_cabinet)throws SQLException
	{
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// ִ�����		
		db.execSQL("insert into vmc_cabinet (cabID,cabType) values (?,?)",
				new Object[] { tb_vmc_cabinet.getCabID(), tb_vmc_cabinet.getCabType() });
        
        
		db.close(); 
	}
	/**
     * ��ȡȫ������Ϣ
     *     
     * @return
     */
    public List<Tb_vmc_cabinet> getScrollData() 
    {
        List<Tb_vmc_cabinet> tb_inaccount = new ArrayList<Tb_vmc_cabinet>();// �������϶���
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        // ��ȡ����������Ϣ
        Cursor cursor = db.rawQuery("select cabID,cabType from vmc_cabinet", null);
        //�������е�������Ϣ
        while (cursor.moveToNext()) 
        {	
            // ����������������Ϣ��ӵ�������
            tb_inaccount.add(new Tb_vmc_cabinet(cursor.getString(cursor.getColumnIndex("cabID")),cursor.getInt(cursor.getColumnIndex("cabType"))));
        }
        if (!cursor.isClosed()) 
 		{  
 			cursor.close();  
 		}  
 		db.close(); 
        return tb_inaccount;// ���ؼ���
    }
    
    //ɾ���ù�
  	public void detele(String cabID) 
  	{       
          db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
          // ִ��ɾ���ù���Ʒ��
          db.execSQL("delete from vmc_cabinet where cabID=?", 
          		new Object[] { cabID});    
          
          db.close(); 
  	}
  	
    //ɾ���ù�
  	public void deteleall() 
  	{       
          db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
          // ִ��ɾ����
          db.execSQL("delete from vmc_class"); 
          db.execSQL("delete from vmc_classproduct"); 
          db.execSQL("delete from vmc_product"); 
          db.execSQL("delete from vmc_cabinet"); 
          db.execSQL("delete from vmc_column");    
          
          db.close(); 
  	}
}
