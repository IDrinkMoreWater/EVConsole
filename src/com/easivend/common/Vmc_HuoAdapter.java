package com.easivend.common;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import android.content.Context;
import com.easivend.dao.vmc_columnDAO;
import com.easivend.dao.vmc_productDAO;
import com.easivend.model.Tb_vmc_column;
import com.easivend.model.Tb_vmc_product;

public class Vmc_HuoAdapter 
{
	private String[] huoID = null;
	private String[] huoproID = null;
    private String[] huoRemain = null;
    private String[] huolasttime = null;
    private String[] proImage = null;
    // ��Ʒ���е�������Ʒ��Ϣ���䵽��Ʒ���ݽṹ������
    public void showProInfo(Context context,String param,Map<String, Integer> set,String cabID) 
 	{
 	    List<Tb_vmc_column> listinfos=null;//���ݱ�list�༯
 	    // ����InaccountDAO�������ڴ����ݿ�����ȡ���ݵ�Tb_vmc_column����
 	    vmc_columnDAO columnDAO = new vmc_columnDAO(context);
 	    vmc_productDAO productDAO = new vmc_productDAO(context);// ����InaccountDAO����
	    
 	    // ��ȡ����������Ϣ�����洢��List���ͼ�����
 		listinfos = columnDAO.getScrollData(cabID);
 	   
    	
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
           //����������Ӧ����Ʒ��Ϣ
           // ����List���ͼ���
    	   for (Tb_vmc_column tb_inaccount : listinfos) 
    	   {
    	    	if(
    	    			(cabID.equals(tb_inaccount.getCabineID())==true)
    	    			&&(huoID[m].equals(tb_inaccount.getColumnID())==true)
    	          )
    	    	{
	    	    	// �����������Ϣ��ϳ�һ���ַ������洢���ַ����������Ӧλ��
	    	    	huoproID[m] = tb_inaccount.getProductID();
	    	    	huoRemain[m] = String.valueOf(tb_inaccount.getPathRemain());
	    	    	huolasttime[m] = tb_inaccount.getLasttime().substring(0, 10);  
	    	    	//�õ������Ʒid��Ӧ��ͼƬ
	    	    	if(huoproID[m].equals("0")!=true)
	    	    	{
		    	    	// ��ȡ����������Ϣ�����洢��List���ͼ�����
		    		    Tb_vmc_product tb_product = productDAO.find(huoproID[m]);
		    		    if(tb_product!=null)
		    		    	proImage[m] = tb_product.getAttBatch1().toString();
	    	    	}
	    	    	break;
    	    	}
    	   }
           m++;// ��ʶ��1
       } 
 	    
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
