/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        vmc_column ������      
**------------------------------------------------------------------------------------------------------
** Created by:          yanbo 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.model;

import java.util.Date;

public class Tb_vmc_column
{
	private String cabineID;// �����
    private String columnID;// ������
    private String productID;// ��ƷID
    private int pathCount;// ��������
    private int pathRemain;// ����ʣ����Ʒ����
    private int columnStatus;// ����״̬0,δ����,1��ȷ��2���ϣ�3����
    private String lasttime;//�������¸���ʱ��
 // �����вι��캯����������ʼ��������Ϣʵ�����еĸ����ֶ�
	public Tb_vmc_column(String cabineID, String columnID, String productID,
			int pathCount, int pathRemain, int columnStatus, String lasttime) {
		super();
		this.cabineID = cabineID;
		this.columnID = columnID;
		this.productID = productID;
		this.pathCount = pathCount;
		this.pathRemain = pathRemain;
		this.columnStatus = columnStatus;
		this.lasttime = lasttime;
	}
	public String getCabineID() {
		return cabineID;
	}
	public void setCabineID(String cabineID) {
		this.cabineID = cabineID;
	}
	public String getColumnID() {
		return columnID;
	}
	public void setColumnID(String columnID) {
		this.columnID = columnID;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public int getPathCount() {
		return pathCount;
	}
	public void setPathCount(int pathCount) {
		this.pathCount = pathCount;
	}
	public int getPathRemain() {
		return pathRemain;
	}
	public void setPathRemain(int pathRemain) {
		this.pathRemain = pathRemain;
	}
	public int getColumnStatus() {
		return columnStatus;
	}
	public void setColumnStatus(int columnStatus) {
		this.columnStatus = columnStatus;
	}
	public String getLasttime() {
		return lasttime;
	}
	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}
    
}
