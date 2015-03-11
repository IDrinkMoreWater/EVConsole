package com.easivend.common;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;

import com.easivend.dao.vmc_productDAO;
import com.easivend.model.Tb_vmc_product;

public class Vmc_HuoAdapter 
{
	private String[] huoID = null;
	private String[] huoproID = null;
    private String[] huoRemain = null;
    private String[] huolasttime = null;
    private String[] proImage = null;
    // ��Ʒ���е�������Ʒ��Ϣ���䵽��Ʒ���ݽṹ������
    public void showProInfo(Context context,String param,Map<String, Integer> set) 
 	{
// 	    List<Tb_vmc_product> listinfos=null;//���ݱ�list�༯
// 	    // ����InaccountDAO�������ڴ����ݿ�����ȡ���ݵ�Tb_vmc_product����
// 	    vmc_productDAO productdao = new vmc_productDAO(context);
// 	    if(param.isEmpty()==true)
// 	    {
// 		    // ��ȡ����������Ϣ�����洢��List���ͼ�����
// 		    listinfos = productdao.getScrollData(0, (int) productdao.getCount(),sort);
// 	    }
// 	    else
// 	    {
// 		    // ��ȡ����������Ϣ�����洢��List���ͼ�����
// 		    listinfos = productdao.getScrollData(param,sort);
// 	    }
    	
 	    huoID = new String[set.size()];// �����ַ�������ĳ���
 	    huoproID = new String[set.size()];// �����ַ�������ĳ���
 	    huoRemain = new String[set.size()];// �����ַ�������ĳ���
 	    huolasttime = new String[set.size()];// �����ַ�������ĳ��� 	 
 	    proImage = new String[set.size()];// �����ַ�������ĳ��� 	 
 	    int m = 0;// ����һ����ʼ��ʶ
 	    //���ȫ������
 	   Set<Entry<String, Integer>> allset=set.entrySet();  //ʵ����
       Iterator<Entry<String, Integer>> iter=allset.iterator();
       while(iter.hasNext())
       {
           Entry<String, Integer> me=iter.next();
           huoID[m]=me.getKey();
           huoproID[m]="0";
           huoRemain[m]="0";
           huolasttime[m]="0";
           proImage[m]="0";
           //System.out.println(me.getKey()+"--"+me.getValue());
           m++;// ��ʶ��1
       } 
// 	    int m = 0;// ����һ����ʼ��ʶ
// 	    // ����List���ͼ���
// 	    for (Tb_vmc_product tb_inaccount : listinfos) 
// 	    {
// 	        // �����������Ϣ��ϳ�һ���ַ������洢���ַ����������Ӧλ��
// 	    	proID[m] = tb_inaccount.getProductID()+"-"+tb_inaccount.getProductName();
// 	    	productID[m] = tb_inaccount.getProductID();
// 	    	proImage[m] = tb_inaccount.getAttBatch1();
// 	    	promarket[m] = String.valueOf(tb_inaccount.getMarketPrice());
// 	    	prosales[m] = String.valueOf(tb_inaccount.getSalesPrice());
// 	    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproductID="+proID[m]+" marketPrice="
// 					+promarket[m]+" salesPrice="+prosales[m]+" attBatch1="
// 					+proImage[m]+" attBatch2="+tb_inaccount.getAttBatch2()+" attBatch3="+tb_inaccount.getAttBatch3());
// 	        m++;// ��ʶ��1
// 	    }
 	    
 	}
	public String[] getHuoID() {
		return huoID;
	}
	public void setHuoID(String[] huoID) {
		this.huoID = huoID;
	}
	public String[] getHuoproID() {
		return huoproID;
	}
	public void setHuoproID(String[] huoproID) {
		this.huoproID = huoproID;
	}
	public String[] getHuoRemain() {
		return huoRemain;
	}
	public void setHuoRemain(String[] huoRemain) {
		this.huoRemain = huoRemain;
	}
	public String[] getHuolasttime() {
		return huolasttime;
	}
	public void setHuolasttime(String[] huolasttime) {
		this.huolasttime = huolasttime;
	}
	public String[] getProImage() {
		return proImage;
	}
	public void setProImage(String[] proImage) {
		this.proImage = proImage;
	}
    
}
