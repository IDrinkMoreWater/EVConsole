/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           ShowSortAdapter.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        Spinner�������࣬������������Ʒ���԰�����������ʽ     
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.common;

import java.util.ArrayList;
import java.util.List;

public class ShowSortAdapter
{
	private List<String> dataSortID = null,dataSortName=null;	
	public ShowSortAdapter()
	{
		super();
		// ��ȡ����������Ϣ�����洢��List���ͼ�����
	    dataSortID = new ArrayList<String>();
	    dataSortName = new ArrayList<String>();
	    dataSortID.add("sale");
	    dataSortName.add("sale�������");
	    dataSortID.add("marketPrice");
	    dataSortName.add("marketPriceԭ��");
	    dataSortID.add("salesPrice");
	    dataSortName.add("salesPrice���ۼ�");
	    dataSortID.add("shelfLife");
	    dataSortName.add("shelfLife��������");
	    dataSortID.add("colucount");
	    dataSortName.add("colucountʣ������");
	    dataSortID.add("onloadTime");
	    dataSortName.add("onloadTime�ϼ�ʱ��");
	    dataSortID.add("shoudong");
	    dataSortName.add("shoudong�ֶ�����");	
	}
	public List<String> getDataSortID() {
		return dataSortID;
	}
	public void setDataSortID(List<String> dataSortID) {
		this.dataSortID = dataSortID;
	}
	public List<String> getDataSortName() {
		return dataSortName;
	}
	public void setDataSortName(List<String> dataSortName) {
		this.dataSortName = dataSortName;
	}
	
	
	
}
