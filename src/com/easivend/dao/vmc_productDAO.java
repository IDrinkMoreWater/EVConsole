package com.easivend.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.easivend.evprotocol.ToolClass;
import com.easivend.model.Tb_vmc_class;
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
 		if(Integer.parseInt(classID)>0)
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
	}
    //�޸�
	public void update(Tb_vmc_class tb_vmc_class) {
//        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
//        // ִ���޸�
//        db.execSQL("update vmc_class set className = ?,classTime=(datetime('now', 'localtime')) where classID = ?", 
//        		new Object[] { tb_vmc_class.getClassName(),tb_vmc_class.getClassID()});
    }
	//ɾ������
	public void detele(Tb_vmc_class tb_vmc_class) 
	{       
//        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
//        // ִ��ɾ��������Ϣ����
//        db.execSQL("delete from vmc_class where classID = ?", 
//        		new Object[] { tb_vmc_class.getClassID()});
        
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
    
    /**
     * ����һ����Ʒ��Ϣ
     * 
     * @param id
     * @return
     */
    public Tb_vmc_product find(String productID) 
    {
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        Cursor cursor = db.rawQuery("select * from vmc_product where productID = ?", new String[] { String.valueOf(productID) });// ���ݱ�Ų���֧����Ϣ�����洢��Cursor����
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
        return "";// ���û����Ϣ���򷵻�null
    }

    /**
     * ��ȡָ��������Ʒ�����Ϣ
     * 
     * @param start
     *            ��ʼλ��
     * @param count
     *            ÿҳ��ʾ����
     * @return
     */
    public List<Tb_vmc_product> getScrollData(int start, int count) 
    {
        List<Tb_vmc_product> tb_inaccount = new ArrayList<Tb_vmc_product>();// �������϶���
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        // ��ȡ����������Ϣ
        Cursor cursor = db.rawQuery("select * from vmc_product limit ?,?", new String[] { String.valueOf(start), String.valueOf(count) });
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
