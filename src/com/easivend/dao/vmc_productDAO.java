/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        vmc_productDAO ��Ʒ������ļ�  
**------------------------------------------------------------------------------------------------------
** Created by:          yanbo 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.dao;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.easivend.common.ToolClass;
import com.easivend.model.Tb_vmc_product;


public class vmc_productDAO
{
	private DBOpenHelper helper;// ����DBOpenHelper����
    private SQLiteDatabase db;// ����SQLiteDatabase����
    //SimpleDateFormat sdf = new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
    // ���幹�캯��
	public vmc_productDAO(Context context) 
	{
		helper=new DBOpenHelper(context);// ��ʼ��DBOpenHelper����
	}
	//���
	public void add(Tb_vmc_product tb_vmc_product,String classID)throws SQLException
	{
		int max=0;
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		//ȡ������ֵ
		Cursor cursor = db.rawQuery("select max(paixu) from vmc_product", null);// ��ȡ������Ϣ���е������
        if (cursor.moveToLast()) {// ����Cursor�е����һ������
        	max=cursor.getInt(0); 
        }
        // ����һ������
	    db.beginTransaction();
	    try {
		
	        // ִ�������Ʒ		
	 		db.execSQL(
	 				"insert into vmc_product" +
	 				"(" +
	 				"productID,productName,productDesc,marketPrice,salesPrice," +
	 				"shelfLife,downloadTime,onloadTime,attBatch1,attBatch2,attBatch3,paixu,isdelete" +
	 				") " +
	 				"values" +
	 				"(" +
	 				"?,?,?,?,?,?,(datetime('now', 'localtime')),(datetime('now', 'localtime')),?,?,?,?,?" +
	 				")",
	 		        new Object[] { tb_vmc_product.getProductID(), tb_vmc_product.getProductName(),tb_vmc_product.getProductDesc(), tb_vmc_product.getMarketPrice(),
	 						tb_vmc_product.getSalesPrice(), tb_vmc_product.getShelfLife(),tb_vmc_product.getAttBatch1(), tb_vmc_product.getAttBatch2(),
	 						tb_vmc_product.getAttBatch3(), max+1,tb_vmc_product.getIsdelete()});
	 		
	 		// ִ�������Ʒ����������	
	 		if(classID.equals("")!=true)
	 		{
			db.execSQL(
					"insert into vmc_classproduct" +
					"(" +
					"classID,productID,classTime" +
					") " +
					"values" +
					"(" +
					"?,?,(datetime('now', 'localtime'))" +
					")",
			        new Object[] { classID,tb_vmc_product.getProductID()});
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
    //�޸�
	public void update(Tb_vmc_product tb_vmc_product,String classID)throws SQLException
	{
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����		
        // ִ���޸���Ʒ		
 		db.execSQL(
 				"update vmc_product set " +
 				"productName=?,productDesc=?,marketPrice=?,salesPrice=?,shelfLife=?," +
 				"downloadTime=(datetime('now', 'localtime')),onloadTime=(datetime('now', 'localtime'))," +
 				"attBatch1=?,attBatch2=?,attBatch3=? " +
 				"where productID=?",
 		        new Object[] { tb_vmc_product.getProductName(),tb_vmc_product.getProductDesc(), tb_vmc_product.getMarketPrice(),
 						tb_vmc_product.getSalesPrice(), tb_vmc_product.getShelfLife(),tb_vmc_product.getAttBatch1(), tb_vmc_product.getAttBatch2(),
 						tb_vmc_product.getAttBatch3(),tb_vmc_product.getProductID()});
 		
 		// ִ�������Ʒ����������	
 		//����ԭ����ƷID��Ӧ�����ID
 		String clsID=findclass(tb_vmc_product.getProductID()); 	
 		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����		
 		// ����һ������
	    db.beginTransaction();
	    try {
	 		//�����Ҫ������Ʒ����
	 		if(classID.equals("")!=true)
	 		{
	 			if(ToolClass.isEmptynull(clsID)==true)//���ԭ��û����Ʒ����
	 			{
	 				db.execSQL(
	 						"insert into vmc_classproduct" +
	 						"(" +
	 						"classID,productID,classTime" +
	 						") " +
	 						"values" +
	 						"(" +
	 						"?,?,(datetime('now', 'localtime'))" +
	 						")",
	 				        new Object[] { classID,tb_vmc_product.getProductID()});
	 			}
	 			//���ԭ������Ʒ����
	 			else 
	 			{
					db.execSQL(
							"update vmc_classproduct set " +
							"classID=?,classTime=(datetime('now', 'localtime')) " +
							"where productID=?",
					        new Object[] { classID,tb_vmc_product.getProductID()});
	 			}
	 		}	
	 		//����Ҫ������Ʒ����
	 		else
	 		{
	 			if(ToolClass.isEmptynull(clsID)!=true)//���ԭ������Ʒ����
	 			{
	 				db.execSQL(
							"delete from vmc_classproduct " +
							"where productID=?",
					        new Object[] { tb_vmc_product.getProductID()});
	 			}
	 		}
	 	
	 		// ��������ı�־Ϊ�ɹ������������setTransactionSuccessful() ������Ĭ�ϻ�ع�����
	        db.setTransactionSuccessful();
	    } catch (Exception e) {
	        // process it
	        e.printStackTrace();
	    } finally {
	        // ��������ı�־�Ƿ�Ϊ�ɹ������Ϊ�ɹ����ύ���񣬷���ع�����
	        db.endTransaction();
	        db.close(); 
	    }
	}
	//��ӻ����޸�
	public void addorupdate(Tb_vmc_product tb_vmc_product,String classID) {
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        Cursor cursor = db.rawQuery("select productID from vmc_product where productID=?", new String[] { tb_vmc_product.getProductID()});// ��ȡ������Ϣ���е������
        if (cursor.moveToLast()) {// ����Cursor�е����һ������
        	update(tb_vmc_product,classID);//ִ���޸�
        }
        else {
        	add(tb_vmc_product,classID);//ִ�����
		}
        if (!cursor.isClosed()) 
 		{  
 			cursor.close();  
 		}  
        db.close(); 
	}
	//ɾ������
	public void detele(Tb_vmc_product tb_vmc_product) 
	{       
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        //�Ƿ��ڻ��������й���
  		Cursor cursor = db.rawQuery("select productID from vmc_column where productID=?", new String[] { tb_vmc_product.getProductID()});// ��ȡ������Ϣ���е������
  		// ����һ������
	    db.beginTransaction();
	    try {
	  		// û�й�������������ɾ��
	  		if (!cursor.moveToLast()) 
	        {
	  			// ִ��ɾ����Ʒ��
	  	        db.execSQL("delete from vmc_product where productID=?", 
	  	        		new Object[] { tb_vmc_product.getProductID()});
	  	        //ɾ����Ʒ���������
	  	        db.execSQL(
	  					"delete from vmc_classproduct " +
	  					"where productID=?",
	  			        new Object[] { tb_vmc_product.getProductID()});
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
     * ͨ��ID�h��������Ϣ
     * 
     * @param ids
     */
    public void detele(StringBuffer... ids) 
    {
//    	// �ж��Ƿ����Ҫɾ����id
//        if (ids.length > 0) 
//        {
//            StringBuffer sb = new StringBuffer();// ����StringBuffer����
//            for (int i = 0; i < ids.length; i++) 
//            {// ����Ҫɾ����id����
//
//                sb.append('?').append(',');// ��ɾ��������ӵ�StringBuffer������
//            }
//            sb.deleteCharAt(sb.length() - 1);// ȥ�����һ����,���ַ�
//            db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
//            // ִ��ɾ��������Ϣ����
//            db.execSQL("delete from vmc_class where classID in (" + sb + ")", (Object[]) ids);
//        }
    }
    
    //ɾ��product��,���ǲ��䶯classproduct
    public void deleteall()
    {
    	  db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
          
          // ����һ������
  	    db.beginTransaction();
  	    try {
  	          // ִ��ɾ����
  	          db.execSQL("delete from vmc_product"); 
  	          
  	       // ��������ı�־Ϊ�ɹ������������setTransactionSuccessful() ������Ĭ�ϻ�ع�����
  		      db.setTransactionSuccessful();
  	    } catch (Exception e) {
  	        // process it
  	        e.printStackTrace();
  	    } finally {
  	        // ��������ı�־�Ƿ�Ϊ�ɹ������Ϊ�ɹ����ύ���񣬷���ع�����
  	        db.endTransaction();
  	        db.close(); 
  	    }
    }
    
    /**
     * ���ƻ������������
     * 
     * @param type=1����,2����
     */
  	public void sortupdown(Tb_vmc_product tb_vmc_product,int type) 
  	{   
  		int index=0,max=0;
  		Cursor cursor = null;
  		String nextid="";
  		
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
	    //ȡ�õ�ǰ����ֵ
	  	cursor = db.rawQuery("select paixu from vmc_product where productID=?", 
        		 new String[] { tb_vmc_product.getProductID()});// ��ȡ������Ϣ���е������
	    if (cursor.moveToLast()) 
	    {// ����Cursor�е����һ������
	    	index=cursor.getInt(0); 
	    }
	    
	    // ����һ������
	    db.beginTransaction();
	    try {
		    //����
		    if(type==1)
		    {
		    	//���ǵ�һ��ֵ
		    	if(index>0)
		    	{
		    		//ȡ����һ����ֵ��id
		    	  	cursor = db.rawQuery("select productID from vmc_product where paixu=?", 
		            		 new String[] { String.valueOf(index-1)});// ��ȡ������Ϣ���е������
		    	    if (cursor.moveToLast()) 
		    	    {// ����Cursor�е����һ������
		    	    	nextid=cursor.getString(0); 
		    	    }
		    	    //��һ��ֵ+1
		    	    db.execSQL(
							"update vmc_product set " +
							"paixu=? " +
							"where productID=?",
					        new Object[] { index,nextid});
		    	    //��ֵ-1
		    	    db.execSQL(
							"update vmc_product set " +
							"paixu=? " +
							"where productID=?",
					        new Object[] { index-1,tb_vmc_product.getProductID()});
		    	}
		    }
		    //����
		    else 
		    {
		    	//ȡ���������ֵ
		  		cursor = db.rawQuery("select max(paixu) from vmc_product", null);// ��ȡ������Ϣ���е������
		          if (cursor.moveToLast()) {// ����Cursor�е����һ������
		          	max=cursor.getInt(0); 
		          }
		        //�������һ��ֵ  
		        if(index<max)
		        {
		    		//ȡ����һ����ֵ��id
		    	  	cursor = db.rawQuery("select productID from vmc_product where paixu=?", 
		            		 new String[] { String.valueOf(index+1)});// ��ȡ������Ϣ���е������
		    	    if (cursor.moveToLast()) 
		    	    {// ����Cursor�е����һ������
		    	    	nextid=cursor.getString(0); 
		    	    }
		    	    //��һ��ֵ-1
		    	    db.execSQL(
							"update vmc_product set " +
							"paixu=? " +
							"where productID=?",
					        new Object[] { index,nextid});
		    	    //��ֵ+1
		    	    db.execSQL(
							"update vmc_product set " +
							"paixu=? " +
							"where productID=?",
					        new Object[] { index+1,tb_vmc_product.getProductID()});
		    	}
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
    public Tb_vmc_product find(String productID) 
    {
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        Cursor cursor = db.rawQuery("select productID,productName,productDesc,marketPrice," +
            		"salesPrice,shelfLife,downloadTime,onloadTime,attBatch1,attBatch2,attBatch3," +
            		"paixu,isdelete from vmc_product where productID = ?", new String[] { String.valueOf(productID) });// ���ݱ�Ų���֧����Ϣ�����洢��Cursor����
        if (cursor.moveToNext()) {// �������ҵ���֧����Ϣ

            // ����������֧����Ϣ�洢��Tb_outaccount����
            return new Tb_vmc_product(
    				cursor.getString(cursor.getColumnIndex("productID")), cursor.getString(cursor.getColumnIndex("productName")),
    				cursor.getString(cursor.getColumnIndex("productDesc")),cursor.getFloat(cursor.getColumnIndex("marketPrice")),
    				cursor.getFloat(cursor.getColumnIndex("salesPrice")),cursor.getInt(cursor.getColumnIndex("shelfLife")),
    				cursor.getString(cursor.getColumnIndex("downloadTime")),cursor.getString(cursor.getColumnIndex("onloadTime")),
    				cursor.getString(cursor.getColumnIndex("attBatch1")), cursor.getString(cursor.getColumnIndex("attBatch2")),
    				cursor.getString(cursor.getColumnIndex("attBatch3")),cursor.getInt(cursor.getColumnIndex("paixu")),
    				cursor.getInt(cursor.getColumnIndex("isdelete"))
    		);
        }
        if (!cursor.isClosed()) 
 		{  
 			cursor.close();  
 		}  
 		db.close();
        return null;// ���û����Ϣ���򷵻�null
    }
    
    /**
     * ����һ����Ʒ��Ӧ��������Ϣ
     * 
     * @param id
     * @return
     */
    public String findclass(String productID) 
    {
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        Cursor cursor = db.rawQuery("select classID from vmc_classproduct where productID = ?", new String[] { String.valueOf(productID) });// ���ݱ�Ų���֧����Ϣ�����洢��Cursor����
        if (cursor.moveToNext()) {// �������ҵ���֧����Ϣ

            // ����������֧����Ϣ�洢��Tb_outaccount����
            return cursor.getString(cursor.getColumnIndex("classID"));    		
        }
        if (!cursor.isClosed()) 
 		{  
 			cursor.close();  
 		}  
 		db.close();
        return "";// ���û����Ϣ���򷵻�null
    }

    /**
     * ��ȡָ��������Ʒ��Ϣ
     * 
     * @param start
     *            ��ʼλ��
     * @param count
     *            ÿҳ��ʾ����
     *        datasort���򷽷�    
     * @return
     */
    public List<Tb_vmc_product> getScrollData(int start, int count,String datasort) 
    {
        List<Tb_vmc_product> tb_inaccount = new ArrayList<Tb_vmc_product>();// �������϶���
        Cursor cursor = null;
        ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ʒsort="+datasort,"log.txt");
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        if(datasort.equals("sale"))
        {
        	// ��ȡ����������Ϣ
            cursor = db.rawQuery("select productID,productName,productDesc,marketPrice," +
            		"salesPrice,shelfLife,downloadTime,onloadTime,attBatch1,attBatch2,attBatch3," +
            		"paixu,isdelete from vmc_product order by paixu asc", null);
        }
        else if(datasort.equals("marketPrice"))
        {
        	// ��ȡ����������Ϣ
            cursor = db.rawQuery("select productID,productName,productDesc,marketPrice," +
            		"salesPrice,shelfLife,downloadTime,onloadTime,attBatch1,attBatch2,attBatch3," +
            		"paixu,isdelete from vmc_product order by marketPrice asc", null);
        }
        else if(datasort.equals("salesPrice"))
        {
        	// ��ȡ����������Ϣ
            cursor = db.rawQuery("select productID,productName,productDesc,marketPrice," +
            		"salesPrice,shelfLife,downloadTime,onloadTime,attBatch1,attBatch2,attBatch3," +
            		"paixu,isdelete from vmc_product order by salesPrice asc", null);
        }
        else if(datasort.equals("shelfLife"))
        {
        	// ��ȡ����������Ϣ
            cursor = db.rawQuery("select productID,productName,productDesc,marketPrice," +
            		"salesPrice,shelfLife,downloadTime,onloadTime,attBatch1,attBatch2,attBatch3," +
            		"paixu,isdelete from vmc_product order by (datetime('now', 'localtime'))-onloadTime asc", null);
        }
        else if(datasort.equals("colucount"))
        {
        	// ��ȡ����������Ϣ
            cursor = db.rawQuery("select productID,productName,productDesc,marketPrice," +
            		"salesPrice,shelfLife,downloadTime,onloadTime,attBatch1,attBatch2,attBatch3," +
            		"paixu,isdelete from vmc_product order by paixu asc", null);
        }
        else if(datasort.equals("onloadTime"))
        {
        	// ��ȡ����������Ϣ
            cursor = db.rawQuery("select productID,productName,productDesc,marketPrice," +
            		"salesPrice,shelfLife,downloadTime,onloadTime,attBatch1,attBatch2,attBatch3," +
            		"paixu,isdelete from vmc_product order by onloadTime asc", null);
        }
        else if(datasort.equals("shoudong"))
        {
        	// ��ȡ����������Ϣ
            cursor = db.rawQuery("select productID,productName,productDesc,marketPrice," +
            		"salesPrice,shelfLife,downloadTime,onloadTime,attBatch1,attBatch2,attBatch3," +
            		"paixu,isdelete from vmc_product order by paixu asc", null);
        }
       
        //�������е�������Ϣ
        while (cursor.moveToNext()) 
        {	
            // ����������������Ϣ��ӵ�������
            tb_inaccount.add(new Tb_vmc_product
        		(
        				cursor.getString(cursor.getColumnIndex("productID")), cursor.getString(cursor.getColumnIndex("productName")),
        				cursor.getString(cursor.getColumnIndex("productDesc")),cursor.getFloat(cursor.getColumnIndex("marketPrice")),
        				cursor.getFloat(cursor.getColumnIndex("salesPrice")),cursor.getInt(cursor.getColumnIndex("shelfLife")),
        				cursor.getString(cursor.getColumnIndex("downloadTime")),cursor.getString(cursor.getColumnIndex("onloadTime")),
        				cursor.getString(cursor.getColumnIndex("attBatch1")), cursor.getString(cursor.getColumnIndex("attBatch2")),
        				cursor.getString(cursor.getColumnIndex("attBatch3")),cursor.getInt(cursor.getColumnIndex("paixu")),
        				cursor.getInt(cursor.getColumnIndex("isdelete"))
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
    
    /**
     * ���غ�������ȡָ������������Ʒ��Ϣ
     * 
     * @param param��������
     *        datasort���򷽷�    
     * 
     *            
     * @return
     */
    public List<Tb_vmc_product> getScrollData(String param,String datasort) 
    {
    	String params="";
        List<Tb_vmc_product> tb_inaccount = new ArrayList<Tb_vmc_product>();// �������϶���
        Cursor cursor = null;
        params="%"+param+"%";
        ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproductName="+params+" sort="+datasort,"log.txt");
        
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        if(datasort.equals("sale"))
        {
        	// ��ȡ����������Ϣ
            cursor = db.rawQuery("select productID,productName,productDesc,marketPrice," +
            		"salesPrice,shelfLife,downloadTime,onloadTime,attBatch1,attBatch2,attBatch3," +
            		"paixu,isdelete from vmc_product where productName like ?  order by paixu asc", new String[] { params });
        }
        else if(datasort.equals("marketPrice"))
        {
        	// ��ȡ����������Ϣ
            cursor = db.rawQuery("select productID,productName,productDesc,marketPrice," +
            		"salesPrice,shelfLife,downloadTime,onloadTime,attBatch1,attBatch2,attBatch3," +
            		"paixu,isdelete from vmc_product where productName like ? order by marketPrice asc", new String[] { params });
        }
        else if(datasort.equals("salesPrice"))
        {
        	// ��ȡ����������Ϣ
            cursor = db.rawQuery("select productID,productName,productDesc,marketPrice," +
            		"salesPrice,shelfLife,downloadTime,onloadTime,attBatch1,attBatch2,attBatch3," +
            		"paixu,isdelete from vmc_product where productName like ? order by salesPrice asc", new String[] { params });
        }
        else if(datasort.equals("shelfLife"))
        {
        	// ��ȡ����������Ϣ
            cursor = db.rawQuery("select productID,productName,productDesc,marketPrice," +
            		"salesPrice,shelfLife,downloadTime,onloadTime,attBatch1,attBatch2,attBatch3," +
            		"paixu,isdelete from vmc_product where productName like ?  order by (datetime('now', 'localtime'))-onloadTime asc", new String[] { params });
        }
        else if(datasort.equals("colucount"))
        {
        	// ��ȡ����������Ϣ
            cursor = db.rawQuery("select productID,productName,productDesc,marketPrice," +
            		"salesPrice,shelfLife,downloadTime,onloadTime,attBatch1,attBatch2,attBatch3," +
            		"paixu,isdelete from vmc_product where productName like ? order by paixu asc", new String[] { params });
        }
        else if(datasort.equals("onloadTime"))
        {
        	// ��ȡ����������Ϣ
            cursor = db.rawQuery("select productID,productName,productDesc,marketPrice," +
            		"salesPrice,shelfLife,downloadTime,onloadTime,attBatch1,attBatch2,attBatch3," +
            		"paixu,isdelete from vmc_product where productName like ? order by onloadTime asc", new String[] { params });
        }
        else if(datasort.equals("shoudong"))
        {
        	// ��ȡ����������Ϣ
            cursor = db.rawQuery("select productID,productName,productDesc,marketPrice," +
            		"salesPrice,shelfLife,downloadTime,onloadTime,attBatch1,attBatch2,attBatch3," +
            		"paixu,isdelete from vmc_product where productName like ? order by paixu asc", new String[] { params });
        }
        
        
        //�������е�������Ϣ
        while (cursor.moveToNext()) 
        {	
            // ����������������Ϣ��ӵ�������
            tb_inaccount.add(new Tb_vmc_product
        		(
        				cursor.getString(cursor.getColumnIndex("productID")), cursor.getString(cursor.getColumnIndex("productName")),
        				cursor.getString(cursor.getColumnIndex("productDesc")),cursor.getFloat(cursor.getColumnIndex("marketPrice")),
        				cursor.getFloat(cursor.getColumnIndex("salesPrice")),cursor.getInt(cursor.getColumnIndex("shelfLife")),
        				cursor.getString(cursor.getColumnIndex("downloadTime")),cursor.getString(cursor.getColumnIndex("onloadTime")),
        				cursor.getString(cursor.getColumnIndex("attBatch1")), cursor.getString(cursor.getColumnIndex("attBatch2")),
        				cursor.getString(cursor.getColumnIndex("attBatch3")),cursor.getInt(cursor.getColumnIndex("paixu")),
        				cursor.getInt(cursor.getColumnIndex("isdelete"))
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
    
    /**
     * ���غ�������ȡָ��������Ʒ��Ϣ
     * 
     * @param param��������
     *        datasort���򷽷�    
     * 
     *            
     * @return
     */
    public List<Tb_vmc_product> getScrollData(String classID) 
    {
    	List<Tb_vmc_product> tb_inaccount = new ArrayList<Tb_vmc_product>();// �������϶���
        Cursor cursor = null;
        //ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproductName="+params+" sort="+datasort);
        
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����        
    	// ��ȡ����������Ϣ
        cursor = db.rawQuery("select productID,productName,productDesc,marketPrice," +
        		"salesPrice,shelfLife,downloadTime,onloadTime,attBatch1,attBatch2,attBatch3," +
        		"paixu,isdelete from vmc_product where productID in" +
        		"(select productID from vmc_classproduct where classID=?)", new String[] { classID });
         
        //�������е�������Ϣ
        while (cursor.moveToNext()) 
        {	
            // ����������������Ϣ��ӵ�������
            tb_inaccount.add(new Tb_vmc_product
        		(
        				cursor.getString(cursor.getColumnIndex("productID")), cursor.getString(cursor.getColumnIndex("productName")),
        				cursor.getString(cursor.getColumnIndex("productDesc")),cursor.getFloat(cursor.getColumnIndex("marketPrice")),
        				cursor.getFloat(cursor.getColumnIndex("salesPrice")),cursor.getInt(cursor.getColumnIndex("shelfLife")),
        				cursor.getString(cursor.getColumnIndex("downloadTime")),cursor.getString(cursor.getColumnIndex("onloadTime")),
        				cursor.getString(cursor.getColumnIndex("attBatch1")), cursor.getString(cursor.getColumnIndex("attBatch2")),
        				cursor.getString(cursor.getColumnIndex("attBatch3")),cursor.getInt(cursor.getColumnIndex("paixu")),
        				cursor.getInt(cursor.getColumnIndex("isdelete"))
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

    /**
     * ��ȡ�ܼ�¼��
     * 
     * @return
     */
    public long getCount() {
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        Cursor cursor = db.rawQuery("select count(productID) from vmc_product", null);// ��ȡ������Ϣ�ļ�¼��
        if (cursor.moveToNext()) {// �ж�Cursor���Ƿ�������

            return cursor.getLong(0);// �����ܼ�¼��
        }
        if (!cursor.isClosed()) 
 		{  
 			cursor.close();  
 		}  
 		db.close();
        return 0;// ���û�����ݣ��򷵻�0
    }

    /**
     * ��ȡ���������
     * 
     * @return
     */
    public int getMaxId() {
//        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
//        Cursor cursor = db.rawQuery("select max(productID) from vmc_product", null);// ��ȡ������Ϣ���е������
//        while (cursor.moveToLast()) {// ����Cursor�е����һ������
//            return cursor.getInt(0);// ��ȡ���ʵ������ݣ��������
//        }
        return 0;// ���û�����ݣ��򷵻�0
    }
}
