/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           Vmc_ProductAdapter.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        ��ȡ��Ʒ���е�������Ʒ��Ϣ�����䵽��Ʒ���ݽṹ������   
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.common;

import java.util.List;

import android.content.Context;

import com.easivend.dao.vmc_columnDAO;
import com.easivend.dao.vmc_productDAO;
import com.easivend.model.Tb_vmc_product;

public class Vmc_ProductAdapter
{	 
	 private String[] proID = null;
	 private String[] productID = null;
     private String[] proImage = null;
     private String[] promarket = null;
     private String[] prosales = null;
     private String[] procount = null;
     
     // ��Ʒ���е�������Ʒ��Ϣ���䵽��Ʒ���ݽṹ������
     //param��������,sort��������,classID������Ϣ
    public void showProInfo(Context context,String param,String sort,String classID) 
 	{
 	    //ArrayAdapter<String> arrayAdapter = null;// ����ArrayAdapter����
 	    List<Tb_vmc_product> listinfos=null;//���ݱ�list�༯
 	    // ����InaccountDAO�������ڴ����ݿ�����ȡ���ݵ�Tb_vmc_product����
 	    vmc_productDAO productdao = new vmc_productDAO(context);
 	    //�޹���������ѯ
 	    if(param.isEmpty()==true)
 	    {
 	    	//�����ѯ
 	    	if(classID.isEmpty()!=true)	
 	    	{
 			    // ��ȡ����������Ϣ�����洢��List���ͼ�����
 			    listinfos = productdao.getScrollData(classID);
 		    }	
 	    	else
 	    	{
	 		    // ��ȡ����������Ϣ�����洢��List���ͼ�����
	 		    listinfos = productdao.getScrollData(0, (int) productdao.getCount(),sort);
 	    	}
 	    }
 	    //�й���������ѯ
 	    else 
 	    {
 		    // ��ȡ����������Ϣ�����洢��List���ͼ�����
 		    listinfos = productdao.getScrollData(param,sort);
 	    }
 	    proID = new String[listinfos.size()];// �����ַ�������ĳ���
 	    productID = new String[listinfos.size()];// �����ַ�������ĳ���
 	    proImage = new String[listinfos.size()];// �����ַ�������ĳ���
 	    promarket = new String[listinfos.size()];// �����ַ�������ĳ���
 	    prosales = new String[listinfos.size()];// �����ַ�������ĳ���
 	    procount = new String[listinfos.size()];// �����ַ�������ĳ���
 	    int m = 0;// ����һ����ʼ��ʶ
 	    // ����List���ͼ���
 	    for (Tb_vmc_product tb_inaccount : listinfos) 
 	    {
 	        // �����������Ϣ��ϳ�һ���ַ������洢���ַ����������Ӧλ��
 	    	proID[m] = tb_inaccount.getProductID()+"-"+tb_inaccount.getProductName();
 	    	productID[m] = tb_inaccount.getProductID();
 	    	proImage[m] = tb_inaccount.getAttBatch1();
 	    	promarket[m] = String.valueOf(tb_inaccount.getMarketPrice());
 	    	prosales[m] = String.valueOf(tb_inaccount.getSalesPrice());
 	    	//�õ������Ʒid��Ӧ�Ĵ������
	    	if(productID[m]!=null)
	    	{
	    		vmc_columnDAO columnDAO = new vmc_columnDAO(context);// ����InaccountDAO����
    		    // ��ȡ����������Ϣ�����洢��List���ͼ�����
    		    procount[m] = String.valueOf(columnDAO.getproductCount(productID[m]));
	    	}
	    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproductID="+proID[m]+" marketPrice="
 					+promarket[m]+" salesPrice="+prosales[m]+" attBatch1="
 					+proImage[m]+" attBatch2="+tb_inaccount.getAttBatch2()+" attBatch3="+tb_inaccount.getAttBatch3()
 					+" procount="+procount[m]);
 	    	
 	    	m++;// ��ʶ��1
 	    }
 	    
 	}
	public String[] getProID() {
		return proID;
	}
	public void setProID(String[] proID) {
		this.proID = proID;
	}
	public String[] getProductID() {
		return productID;
	}
	public void setProductID(String[] productID) {
		this.productID = productID;
	}
	public String[] getProImage() {
		return proImage;
	}
	public void setProImage(String[] proImage) {
		this.proImage = proImage;
	}
	public String[] getPromarket() {
		return promarket;
	}
	public void setPromarket(String[] promarket) {
		this.promarket = promarket;
	}
	public String[] getProsales() {
		return prosales;
	}
	public void setProsales(String[] prosales) {
		this.prosales = prosales;
	}
	public String[] getProcount() {
		return procount;
	}
	public void setProcount(String[] procount) {
		this.procount = procount;
	}
    
}
